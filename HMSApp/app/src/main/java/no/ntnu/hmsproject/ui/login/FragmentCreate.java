package no.ntnu.hmsproject.ui.login;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

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
    TextView emlV;
    TextView uidV;
    TextView firstNameV;
    TextView lastNameV;
    TextView pwdV;

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
        pwdV = view.findViewById(R.id.create_pwd);
        Button createV = (Button) view.findViewById(R.id.create_submit);

        createV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.create_submit:
                        createUser();
                        break;
                }
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
        createMap.put("pwd", pwdV.getText().toString());

        Context context = getActivity();
        RequestQueue requestQueue = Volley.newRequestQueue(context);


        //Create request
        GsonRequest<User> userGsonRequest = new GsonRequest(
                ApiLinks.CREATE_URL, Request.Method.POST, User.class, new HashMap<String, String>(), new Response.Listener<User>() {
            @Override
            public void onResponse(User response) {
                LoggedUser.getInstance().setUser(response);
                Navigation.findNavController(getView()).popBackStack();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(), "something went wrong", Toast.LENGTH_LONG).show();
                System.out.println(error);
            }
        }
        ) {
            @Override
            public String getBodyContentType() {
                return "application/x-www-form-urlencoded; charset=UTF-8";
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return createMap;
            }
        };

        requestQueue.add(userGsonRequest);

    }
}