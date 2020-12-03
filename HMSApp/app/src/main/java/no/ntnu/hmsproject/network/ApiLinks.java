package no.ntnu.hmsproject.network;

import com.android.volley.Response;
import com.android.volley.VolleyError;

public class ApiLinks {

    // OBS!
    // LINKS ARE CASE SENSITIVE FOR SOME REASON, BE SURE TO CHECK THE CAPITALIZING.
    // ALSO, CHECK THAT THE IP HERE IS CORRECT...
    //


    //server ip

    public static final String BASE_URL = "https://hms.northeurope.cloudapp.azure.com/HMSProject-1/api/";
    //public static final String BASE_URL = "http://10.22.193.136:8080/HMSProject/api/";

    public static final String LOGIN_URL = BASE_URL + "auth/login";
    public static final String CREATE_URL = BASE_URL + "auth/create";
    public static final String CURRENT_USER_URL = BASE_URL + "auth/currentuser";
    public static final String LOGIN_GOOGLE_URL = BASE_URL + "auth/logingoogle";


    //-------------------Service
    public static final String CONTACT_US_URL = BASE_URL + "hotel/contactus";
    //Booking
    public static final String ADD_BOOKING_URL = BASE_URL + "hotel/addbooking";
    public static final String REMOVE_BOOKING_URL = BASE_URL + "hotel/removebooking";
    public static final String UPDATE_BOOKING_URL = BASE_URL + "hotel/updatebooking";
    public static final String GET_SPECIFIC_BOOKING_URL = BASE_URL + "hotel/getbooking";
    public static final String GET_ALL_BOOKING_URL = BASE_URL + "hotel/getAllBookings";


    //Damage Report
    public static final String GET_ALL_DAMAGE_REPORT_URL = BASE_URL + "hotel/getDamageReports";
    public static final String ADD_DAMAGE_REPORT_URL = BASE_URL + "hotel/addDamageReport";
    public static final String REMOVE_DAMAGE_REPORT_URL = BASE_URL + "hotel/removeDamageReport";
    public static final String UPDATE_DAMAGE_REPORT_URL = BASE_URL + "hotel/updateDamageReport";
    public static final String GET_SPECIFIC_DAMAGE_REPORT = BASE_URL + "hotel/getspecificreport";
    public static final String GET_DAMAGE_REPORT_IMAGES = BASE_URL + "hotel/getdamageimages";

    //Room Type
    public static final String ADD_ROOMTYPE_URL = BASE_URL + "hotel/addRoomType";
    public static final String REMOVE_ROOMTYPE_URL = BASE_URL + "hotel/removeRoomType";
    public static final String UPDATE_ROOMTYPE_URL = BASE_URL + "hotel/updateRoomType";

    //Staff
    public static final String ACCEPT_BOOKING_URL = BASE_URL + "hotel/staff/updateBookingStaff";
    public static final String CHECK_IN_URL = BASE_URL + "hotel/staff/checkin";
    public static final String CHECK_OUT_URL = BASE_URL + "hotel/staff/checkout";
    public static final String UPDATE_ROOM_STATUS_URL = BASE_URL + "hotel/staff/updateroomstatus";



    public static final Response.ErrorListener dumdumListener = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            error.printStackTrace();
            System.out.printf("Error: ", error.getMessage());
        }
    };
}
