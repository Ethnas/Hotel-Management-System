package no.nyseth.hmsproject.hms;

import java.io.InputStream;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import javax.annotation.security.DeclareRoles;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import lombok.extern.java.Log;
import no.nyseth.hmsproject.auth.AuthenticationService;
import no.nyseth.hmsproject.auth.User;
import no.nyseth.hmsproject.hms.RoomType;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.jwt.JsonWebToken;
import org.glassfish.jersey.media.multipart.ContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataBodyPart;
import org.glassfish.jersey.media.multipart.FormDataMultiPart;
        
@Path("hotel")
@Stateless
@Log
public class HMService {
    
    @PersistenceContext
    EntityManager em;
    
    @Inject
    AuthenticationService as;
    
    @GET
    @Path("pingtest")
    public Response serviceTest() {
        return Response.ok("sdf").build();
    }
    
    @Inject
    JsonWebToken principal;

    //GETCURRENTUSER!
    private User getCurrentUser() {
        System.out.println("ASDOKASDOKASODKASODKASDK = " + principal.getName());
        return em.find(User.class, principal.getName());
    }

    

    //Booking
        //addBooking
        //@POST
        //@RolesAllowed({Group.USER}) //alle grupper.
        //@Consumes({MediaType.MULTIPART_FORM_DATA, MediaType.APPLICATION_JSON})
        //FormDataParam
        //Romtype, innsjekkingsdato, utsjekkingsdato. (innsjekking og utsjekking genererer totalpris (egen knapp eller funksjon hvor å hente pris?))

    /**
     * 
     * @param bookingRoomType Room type, e.g. small/medium/large etc.
     * @param bookingStartDate //When booking be start
     * @param bookingEndDate //When booking be end
     * @return success
     * 
     * 1. Creates a bookingGuest variable that is set to currentuser.
     * 2. Creates a new booking object and sets properties
     * 3. Persists to db. 
     */
    
    @POST
    @Path("addbooking")
    //@RolesAllowed({Group.USER})
    public Response addBooking(@FormParam("bookingRoomType") String bookingRoomType, 
                @FormParam("bookingStartDate") String bookingStartDate, 
                @FormParam("bookingEndDate") String bookingEndDate) {
        log.log(Level.INFO, "attempting to add booking");
        User booker = this.getCurrentUser();
        Booking bookingtbb = new Booking(); 
        
        RoomType bookingType = new RoomType();
        bookingType.setRoomType(bookingRoomType);
        bookingtbb.setRoomType(bookingType);
        
        try {
            log.log(Level.INFO, "attempting to add date");
            Date dateStart = dateParser(bookingStartDate);
            Date dateEnd = dateParser(bookingEndDate);
            bookingtbb.setBookingStartDate(dateStart);
            bookingtbb.setBookingEndDate(dateEnd);
            bookingtbb.setUsername(booker);
                
        } catch(ParseException e) {
            //TODO 
            //LÆGG INN NOE
        }
            
        log.log(Level.INFO, "added apps");
        em.persist(bookingtbb);
            //IMAGE SHIT
        return Response.ok().build();
    }
    
    
    public Date dateParser(String bookingDate) throws ParseException {
        
        Date date = new SimpleDateFormat("yyyy-MM--dd").parse(bookingDate);
        
        return date;
    }

    /**
     * 
     * @param bookingid ID that you want to return
     * @return OK if bookingid exists and user is the one that booked it.
     * 
     * Class for deleting bookings.
     * 1. Checks if the booking exists. If not -> Fails.
     * 2. Checks if user attempting deletion is same as the one who booked it. If not -> Fails.
     * 3. Deletes.
     * 
     */
    @DELETE
    @Path("removebooking")
    //@RolesAllowed({Group.USER})
    public Response removeBooking(@QueryParam("bookingid") int bookingid) {
        log.log(Level.INFO, "checking for booking -1", bookingid);
        Booking bookingtbd = em.find(Booking.class, bookingid);
        if (bookingtbd != null) {
            log.log(Level.INFO, "checking if existing", bookingid);
            User bookingDeleter = this.getCurrentUser();
            
            if (bookingtbd.getUsername().equals(bookingDeleter)) {
                log.log(Level.INFO, "user verified, moving onto deletion", bookingid);
                em.remove(bookingtbd);
                return Response.ok().build();
            }
        }
        
        log.log(Level.INFO, "booking doesnt exist!");
        return Response.notModified().build();
        
    }
    
