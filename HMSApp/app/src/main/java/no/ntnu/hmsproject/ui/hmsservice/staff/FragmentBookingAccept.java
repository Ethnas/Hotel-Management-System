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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.lang.reflect.Array;
import java.util.HashMap;
import java.util.Map;

import no.ntnu.hmsproject.R;
import no.ntnu.hmsproject.domain.LoggedUser;
import no.ntnu.hmsproject.network.ApiLinks;

public class FragmentBookingAccept extends Fragment implements AdapterView.OnItemSelectedListener{
    TextView bookingIdV;
    Spinner acceptV;
    Spinner statusV;
    Spinner roomV;

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
        View view = inflater.inflate(R.layout.fragment_booking_accept, container, false);

        bookingIdV = view.findViewById(R.id.acceptbooking_bookingid);

        //Spinner for accept
        acceptV = (Spinner) view.findViewById(R.id.acceptbooking_accepted);
        ArrayAdapter adapterAccept = ArrayAdapter.createFromResource(getActivity(), R.array.acceptbooking_accept_array, android.R.layout.simple_spinner_item);
        adapterAccept.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        acceptV.setAdapter(adapterAccept);

        /*
        //Spinner for status
        statusV = (Spinner) view.findViewById(R.id.acceptbooking_status);
        ArrayAdapter adapterStatus = ArrayAdapter.createFromResource(getActivity(), R.array.acceptbooking_status_array, android.R.layout.simple_spinner_item);
        adapterStatus.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        statusV.setAdapter(adapterStatus);
         */

        //Spinner for room
        roomV = (Spinner) view.findViewById(R.id.acceptbooking_room);
        ArrayAdapter adapterRoom = ArrayAdapter.createFromResource(getActivity(), R.array.room_array, android.R.layout.simple_spinner_item);
        adapterRoom.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        roomV.setAdapter(adapterRoom);


        Button acceptBookingV = (Button) view.findViewById(R.id.acceptbooking_submit);

        acceptBookingV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                acceptBooking();
            }
        });



        return view;
    }

    private void acceptBooking() {
        final HashMap<String, String> acceptBookingMap = new HashMap<>();
        acceptBookingMap.put("bookingid", bookingIdV.getText().toString());
        acceptBookingMap.put("bookingStatus", acceptV.getSelectedItem().toString());
        acceptBookingMap.put("roomNumber", roomV.getSelectedItem().toString());


        String bookingId = acceptV.getSelectedItem().toString();
        String bookingStatus = acceptV.getSelectedItem().toString();
        String bookingRoom = roomV.getSelectedItem().toString();

        Context context = getActivity();
        RequestQueue requestQueue = Volley.newRequestQueue(context);

        String url = ApiLinks.ACCEPT_BOOKING_URL + "?bookingid=" + bookingId
                + "&bookingAccepted=" + bookingStatus + "&RoomNumber=" + bookingRoom;

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
        System.out.println("SR: "+ stringRequest);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long id) {
        adapterView.getItemAtPosition(pos);

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}