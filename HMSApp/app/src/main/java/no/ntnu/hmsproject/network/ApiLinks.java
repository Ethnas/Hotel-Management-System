package no.ntnu.hmsproject.network;

import com.android.volley.Response;
import com.android.volley.VolleyError;

public class ApiLinks {

    // OBS!
    // LINKS ARE CASE SENSITIVE FOR SOME REASON, BE SURE TO CHECK THE CAPITALIZING.
    // ALSO, CHECK THAT THE IP HERE IS CORRECT...
    //


    //skole ip
    public static final String BASE_URL = "https://hms.northeurope.cloudapp.azure.com/HMSProject-1/api/";

    public static final String LOGIN_URL = BASE_URL + "auth/login";
    public static final String CREATE_URL = BASE_URL + "auth/create";
    public static final String CURRENT_USER_URL = BASE_URL + "auth/currentuser";


    //-------------------Service
    //Booking
    public static final String ADD_BOOKING_URL = BASE_URL + "hotel/addbooking";
    public static final String REMOVE_BOOKING_URL = BASE_URL + "hotel/removebooking";
    public static final String UPDATE_BOOKING_URL = BASE_URL + "hotel/updatebooking";

    //Damage Report
    public static final String GET_ALL_DAMAGE_REPORT_URL = BASE_URL + "hotel/getDamageReports";
    public static final String ADD_DAMAGE_REPORT_URL = BASE_URL + "hotel/addDamageReport";
    public static final String REMOVE_DAMAGE_REPORT_URL = BASE_URL + "hotel/removeDamageReport";
    public static final String UPDATE_DAMAGE_REPORT_URL = BASE_URL + "hotle/updateDamageReport";

    //Room Type
    public static final String ADD_ROOMTYPE_URL = BASE_URL + "hotel/addRoomType";
    public static final String REMOVE_ROOMTYPE_URL = BASE_URL + "hotel/removeRoomType";
    public static final String UPDATE_ROOMTYPE_URL = BASE_URL + "hotel/updateRoomType";

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
