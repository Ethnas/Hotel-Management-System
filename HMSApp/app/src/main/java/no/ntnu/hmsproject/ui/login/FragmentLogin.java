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
import android.view.Menu;
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
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.api.Api;

import java.util.HashMap;
import java.util.Map;

import no.ntnu.hmsproject.R;
import no.ntnu.hmsproject.domain.LoggedUser;
import no.ntnu.hmsproject.network.ApiLinks;


public class FragmentLogin extends Fragment {
    EditText uidView;
    EditText pwdView;

    private String token;
    LoggedUser loggedUser = new LoggedUser();

    public FragmentLogin() {}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onPrepareOptionsMenu(@NonNull Menu menu) {

        menu.clear();
        super.onPrepareOptionsMenu(menu);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        uidView = view.findViewById(R.id.login_uid);
        pwdView = view.findViewById(R.id.login_pwd);
        Button submitView = (Button) view.findViewById(R.id.login_submit);


        submitView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginUser();


            }
        });



        Button clearTextView = (Button) view.findViewById(R.id.clear_text_submit);
        clearTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uidView.getText().clear();
                pwdView.getText().clear();
            }
        });

        return view;
    }

    public void loginUser() {
        Context context = getActivity();
        RequestQueue requestQueue = Volley.newRequestQueue(context);

        String url = ApiLinks.LOGIN_URL + "?uid=" + uidView.getText() + "&pwd=" + pwdView.getText();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                response -> {
                    NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
                    NavDirections action = FragmentLoginDirections.actionNavToHome();
                    navController.navigate(action);

                    LoggedUser.getInstance().setJwt(response);
                    LoggedUser.getInstance().setLoggedIn(true);

                    System.out.println(response);

                    System.out.println("Things went smooth");


                }, error -> {
            System.out.println(error);
            System.out.println("Something went wrong");
        });

        System.out.println(stringRequest);
        requestQueue.add(stringRequest);
    }



    //TODO - SLETT DEN HER NÅR FERDIGSTILT!
    public void testLogin() {
        Context context = getActivity();
        RequestQueue requestQueue = Volley.newRequestQueue(context);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, ApiLinks.CURRENT_USER_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                System.out.println("nicerdicer");
                System.out.println(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("not nice");
                System.out.println(error);
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<String, String>();

                String authToken = "Bearer " + LoggedUser.getInstance().getJwt();
                headers.put("Authorization", authToken);

                System.out.println("(Inside call) Token: " + LoggedUser.getInstance().getJwt());
                return headers;
            }
        };

        System.out.println("Token: " + LoggedUser.getInstance().getJwt());
        requestQueue.add(stringRequest);
        System.out.println(stringRequest);
    }
}