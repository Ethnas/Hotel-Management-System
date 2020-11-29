package no.ntnu.hmsproject.network;

import com.android.volley.Response;
import com.android.volley.VolleyError;

public class ApiLinks {


    //skole ip
    public static final String BASE_URL = "http://10.22.184.117:8080/HMSProject/api/";

    public static final String CREATE_URL = BASE_URL + "auth/create";



    public static final Response.ErrorListener dumdumListener = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            error.printStackTrace();
            System.out.printf("Error: ", error.getMessage());
        }
    };
}
