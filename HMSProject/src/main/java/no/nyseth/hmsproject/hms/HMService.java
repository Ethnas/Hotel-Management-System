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
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
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

    @POST
    @Path("addbooking")
    @RolesAllowed({Group.USER})
    @Consumes({MediaType.MULTIPART_FORM_DATA, MediaType.APPLICATION_JSON})
    public Response addBooking(@FormDataParam("bookingRoomType") String bookingRoomType, 
                @FormDataParam("bookingStartDate") Date bookingStartDate, 
                @FormDataParam("bookingEndDate") Date bookingEndDate) {
        log.log(Level.INFO, "attempting to add booking");
        User bookingGuest = this.getCurrentUser();
        Booking bookingtbb = new Booking();
        //bookingtbb.setBookingRoomType(bookingRoomType);
        bookingtbb.setBookingStartDate(bookingStartDate);
        bookingtbb.setBookingEndDate(bookingEndDate);
        bookingtbb.setBookingGuest(bookingGuest);

        
        em.persist(bookingtbb);
            //IMAGE SHIT
        return Response.ok().build();
    }


        //removeBooking 
            //@DELETE
            //@RolesAllowed({Group.USER}) // alle grupper, men kun den som la inn booking (eller ansatt?)

        //updateBooking
            //@PUT
            //@RolesAllowed({Group.USER}) //alle grupper, men kun den som la inn booking (eller ansatt?)
    
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