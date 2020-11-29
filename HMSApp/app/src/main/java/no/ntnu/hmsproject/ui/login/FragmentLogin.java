package no.ntnu.hmsproject.ui.login;

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

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import no.ntnu.hmsproject.R;
import no.ntnu.hmsproject.domain.LoggedUser;


public class FragmentLogin extends Fragment {
    TextView uidView;
    TextView pwdView;

    private String token;
    LoggedUser loggedUser = new LoggedUser();

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
        Button testKnapp = (Button) view.findViewById(R.id.testknapp);

        submitView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Context context = getActivity();
                RequestQueue requestQueue = Volley.newRequestQueue(context);
                String url = "http://localhost:8080/HMSProject/api/auth/login" + "?uid=" + uidView.getText() + "?pwd=" + pwdView.getText();

                StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                response -> {
                    LoggedUser.getInstance().setJwt(response);
                    LoggedUser.getInstance().setLoggedIn(true);

                    System.out.println("Things went smooth");


                }, error -> {
                    System.out.println("Something went wrong");
                });
                requestQueue.add(stringRequest);
            }
        });

        return view;
    }
}