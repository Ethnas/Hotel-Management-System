package no.ntnu.hmsproject.domain;

import com.android.volley.AuthFailureError;

import java.util.HashMap;
import java.util.Map;

public class LoggedUser {

    private static LoggedUser instance = null;
    private boolean isLoggedIn = false;
    private User user;
    private String jwt;


    public static LoggedUser getInstance() {
        if (instance == null) {
            instance = new LoggedUser();
        }
        return instance;
    }

    public boolean isLoggedIn() {
        return isLoggedIn;
    }

    public void setLoggedIn(boolean loggedIn) {
        isLoggedIn = loggedIn;
    }

    public void updateUser() {}

    public Map<String, String> getHeaders() throws AuthFailureError {
        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization, ", "Bearer " + jwt);
        return headers;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getJwt() {
        return jwt;
    }

    public void setJwt(String jwt) {
        this.jwt = jwt;
    }
}
