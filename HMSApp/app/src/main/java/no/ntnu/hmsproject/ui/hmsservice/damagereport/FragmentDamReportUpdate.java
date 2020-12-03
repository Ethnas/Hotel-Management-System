package no.ntnu.hmsproject.ui.hmsservice.damagereport;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
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
import no.ntnu.hmsproject.domain.DamageReport;
import no.ntnu.hmsproject.domain.LoggedUser;
import no.ntnu.hmsproject.network.ApiLinks;


public class FragmentDamReportUpdate extends Fragment {
    EditText damRepIdV;
    EditText damRepTitleV;
    EditText damRepBookingIdV;
    EditText damRepDescV;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dam_report_update, container, false);

        damRepIdV = view.findViewById(R.id.upd_damrep_bookingid);
        damRepTitleV = view.findViewById(R.id.upd_damrep_title);
        damRepBookingIdV = view.findViewById(R.id.upd_damrep_bookingid);
        damRepDescV = view.findViewById(R.id.upd_damrep_desc);

        Button updDamRepV = (Button) view.findViewById(R.id.updreport_submit);

        updDamRepV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updReport();
            }
        });

        return view;
    }

    private void updReport() {
        final HashMap<String, String> updDamRepMap = new HashMap<>();
        updDamRepMap.put("reportId", damRepIdV.getText().toString());
        updDamRepMap.put("damageTitle", damRepTitleV.getText().toString());
        updDamRepMap.put("bookingid", damRepBookingIdV.getText().toString());
        updDamRepMap.put("damageDesc", damRepDescV.getText().toString());

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

        Context context = getActivity();
        RequestQueue requestQueue = Volley.newRequestQueue(context);

        StringRequest stringRequest = new StringRequest(
                Request.Method.PUT, ApiLinks.UPDATE_DAMAGE_REPORT_URL,
                response -> {
                    NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
                    navController.popBackStack();

                    try {
                        JSONObject obj = new JSONObject(response);

                        if(!obj.getBoolean("error")) {
                            JSONObject jsonObject = obj.getJSONObject("damagereport");
                            DamageReport damageReport = new DamageReport(
                                jsonObject.getString("reportId"),
                                jsonObject.getString("damageTitle"),
                                jsonObject.getString("damageDesc"),
                                jsonObject.getString("bookingid")
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
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                final HashMap<String, String> updDamRepMap = new HashMap<>();
                updDamRepMap.put("reportId", damRepIdV.getText().toString());
                updDamRepMap.put("damageTitle", damageTitle);
                updDamRepMap.put("damageDesc", damageDesc);
                updDamRepMap.put("bookingid", bookingid);


                return updDamRepMap;
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
        System.out.println("Map: " + updDamRepMap);
        System.out.println("SR: " + stringRequest);
    }
}