package no.ntnu.hmsproject.network;

import com.android.volley.Response;
import com.android.volley.VolleyError;

public class ApiLinks {


    //skole ip
    public static final String BASE_URL = "https://hms.northeurope.cloudapp.azure.com/HMSProject-1.0-SNAPSHOT/api/";

    public static final String CREATE_URL = BASE_URL + "auth/create";
    public static final String CURRENT_USER_URL = BASE_URL + "auth/currentuser";


    //-------------------Service
    //Booking
    public static final String ADD_BOOKING_URL = BASE_URL + "hotel/addbooking";

    //Damage Report
    public static final String GET_ALL_DAMAGE_REPORT_URL = BASE_URL + "hotel/getDamageReports";
    public static final String ADD_DAMAGE_REPORT_URL = BASE_URL + "hotel/addDamageReport";

    //Room Type
    public static final String ADD_ROOMTYPE_URL = BASE_URL + "hotel/addRoomType";

    //Staff
    public static final String ACCEPT_BOOKING_URL = BASE_URL + "hotel/staff/updateBookingStaff";



    public static final Response.ErrorListener dumdumListener = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            error.printStackTrace();
            System.out.printf("Error: ", error.getMessage());
        }
    };
}
