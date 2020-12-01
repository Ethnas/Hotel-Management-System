package no.ntnu.hmsproject.ui.hmsservice.staff;

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

import java.lang.reflect.Array;
import java.util.HashMap;

import no.ntnu.hmsproject.R;

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

            }
        });



        return view;
    }

    private void acceptBooking() {
        final HashMap<String, String> acceptBookingMap = new HashMap<>();
        acceptBookingMap.put("bookingid", bookingIdV.getText().toString());
        acceptBookingMap.put("bookingStatus", acceptV.getSelectedItem().toString());
        acceptBookingMap.put("roomNumber", roomV.getSelectedItem().toString());

        //TODO - StringRequest.
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long id) {
        adapterView.getItemAtPosition(pos);

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}