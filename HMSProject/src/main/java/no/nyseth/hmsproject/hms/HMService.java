package no.nyseth.hmsproject.hms;

import java.io.InputStream;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;
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
import no.nyseth.hmsproject.auth.Group;
import no.nyseth.hmsproject.auth.User;
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
    @RolesAllowed({Group.USER})
    @Consumes({MediaType.MULTIPART_FORM_DATA, MediaType.APPLICATION_JSON})
    public Response addBooking(@FormDataParam("bookingRoomType") Room bookingRoomType, 
                @FormDataParam("bookingStartDate") Date bookingStartDate, 
                @FormDataParam("bookingEndDate") Date bookingEndDate) {
        log.log(Level.INFO, "attempting to add booking");
        User bookingGuest = this.getCurrentUser();
        Booking bookingtbb = new Booking(); 
        bookingtbb.setRoomType(bookingRoomType);
        bookingtbb.setBookingStartDate(bookingStartDate);
        bookingtbb.setBookingEndDate(bookingEndDate);
        bookingtbb.setBookingGuest(bookingGuest);

        
        em.persist(bookingtbb);
            //IMAGE SHIT
        return Response.ok().build();
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
    @RolesAllowed({Group.USER})
    public Response removeBooking(@QueryParam("bookingid") Long bookingid) {
        Booking bookingtbd = em.find(Booking.class, bookingid);
        if (bookingtbd != null) {
            log.log(Level.INFO, "checking if existing", bookingid);
            User bookingDeleter = this.getCurrentUser();
            if (bookingtbd.getBookingGuest().equals(bookingDeleter)) {
                log.log(Level.INFO, "user verified, moving onto deletion", bookingid);
                em.remove(this);
                return Response.ok.build();
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
    @RolesAllowed({Group.USER})
    public Response updateBooking(@FormDataParam("bookingid") Long bookingid, 
            @FormDataParam("bookingRoomType") Room bookingRoomType, 
            @FormDataParam("bookingStartDate") Date bookingStartDate,
            @FormDataParam("bookingEndDate") Date bookingEndDate) {
        
        log.log(Level.INFO, "checking if booking exists", bookingid);
        Booking bookingtbu = em.find(Booking.class, bookingid);
              
        if (bookingtbu != null) {
            log.log(Level.INFO, "Booking exists, moving to checking if correct user");
            User bookingUpdater = this.getCurrentUser();
            
             if (bookingtbu.getBookingGuest().equals(bookingUpdater)) {
                 bookingtbu.setRoomType(bookingRoomType);
                 bookingtbu.setBookingStartDate(bookingStartDate);
                 bookingtbu.setBookingEndDate(bookingEndDate);
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
    @RolesAllowed({Group.STAFF})
    public Response staffBookingAccept(@QueryParam("bookingid") Long bookingid, 
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
        //getBooking(specific)

    //DamageReport
        //addDamageReport - POST, kun ansatt gruppe
        //removeDamageReport - DELETE, kun ansatt gruppe
        //updateDamageReport - PUT, kun ansatt gruppe
        //addImage etc.

    //Room
        //addRoomType
            //romtype (small, medium, large etc.), pris per dag.
        //removeRoomType
        //updateRoomType
        //addImage.
    

}