        //updateBooking
            //@PUT
            //@RolesAllowed({Group.USER}) //alle grupper, men kun den som la inn booking (eller ansatt?)
    
    /**
     * 
     * Update user for guests. Separate one for staff.
     * 
     * @param bookingid 
     * @param bookingRoomType
     * @param bookingStartDate
     * @param bookingEndDate
     * @return 
     * 
     * 1. Checks if booking exists. If not -> Fails
     * 2. Checks if currentuser is same as booker. If not -> Fails
     * 3. Attempts to update
     */
    
    
    @PUT
    @Path("updatebooking")
    //@RolesAllowed({Group.USER})
    public Response updateBooking(@FormParam("bookingid") int bookingid, 
            @FormParam("bookingRoomType") String bookingRoomType, 
            @FormParam("bookingStartDate") String bookingStartDate,
            @FormParam("bookingEndDate") String bookingEndDate) {
        
        log.log(Level.INFO, "checking if booking exists", bookingid);
        Booking bookingtbu = em.find(Booking.class, bookingid);
              
        if (bookingtbu != null) {
            log.log(Level.INFO, "Booking exists, moving to checking if correct user");
            User bookingUpdater = this.getCurrentUser();
            
             if (bookingtbu.getUsername().equals(bookingUpdater)) {
                 
                RoomType bookingType = new RoomType();
                bookingType.setRoomType(bookingRoomType);
                bookingtbu.setRoomType(bookingType);

                try {
                    Date dateStart = dateParser(bookingStartDate);
                    Date dateEnd = dateParser(bookingEndDate);
                    bookingtbu.setBookingStartDate(dateStart);
                    bookingtbu.setBookingEndDate(dateEnd);

                } catch(ParseException e) {
                    //TODO 
                    //LÆGG INN NOE
                }
                 return Response.ok().build();
                 
             } else {
                log.log(Level.INFO, "user not verified, cancelling");
                return Response.status(Response.Status.BAD_REQUEST).build();
            }
            
        } 
        
        log.log(Level.INFO, "booking doesnt exist!");
        return Response.notModified().build();
    }
    
    
    @PUT
    @Path("staff/acceptBooking")
    //@RolesAllowed({Group.STAFF})
    public Response staffBookingAccept(@QueryParam("bookingid") int bookingid, 
            @QueryParam("bookingStatus") Boolean bookingStatus, 
            @Context SecurityContext sc) {
        log.log(Level.INFO, "attempting to check if user OK", bookingid);
        log.log(Level.INFO, "not verified as staff");
        return Response.ok().build();
    }
    
        //getAllBookings(all)
            //@GET
            //Path("whatever")
            //@RolesAllowed({Group.STAFF]})//Kun ansatte
            //Produces(MediaType.APPLICATION_JSON)
            /* 
            public List<Booking> getBookings() {
                return em.createNativeQuery("SELECT * FROM Bookings", Booking.class).getResultList();
            }
            */
    /**
     * 
     * @return list of all bookings
     */
    @GET
    @Path("getAllBookings")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Booking> getAllBookings() {
        log.log(Level.INFO, "attempting to retrieve all bookings");
        return em.createNativeQuery("SELECT * FROM Booking", Booking.class).getResultList();
    }
    
    //getBooking(specific)
    /**
     * 
     * @param bookingid bookingid of booking to retrieve
     * @return info about booking if found
     */
    @GET
    @Path("getbooking")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getBooking(@QueryParam("bookingid") int bookingid) {
        log.log(Level.INFO, "attempting to retrieve booking", bookingid);
        Booking booking = em.find(Booking.class, bookingid);
        return Response.ok(booking).build();
    }
    
    //
    //DamageReport
    //
    //addDamageReport - POST, kun ansatt gruppe
    /**
     * 
     * @param damageDesc description of damage
     * @param bookingid id of said damage
     * @return 
     * 
     * 1. checks if user is staff
     * 2. if user is staff it creates a new damagereport object
     * 3. creates an object of the booking in question
     * 4. sets the properties of the damagereport object accordingly.
     * 5. success?
     */
    @POST
    @Path("addDamageReport")
    public Response addDamageReport(@FormParam("damageTitle") String damageTitle,
            @FormParam("damageDesc") String damageDesc, @FormParam("bookingid") int bookingid) {
        User reportAdder = this.getCurrentUser();
        log.log(Level.INFO, "checking if user is staff");
        log.log(Level.INFO, "user verified as staff, attempting to add report");
        DamageReport damageReport = new DamageReport();
        Booking booking = new Booking();
        booking.setBookingId(bookingid);

        damageReport.setDamageTitle(damageTitle);
        damageReport.setDamageDescription(damageDesc);
        damageReport.setBookingid(booking);
        //Photo things
        return Response.ok().build(); 
    }
    
