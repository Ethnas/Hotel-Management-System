package no.ntnu.hmsproject.ui.hmsservice.staff;

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
import android.widget.Spinner;

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

public class FragmentUpdateRoomStatus extends Fragment implements AdapterView.OnItemSelectedListener  {
    Spinner roomV;
    Spinner statusV;

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
        View view = inflater.inflate(R.layout.fragment_update_room_status, container, false);

        //Spinner for room
        roomV = (Spinner) view.findViewById(R.id.upd_roomstatus_room);
        ArrayAdapter adapterRoom = ArrayAdapter.createFromResource(getActivity(), R.array.room_array, android.R.layout.simple_spinner_item);
        adapterRoom.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        roomV.setAdapter(adapterRoom);

        //Spinner for status
        statusV = (Spinner) view.findViewById(R.id.upd_roomstatus_status);
        ArrayAdapter adapterStatus = ArrayAdapter.createFromResource(getActivity(), R.array.roomstatus_array , android.R.layout.simple_spinner_item);
        adapterStatus.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        statusV.setAdapter(adapterStatus);

        Button updateRoomStatusV = (Button) view.findViewById(R.id.upd_roomstatus_submit);

        updateRoomStatusV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateRoomStatus();
            }
        });


        return view;
    }

    private void updateRoomStatus() {
        String room = roomV.getSelectedItem().toString();
        String status = statusV.getSelectedItem().toString();

        Context context = getActivity();
        RequestQueue requestQueue = Volley.newRequestQueue(context);

        String url = ApiLinks.UPDATE_ROOM_STATUS_URL + "?roomnumber=" + room + "&roomstatus=" + status;

        StringRequest stringRequest = new StringRequest(
                Request.Method.PUT, url,
                response -> {
                    NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
                    navController.popBackStack();

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