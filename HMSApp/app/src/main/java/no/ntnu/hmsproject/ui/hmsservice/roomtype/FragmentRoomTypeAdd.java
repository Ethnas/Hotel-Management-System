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
import android.widget.Button;
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
import no.ntnu.hmsproject.domain.LoggedUser;
import no.ntnu.hmsproject.domain.RoomType;
import no.ntnu.hmsproject.network.ApiLinks;

public class FragmentRoomTypeAdd extends Fragment {
    TextView roomTypeV;
    TextView priceV;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onPrepareOptionsMenu(@NonNull Menu menu) {
        super.onPrepareOptionsMenu(menu);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_room_type_add, container, false);

        roomTypeV = view.findViewById(R.id.add_roomtype_price);
        priceV = view.findViewById(R.id.add_roomtype_price);

        Button addRoomTypeV = (Button) view.findViewById(R.id.addroomtype_submit);

        addRoomTypeV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addRoomType();
            }
        });


        return view;
    }

    private void addRoomType() {
        //Kind of a duplicate, here to verifiy that the maps are proper.
        final HashMap<String, String> addRoomTypeMap = new HashMap<>();
        addRoomTypeMap.put("roomtype", roomTypeV.getText().toString());
        addRoomTypeMap.put("roomPrice", priceV.getText().toString());

        String roomType = roomTypeV.getText().toString();

        String roomPrice = priceV.getText().toString();

        if (roomType.isEmpty()) {
            roomTypeV.setError("Ingen romtype fylt inn");
            roomTypeV.requestFocus();
            return;
        }
        if (roomPrice.isEmpty()) {
            priceV.getText().toString();
            priceV.requestFocus();
            return;
        }

        //Request
        Context context = getActivity();
        RequestQueue requestQueue = Volley.newRequestQueue(context);

        StringRequest stringRequest = new StringRequest(
                Request.Method.POST, ApiLinks.ADD_ROOMTYPE_URL,
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
        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                final HashMap<String, String> addRoomTypeMap = new HashMap<>();
                addRoomTypeMap.put("roomtype", roomTypeV.getText().toString());
                addRoomTypeMap.put("roomPrice", priceV.getText().toString());
                return addRoomTypeMap;
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
        System.out.println("Map: " + addRoomTypeMap);
        System.out.println("SR: " + stringRequest);

    }
}