    //removeDamageReport - DELETE, kun ansatt gruppe
    @DELETE
    @Path("removeDamageReport")
    public Response removeDamageReport(@QueryParam("reportId") int reportId) {
        User reportRemover = this.getCurrentUser();
        log.log(Level.INFO, "checking if user is staff");
                
       
        log.log(Level.INFO, "user verified, deleting", reportId);
        em.remove(reportId);

        return Response.ok().build();
        
    }

    //updateDamageReport - PUT, kun ansatt gruppe
    @PUT
    @Path("updateDamageReport")
    public Response updateDamageReport(@FormParam("reportId") int reportId,
            @FormParam("damageTitle") String damageTitle,
            @FormParam("damageDesc") String damageDesc, 
            @FormParam("bookingid") int bookingid) {
        User reportAdder = this.getCurrentUser();
        log.log(Level.INFO, "user verified as staff, attempting to find report");

        DamageReport damageReport = em.find(DamageReport.class, reportId);
        if (damageReport != null) {
            log.log(Level.INFO, "report found, attempting update");
            Booking booking = new Booking();
            booking.setBookingId(bookingid);

            damageReport.setDamageTitle(damageTitle);
            damageReport.setDamageDescription(damageDesc);
            damageReport.setBookingid(booking);

            em.merge(damageReport);
            return Response.ok().build();

        } else {
            log.log(Level.INFO, "report not found, rejecting");
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
    }
        
        
    //getdamagereports
    @GET
    @Path("getDamageReports")
    @Produces(MediaType.APPLICATION_JSON)
    public List<DamageReport> getAllDamageReports() {
        log.log(Level.INFO, "attempting to retrieve all reports");
        return em.createNativeQuery("SELECT * FROM DamageReport", DamageReport.class).getResultList();
    }
    //addImage etc.

    //Room
    //addRoomType - POST, staff
        //romtype (small, medium, large etc.), pris per dag.
    @POST
    @Path("addRoomType")
    public Response addRoomType(@FormParam("roomtype") String roomtype, @FormParam("roomPrice") int RoomPrice) {
        User roomTypeAdder = this.getCurrentUser();
        log.log(Level.INFO, "checking if user is staff");

        log.log(Level.INFO, "user verified, attempting to add roomtype");

        RoomType roomType = new RoomType();
        roomType.setRoomType(roomtype);
        roomType.setRoomPrice(RoomPrice);

        em.persist(roomType);
        return Response.ok().build();
        
    }
    
    //removeRoomType - DELETE, staff
    @DELETE
    @Path("removeRoomType")
    public Response removeRoomType(@QueryParam("roomType") String roomType) {
        User roomTypeRemover = this.getCurrentUser();
        
        log.log(Level.INFO, "user verified, checking if thing exists");
        em.remove(roomType);
        return Response.ok().build();
    }
    
    //updateRoomType - PUT, staff, really only to change price lol
    @PUT
    @Path("updateRoomType")
    public Response updateRoomType(@FormParam("roomType") String roomType, @FormParam("roomPrice") int roomPrice) {
        User roomTypeUpdater = this.getCurrentUser();
        
        log.log(Level.INFO, "user verified as staff, attempting to find roomtype");
        RoomType roomTypeUpdate = em.find(RoomType.class, roomType);

        if (roomTypeUpdate != null) {
            log.log(Level.INFO, "roomtype found, updating");
            roomTypeUpdate.setRoomPrice(roomPrice);

            em.merge(roomTypeUpdate);
            return Response.ok().build();
        } else {
            log.log(Level.INFO, "roomtype not found, rejecting");
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
            
    }
    //addImage. lol
    
    //addRoom - POST, staff
    
    //getrooms - romoversikt - samme som alle andre *GET*
    @GET
    @Path("getAllRooms")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Room> getAllRooms() {
        log.log(Level.INFO, "attempting to retrieve all rooms");
        return em.createNativeQuery("SELECT * FROM Room", Room.class).getResultList();
    }

}