package no.ntnu.hmsproject.ui.hmsservice.roomtype;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
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
import no.ntnu.hmsproject.domain.LoggedUser;
import no.ntnu.hmsproject.domain.RoomType;
import no.ntnu.hmsproject.network.ApiLinks;

public class FragmentRoomTypeUpdate extends Fragment {
    Spinner roomTypeV;
    EditText priceV;

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
        View view = inflater.inflate(R.layout.fragment_room_type_update, container, false);

        //spinnerthingfor romtpe
        roomTypeV = (Spinner) view.findViewById(R.id.upd_roomtype_roomtype);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(), R.array.roomtype_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        roomTypeV.setAdapter(adapter);

        priceV = view.findViewById(R.id.upd_roomtype_price);

        Button updRoomTypeV = (Button) view.findViewById(R.id.updroomtype_submit);

        updRoomTypeV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updRoomType();
            }
        });

        return view;
    }

    private void updRoomType() {
        final HashMap<String, String> updRoomTypeMap = new HashMap<>();
        updRoomTypeMap.put("roomtype", roomTypeV.getSelectedItem().toString());
        updRoomTypeMap.put("roomPrice", priceV.getText().toString());

        String roomType = roomTypeV.getSelectedItem().toString();
        String roomPrice = priceV.getText().toString();

        if (roomPrice.isEmpty()) {
            priceV.getText().toString();
            priceV.requestFocus();
            return;
        }

        Context context = getActivity();
        RequestQueue requestQueue = Volley.newRequestQueue(context);

        StringRequest stringRequest = new StringRequest(
                Request.Method.PUT, ApiLinks.UPDATE_ROOMTYPE_URL,
                response -> {
                    try {
                        JSONObject obj = new JSONObject(response);

                        if (!obj.getBoolean("error")) {
                            JSONObject jsonObject = obj.getJSONObject("roomtype");
                            RoomType roomType1 = new RoomType(
                                    jsonObject.getString("roomType"),
                                    jsonObject.getString("roomPrice")
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
                final HashMap<String, String> updRoomTypeMap = new HashMap<>();
                updRoomTypeMap.put("roomtype", roomTypeV.getSelectedItem().toString());
                updRoomTypeMap.put("roomPrice", priceV.getText().toString());
                return updRoomTypeMap;
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
        System.out.println("Map: " + updRoomTypeMap);
        System.out.println("SR: " + stringRequest);
    }
}