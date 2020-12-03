package no.ntnu.hmsproject.ui.hmsservice.damagereport;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import no.ntnu.hmsproject.R;
import no.ntnu.hmsproject.domain.DamageReport;
import no.ntnu.hmsproject.domain.LoggedUser;
import no.ntnu.hmsproject.network.ApiLinks;

public class FragmentDamReportAdd extends Fragment {
    private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 1888;
    EditText damRepTitleV;
    EditText damRepBookingIdV;
    EditText damRepDescV;
    ImageView imageView;
    Bitmap image;
    byte[] imageBytes;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dam_report_add, container, false);
        Button imageButton = (Button) view.findViewById(R.id.add_image);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), "You Clicked", Toast.LENGTH_LONG).show();

                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
            }
        });
        imageView = view.findViewById(R.id.add_image_view);
        damRepTitleV = view.findViewById(R.id.add_damrep_title);
        damRepBookingIdV = view.findViewById(R.id.add_damrep_bookingid);
        damRepDescV = view.findViewById(R.id.add_damrep_desc);

        Button addDamRepV = (Button) view.findViewById(R.id.addreport_submit);

        addDamRepV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addReport();
            }
        });


        return view;
    }


    private void addReport() {
        final String imageString = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        //Kind of a duplicate, here to verify that the maps are proper.
        final HashMap<String, String> addDamRepMap = new HashMap<>();
        addDamRepMap.put("damageTitle", damRepTitleV.getText().toString());
        addDamRepMap.put("bookingid", damRepBookingIdV.getText().toString());
        addDamRepMap.put("damageDesc", damRepDescV.getText().toString());
        addDamRepMap.put("image", imageString);


        //Loads the textview into string variables and checks if they are not empty.

        String damageTitle = damRepTitleV.getText().toString();
        String bookingid = damRepBookingIdV.getText().toString();
        String damageDesc = damRepDescV.getText().toString();

        if (damageTitle.isEmpty()) {
            damRepTitleV.setError("Ingen tittel fylt inn");
            damRepTitleV.requestFocus();
            return;
        }

        if (bookingid.isEmpty()) {
            damRepBookingIdV.setError("Ingen id fylt inn");
            damRepBookingIdV.requestFocus();
            return;
        }

        if (damageDesc.isEmpty()) {
            damRepDescV.setError("Ingen beskrivelse fylt inn");
            damRepDescV.requestFocus();
            return;
        }

        if (imageString.isEmpty()) {
            damRepDescV.setError("Ingen bilde lagt til");
            damRepDescV.requestFocus();
            return;
        }

        //Call

        Context context = getActivity();
        RequestQueue requestQueue = Volley.newRequestQueue(context);

        StringRequest stringRequest = new StringRequest(
                Request.Method.POST, ApiLinks.ADD_DAMAGE_REPORT_URL,
                response -> {
                    try {
                        JSONObject obj = new JSONObject(response);

                        if (!obj.getBoolean("error")) {
                            JSONObject jsonObject = obj.getJSONObject("damagereport");
                            DamageReport damageReport = new DamageReport(
                                    jsonObject.getString("damageTitle"),
                                    jsonObject.getString("bookingid"),
                                    jsonObject.getString("damageDesc"),
                                    jsonObject.getString("image")
                            );
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> {
                    Toast.makeText(getActivity(), "Soemthing wenth wrong ", Toast.LENGTH_LONG).show();
                    System.out.println("Something went wrong");
                }
        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                final HashMap<String, String> addDamRepMap = new HashMap<>();

                addDamRepMap.put("damageTitle", damageTitle);
                addDamRepMap.put("bookingid", bookingid);
                addDamRepMap.put("damageDesc", damageDesc);
                addDamRepMap.put("image", imageString);

                return addDamRepMap;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<String, String>();

                String authToken = "Bearer " + LoggedUser.getInstance().getJwt();
                headers.put("Authorization", authToken);

                System.out.println("(Inside call) Token: " + LoggedUser.getInstance().getJwt());
                return headers;
            }
        };

        requestQueue.add(stringRequest);
        System.out.println("Map: " + addDamRepMap);
        System.out.println("SR: " + stringRequest);

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                image = (Bitmap) intent.getExtras().get("data");
                ByteArrayOutputStream stream = new ByteArrayOutputStream();

                image.compress(Bitmap.CompressFormat.PNG, 100, stream);

                imageBytes = stream.toByteArray();

                imageView.setImageBitmap(image);
            }
        }
    }
}