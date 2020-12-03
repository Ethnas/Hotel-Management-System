package no.ntnu.hmsproject.ui.hmsservice.booking;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
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


public class FragmentBookingAdd extends Fragment implements AdapterView.OnItemSelectedListener  {
    //TextView roomTypeV;
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

        menu.clear();
        super.onPrepareOptionsMenu(menu);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_booking_add, container, false);

        //roomTypeV = view.findViewById(R.id.addBooking_roomtype);

        //Spinner for roomtype
        roomTypeV = (Spinner) view.findViewById(R.id.addBooking_roomtype);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(), R.array.roomtype_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        roomTypeV.setAdapter(adapter);

        startDateV = view.findViewById(R.id.addBooking_startdate);
        endDateV = view.findViewById(R.id.addBooking_enddate);

        Button addbookingV = (Button) view.findViewById(R.id.addbooking_submit);

        addbookingV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addBooking();

            }
        });

        Button clearText = (Button) view.findViewById(R.id.clear_text_submit);
        clearText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startDateV.getText().clear();
                endDateV.getText().clear();
            }
        });

        return view;
    }

    private void addBooking() {
        //Kind of a duplicate, here to verify that the maps are proper.
        final HashMap<String, String> addBookingMap = new HashMap<>();
        addBookingMap.put("bookingRoomType", roomTypeV.getSelectedItem().toString());
        addBookingMap.put("bookingStartDate", startDateV.getText().toString());
        addBookingMap.put("bookingEndDate", endDateV.getText().toString());

        //Loads the textview into string variables and checks if they are not empty.

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

        //StringRequest

        Context context = getActivity();
        RequestQueue requestQueue = Volley.newRequestQueue(context);

        StringRequest stringRequest = new StringRequest(
                Request.Method.POST, ApiLinks.ADD_BOOKING_URL,
                response -> {
                    NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
                    NavDirections action = FragmentBookingAddDirections.actionNavBookaddToThenkyou();
                    navController.navigate(action);
                    try {
                        JSONObject obj = new JSONObject(response);

                        if (!obj.getBoolean("error")) {
                            JSONObject jsonObject = obj.getJSONObject("booking");
                            Booking booking = new Booking(
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
                    Toast.makeText(getActivity(), "Soemthing wenth wrong ", Toast.LENGTH_LONG).show();
                    System.out.println("Something went wrong");
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                final HashMap<String, String> addBookingMap = new HashMap<>();
                addBookingMap.put("bookingRoomType", roomType);
                addBookingMap.put("bookingStartDate", startDate);
                addBookingMap.put("bookingEndDate", endDate);
                return addBookingMap;
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
        System.out.println("Map: " + addBookingMap);
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