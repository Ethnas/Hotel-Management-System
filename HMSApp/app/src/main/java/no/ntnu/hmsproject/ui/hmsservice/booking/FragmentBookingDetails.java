package no.ntnu.hmsproject.ui.hmsservice.booking;

import android.app.VoiceInteractor;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;

import no.ntnu.hmsproject.R;
import no.ntnu.hmsproject.domain.Booking;
import no.ntnu.hmsproject.network.ApiLinks;
import no.ntnu.hmsproject.network.GsonRequest;
import no.ntnu.hmsproject.ui.hmsservice.damagereport.FragmentDamRepDetailsArgs;

public class FragmentBookingDetails extends Fragment {

    TextView id;
    TextView roomType;
    TextView bookingStart;
    TextView bookingEnd;
    TextView bookingStatus;
    TextView roomNumber;
    Booking booking;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_booking_details, container, false);
        this.id = view.findViewById(R.id.booking_details_id);
        this.roomType = view.findViewById(R.id.booking_details_roomtype);
        this.bookingStart = view.findViewById(R.id.booking_details_datestart);
        this.bookingEnd = view.findViewById(R.id.booking_details_dateend);
        this.bookingStatus = view.findViewById(R.id.booking_details_status);
        this.roomNumber = view.findViewById(R.id.booking_details_roomnumber);
        FragmentBookingDetailsArgs args = FragmentBookingDetailsArgs.fromBundle(getArguments());
        int bookingId = args.getBookingId();
        getBooking(bookingId);
        return view;
    }

    private void getBooking(int bookingId) {
        Context context = getActivity();
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        String id = Integer.toString(bookingId);
        String url = ApiLinks.GET_SPECIFIC_BOOKING_URL + "?bookingid=" + id;
        GsonRequest gsonRequest = new GsonRequest(
                url, Request.Method.GET,
                Booking.class,
                new HashMap<String, String>(),
                new Response.Listener<Booking>() {
                    @Override
                    public void onResponse(Booking response) {
                        booking = response;
                        setTextFields();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println(error);
                    }
                }
        );
        requestQueue.add(gsonRequest);
    }

    private void setTextFields() {
        id.setText("Booking ID: " + booking.getBookingId());
        roomType.setText("Room type: " + booking.getRt().getRoomtype());
        bookingStart.setText("Start date: " + booking.getBookingStartDate());
        bookingEnd.setText("End date: "+ booking.getBookingEndDate());
        roomNumber.setText("Room number: " + booking.getRn().getRoomNumber());
        bookingStatus.setText("Booking status: " + booking.getBookingStatus());
    }
}