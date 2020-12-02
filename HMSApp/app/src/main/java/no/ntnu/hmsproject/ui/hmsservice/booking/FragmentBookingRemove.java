package no.ntnu.hmsproject.ui.hmsservice.booking;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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
import no.ntnu.hmsproject.ui.login.FragmentCreate;


public class FragmentBookingRemove extends Fragment implements AdapterView.OnItemSelectedListener {
    EditText bookingIdV;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_booking_remove, container, false);

        bookingIdV = view.findViewById(R.id.removebooking_id);

        Button removeBookingV = (Button) view.findViewById(R.id.removebooking_submit);
        removeBookingV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                removeBooking();
            }
        });

        //Clear input field button
        Button clearTextView = (Button) view.findViewById(R.id.clear_text_submit);
        clearTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bookingIdV.getText().clear();
            }
        });

        return view;
    }

    private void removeBooking() {
        String bookingId = bookingIdV.getText().toString();

        if (bookingId.isEmpty()) {
            bookingIdV.setError("Empty field, please fill out");
            bookingIdV.requestFocus();
            return;
        }

        Context context = getActivity();
        RequestQueue requestQueue = Volley.newRequestQueue(context);

        String url = ApiLinks.REMOVE_BOOKING_URL + "?bookingid=" + bookingId;

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
        ){
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
        System.out.println("BOOKINGID: " + bookingId); //TODO - Fjern, er her for debug årsaker
        System.out.println("SR: " + stringRequest);

    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long id) {
        adapterView.getItemAtPosition(pos);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}