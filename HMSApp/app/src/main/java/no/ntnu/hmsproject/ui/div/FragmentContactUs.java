package no.ntnu.hmsproject.ui.div;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import no.ntnu.hmsproject.R;
import no.ntnu.hmsproject.domain.ContactUs;
import no.ntnu.hmsproject.network.ApiLinks;
import no.ntnu.hmsproject.ui.login.FragmentLoginDirections;

public class FragmentContactUs extends Fragment {
    EditText contactNameV;
    EditText contactEmailV;
    EditText contactSubjectV;
    EditText contactMessageV;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contact_us, container, false);

        contactNameV = view.findViewById(R.id.cs_name);
        contactEmailV = view.findViewById(R.id.cs_name);
        contactSubjectV = view.findViewById(R.id.cs_name);
        contactMessageV = view.findViewById(R.id.cs_name);

        Button contactSendV = (Button) view.findViewById(R.id.cs_send);

        contactSendV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                contactUs();


                Toast.makeText(getActivity(), "Thank you", Toast.LENGTH_LONG).show();
                NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
                navController.popBackStack();
            }
        });


        return view;
    }

    public void contactUs() {
        String contactName = contactNameV.getText().toString();
        String contactEmail = contactEmailV.getText().toString();
        String contactSubject = contactSubjectV.getText().toString();
        String contactMessage = contactMessageV.getText().toString();

        final HashMap<String, String> contactUsMap = new HashMap<>();
        contactUsMap.put("contactName", contactName);
        contactUsMap.put("contactEmail", contactEmail);
        contactUsMap.put("contactSubject", contactSubject);
        contactUsMap.put("contactMessage", contactMessage);

        Context context = getActivity();
        RequestQueue requestQueue = Volley.newRequestQueue(context);

        StringRequest stringRequest = new StringRequest(
                Request.Method.POST, ApiLinks.CONTACT_US_URL,
                response -> {

                    try {
                        JSONObject obj = new JSONObject(response);

                        if (!obj.getBoolean("error")) {
                            JSONObject jsonObject = obj.getJSONObject("contactus");
                            ContactUs contactUs = new ContactUs(
                                    jsonObject.getString("contactName"),
                                    jsonObject.getString("contactEmail"),
                                    jsonObject.getString("contactSubject"),
                                    jsonObject.getString("contactMessage")
                            );
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> {
                    Toast.makeText(getActivity(), "Soemthing wenth wrong ", Toast.LENGTH_LONG).show();
                    System.out.println("Something went wrong");
                    System.out.println("fuck");
                    error.printStackTrace();
                }
        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                final HashMap<String, String> contactUsMap = new HashMap<>();
                contactUsMap.put("contactName", contactName);
                contactUsMap.put("contactEmail", contactEmail);
                contactUsMap.put("contactSubject", contactSubject);
                contactUsMap.put("contactMessage", contactMessage);
                return contactUsMap;
            }
        };

        System.out.println("MAP: " + contactUsMap);
        System.out.println("SR: " + stringRequest);

    }
}