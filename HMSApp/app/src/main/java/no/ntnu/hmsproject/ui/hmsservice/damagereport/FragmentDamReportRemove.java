package no.ntnu.hmsproject.ui.hmsservice.damagereport;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

import no.ntnu.hmsproject.R;
import no.ntnu.hmsproject.domain.LoggedUser;
import no.ntnu.hmsproject.network.ApiLinks;

public class FragmentDamReportRemove extends Fragment {
    EditText reportIdV;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dam_report_remove, container, false);

        reportIdV = view.findViewById(R.id.remove_reportid);

        Button removeDamRepV = (Button) view.findViewById(R.id.remove_damrep_submit);

        removeDamRepV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                removeDamReport();
            }
        });


        return view;
    }

    private void removeDamReport() {
        String reportId = reportIdV.getText().toString();

        if (reportId.isEmpty()) {
            reportIdV.setError("Empty field, please fill out");
            reportIdV.requestFocus();
            return;
        }

        Context context = getActivity();
        RequestQueue requestQueue = Volley.newRequestQueue(context);

        String url = ApiLinks.REMOVE_DAMAGE_REPORT_URL + "?reportId=" + reportId;

        StringRequest stringRequest = new StringRequest(
                Request.Method.DELETE, url,
                response -> {
                    System.out.println(response);
                    System.out.println("Things went smooth");
                },
                error -> {
                    System.out.println(error);
                    System.out.println("Something went wrong");
                }
        ) {
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
        System.out.println("BOOKINGID: " + reportId); //TODO - Fjern, er her for debug årsaker
        System.out.println("SR: " + stringRequest);
    }
}