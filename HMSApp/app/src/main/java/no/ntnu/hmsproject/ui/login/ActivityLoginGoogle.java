package no.ntnu.hmsproject.ui.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.HashMap;
import java.util.Map;

import no.ntnu.hmsproject.R;
import no.ntnu.hmsproject.domain.LoggedUser;
import no.ntnu.hmsproject.network.ApiLinks;


public class ActivityLoginGoogle extends AppCompatActivity {

    private static final int RC_SIGN_IN = 3;
    private static final String TAG = "ActivityLoginGoogle";
    GoogleSignInClient googleSignInClient;
    LoggedUser loggedUser = new LoggedUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_google);
        getSupportActionBar().show();

        // Request only the user's ID token, which can be used to identify the
        // user securely to your backend. This will contain the user's basic
        // profile (name, profile picture URL, etc) so you should not need to
        // make an additional call to personalize your application.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.server_client_id))
                .requestEmail()
                .build();

        // Build a GoogleSignInClient with the options specified by gso.
        googleSignInClient = GoogleSignIn.getClient(this, gso);


        // Set the dimensions of the sign-in button.
        SignInButton signInButton = new SignInButton(this);
        signInButton.findViewById(R.id.sign_in_button);
        signInButton.setSize(SignInButton.SIZE_STANDARD);

        findViewById(R.id.sign_in_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });

    }

    @Override
    public void onStart() {
        super.onStart();

        googleSignInClient.silentSignIn ()
                .addOnCompleteListener (this, new OnCompleteListener<GoogleSignInAccount> () {

                    @Override
                    public void onComplete (@NonNull Task<GoogleSignInAccount> task) {
                        handleSignInResult(task);
                    }
                });

    }

    private void sendIdToServer(String idToken) {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = ApiLinks.LOGIN_GOOGLE_URL;

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                response -> {
                    LoggedUser.getInstance().setJwt(response);
                    LoggedUser.getInstance().setLoggedIn(true);

                    System.out.println(response);

                    System.out.println("Things went smooth");

                }, error -> {
            System.out.println("Shit very fuck");
        }) {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String> params = new HashMap<String, String>();
                params.put("idToken", idToken);
                params.put("clientid", getString(R.string.server_client_id));

                return params;
            }
        };
        queue.add(stringRequest);
    }



    private void signIn() {
        Intent signInIntent = googleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
        // This task is always completed immediately, there is no need to attach an
        // asynchronous listener.
        Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(signInIntent);
        handleSignInResult(task);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(@NonNull Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            String idToken = account.getIdToken();
            sendIdToServer(idToken);

            //TODO updateUI(account);
        } catch (ApiException e) {
            Log.w(TAG, "handleSignInResult:error", e);
            //TODO updateUI(null);
        }
    }
}