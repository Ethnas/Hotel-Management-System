package no.ntnu.hmsproject.ui.login;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.api.Api;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import no.ntnu.hmsproject.R;
import no.ntnu.hmsproject.domain.LoggedUser;
import no.ntnu.hmsproject.domain.User;
import no.ntnu.hmsproject.network.ApiLinks;
import no.ntnu.hmsproject.network.GsonRequest;

/**
 *
 */
public class FragmentCreate extends Fragment {
    EditText emlV;
    EditText uidV;
    EditText firstNameV;
    EditText lastNameV;
    EditText tlfV;
    EditText pwdV;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_create, container, false);

        emlV = view.findViewById(R.id.create_eml);
        uidV = view.findViewById(R.id.create_uid);
        firstNameV = view.findViewById(R.id.create_firstname);
        lastNameV = view.findViewById(R.id.create_lastname);
        tlfV = view.findViewById(R.id.create_tlf);
        pwdV = view.findViewById(R.id.create_pwd);
        Button createV = (Button) view.findViewById(R.id.create_submit);

        createV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createUser();
            }
        });

        return view;
    }


    private void createUser() {

        //Create hashmap with the details of the user inside it
        final HashMap<String, String> createMap = new HashMap<>();
        createMap.put("eml", emlV.getText().toString());
        createMap.put("uid", uidV.getText().toString());
        createMap.put("firstName", firstNameV.getText().toString());
        createMap.put("lastName", lastNameV.getText().toString());
        createMap.put("phoneNumber", tlfV.getText().toString());
        createMap.put("pwd", pwdV.getText().toString());

        Context context = getActivity();
        RequestQueue requestQueue = Volley.newRequestQueue(context);

        StringRequest stringRequest = new StringRequest(
                Request.Method.POST, ApiLinks.CREATE_URL,
                response -> {
                    NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
                    NavDirections action = FragmentCreateDirections.actionNavToLogin();
                    navController.navigate(action);

                    try {
                        JSONObject obj = new JSONObject(response);

                        if (!obj.getBoolean("error")) {
                            JSONObject jsonObject = obj.getJSONObject("user");
                            User user = new User(
                                    jsonObject.getString("eml"),
                                    jsonObject.getString("uid"),
                                    jsonObject.getString("firstName"),
                                    jsonObject.getString("lastName"),
                                    jsonObject.getInt("phoneNumber"),
                                    jsonObject.getString("pwd")
                            );
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> {
                    System.out.println("Error: " + error);
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                final HashMap<String, String> createMap = new HashMap<>();
                createMap.put("eml", emlV.getText().toString());
                createMap.put("uid", uidV.getText().toString());
                createMap.put("firstName", firstNameV.getText().toString());
                createMap.put("lastName", lastNameV.getText().toString());
                createMap.put("phoneNumber", tlfV.getText().toString());
                createMap.put("pwd", pwdV.getText().toString());
                return createMap;
            }
        };

        requestQueue.add(stringRequest);
        System.out.println("MAp: " + createMap);
        System.out.println("SR " + stringRequest);
    }
}