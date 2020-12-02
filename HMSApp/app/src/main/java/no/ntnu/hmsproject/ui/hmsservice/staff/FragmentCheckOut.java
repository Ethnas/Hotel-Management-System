package no.ntnu.hmsproject.ui.hmsservice.staff;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.Menu;
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

public class FragmentCheckOut extends Fragment {
    EditText bookingidV;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }


    @Override
    public void onPrepareOptionsMenu(@NonNull Menu menu) {
        super.onPrepareOptionsMenu(menu);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_check_out, container, false);

        bookingidV = view.findViewById(R.id.check_out_bookingid);
        Button checkOutSubmitView = (Button) view.findViewById(R.id.check_out_submit);

        checkOutSubmitView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkOut();
            }
        });


        return view;
    }

    private void checkOut() {
        Context context = getActivity();
        RequestQueue requestQueue = Volley.newRequestQueue(context);

        String bookingId = bookingidV.getText().toString();

        String url = ApiLinks.CHECK_OUT_URL + "?bookingid=" + bookingId;

        StringRequest stringRequest = new StringRequest(
                Request.Method.PUT, url,
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
        System.out.println("SR: " + stringRequest);
    }
}