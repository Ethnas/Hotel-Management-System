package no.ntnu.hmsproject.ui.hmsservice.booking;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
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
import no.ntnu.hmsproject.domain.Booking;
import no.ntnu.hmsproject.domain.LoggedUser;
import no.ntnu.hmsproject.network.ApiLinks;


public class FragmentBookingUpdate extends Fragment implements AdapterView.OnItemSelectedListener {
    EditText bookingIdV;
    Spinner roomTypeV;
    EditText startDateV;
    EditText endDateV;

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
        View view = inflater.inflate(R.layout.fragment_booking_update, container, false);

        //Spinner for roomtype
        roomTypeV = (Spinner) view.findViewById(R.id.updBooking_roomtype);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(), R.array.roomtype_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        roomTypeV.setAdapter(adapter);

        bookingIdV = view.findViewById(R.id.updbooking_id);
        startDateV = view.findViewById(R.id.updBooking_startdate);
        endDateV = view.findViewById(R.id.updBooking_enddate);

        Button addbookingV = (Button) view.findViewById(R.id.updbooking_submit);

        addbookingV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateBooking();
            }
        });


        return view;
    }

    private void updateBooking() {
        final HashMap<String, String> updBookingMap = new HashMap<>();
        updBookingMap.put("bookingid", bookingIdV.getText().toString());
        updBookingMap.put("bookingRoomType", roomTypeV.getSelectedItem().toString());
        updBookingMap.put("bookingStartDate", startDateV.getText().toString());
        updBookingMap.put("bookingEndDate", endDateV.getText().toString());

        //Loads the textview into string variables and checks if they are not empty.

        String bookingid = bookingIdV.getText().toString();
        String roomType = roomTypeV.getSelectedItem().toString();
        String startDate = startDateV.getText().toString();
        String endDate = endDateV.getText().toString();

        if (startDate.isEmpty()) {
            startDateV.setError("Ingen startsdato fylt inn");
            startDateV.requestFocus();
            return;
        }

        if (endDate.isEmpty()) {
            endDateV.setError("Ingen sluttdato fylt inn");
            endDateV.requestFocus();
            return;
        }

        //String Request

        Context context = getActivity();
        RequestQueue requestQueue = Volley.newRequestQueue(context);

        StringRequest stringRequest = new StringRequest(
                Request.Method.PUT, ApiLinks.UPDATE_BOOKING_URL,
                response -> {
                    NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
                    navController.popBackStack();

                    try {
                        JSONObject obj = new JSONObject(response);

                        if (!obj.getBoolean("error")) {
                            JSONObject jsonObject = obj.getJSONObject("booking");
                            Booking booking = new Booking(
                                    jsonObject.getString("bookingid"),
                                    jsonObject.getString("bookingRoomType"),
                                    jsonObject.getString("bookingStartDate"),
                                    jsonObject.getString("bookingEndDate")
                            );
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                },
                error -> {
                    System.out.println("Something went wrong");
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                final HashMap<String, String> updBookingMap = new HashMap<>();
                updBookingMap.put("bookingid", bookingid);
                updBookingMap.put("bookingRoomType", roomType);
                updBookingMap.put("bookingStartDate", startDate);
                updBookingMap.put("bookingEndDate", endDate);
                return updBookingMap;
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
        System.out.println("Map: " + updBookingMap);
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