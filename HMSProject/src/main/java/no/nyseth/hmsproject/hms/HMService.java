package no.nyseth.hmsproject.hms;

import java.io.InputStream;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
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
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.SecurityContext;
import lombok.extern.java.Log;
import no.nyseth.hmsproject.auth.AuthenticationService;
import no.nyseth.hmsproject.auth.Group;
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
    
    @Inject
    @ConfigProperty(name = "photo.storage.path", defaultValue = "images")
    String photoPath;

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
    @RolesAllowed({Group.USER})
    public Response addBooking(@FormParam("bookingRoomType") String bookingRoomType, 
                @FormParam("bookingStartDate") String bookingStartDate, 
                @FormParam("bookingEndDate") String bookingEndDate) {
        log.log(Level.INFO, "attempting to add booking");
        User booker = this.getCurrentUser();
        Booking bookingtbb = new Booking(); 
        
        RoomType bookingType = em.find(RoomType.class, bookingRoomType);
        bookingtbb.setRoomType(bookingType);
        
        LocalDate dateStart = LocalDate.now();
        LocalDate dateEnd = LocalDate.now();
        
        try {
            log.log(Level.INFO, "attempting to add date");
            dateStart = dateParser(bookingStartDate);
            dateEnd = dateParser(bookingEndDate);
            
                
        } catch(ParseException e) {
            log.log(Level.INFO, "wrong when adding date");
            e.printStackTrace();
        }
        
        bookingtbb.setBookingStartDate(dateStart);
        bookingtbb.setBookingEndDate(dateEnd);
        bookingtbb.setUser(booker);

        //default values
        bookingtbb.setBookingAccepted("false");
        bookingtbb.setBookingStatus("inactive");
            
        log.log(Level.INFO, "added apps");
        em.persist(bookingtbb);
            //IMAGE SHIT
        return Response.ok().build();
    }
    
    
    public LocalDate dateParser(String bookingDate) throws ParseException {
        
        //Date date = new SimpleDateFormat("yyyy-MM--dd").parse(bookingDate);
        LocalDate date = LocalDate.parse(bookingDate/*, DateTimeFormatter.BASIC_ISO_DATE*/);
        
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
    @RolesAllowed({Group.USER})
    public Response removeBooking(@QueryParam("bookingid") int bookingid) {
        log.log(Level.INFO, "checking for booking -1", bookingid);
        Booking bookingtbd = em.find(Booking.class, bookingid);
        if (bookingtbd != null) {
            log.log(Level.INFO, "checking if existing", bookingid);
            User bookingDeleter = this.getCurrentUser();
            
            if (bookingtbd.getUser().equals(bookingDeleter)) {
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
    @RolesAllowed({Group.USER})
    public Response updateBooking(@FormParam("bookingid") int bookingid, 
            @FormParam("bookingRoomType") String bookingRoomType, 
            @FormParam("bookingStartDate") String bookingStartDate,
            @FormParam("bookingEndDate") String bookingEndDate) {
        
        log.log(Level.INFO, "checking if booking exists", bookingid);
        Booking bookingtbu = em.find(Booking.class, bookingid);
              
        if (bookingtbu != null) {
            log.log(Level.INFO, "Booking exists, moving to checking if correct user");
            User bookingUpdater = this.getCurrentUser();
            
             if (bookingtbu.getUser().equals(bookingUpdater)) {
                 
                RoomType bookingType = em.find(RoomType.class, bookingRoomType);
                bookingtbu.setRoomType(bookingType);;
                
                LocalDate dateStart = LocalDate.now();
                LocalDate dateEnd = LocalDate.now();

                try {
                    log.log(Level.INFO, "attempting to add date");
                    dateStart = dateParser(bookingStartDate);
                    dateEnd = dateParser(bookingEndDate);


                } catch(ParseException e) {
                    log.log(Level.INFO, "wrong when adding date");
                    e.printStackTrace();
                }

                bookingtbu.setBookingStartDate(dateStart);
                bookingtbu.setBookingEndDate(dateEnd);
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
    @Path("staff/updateBookingStaff")
    @RolesAllowed({Group.STAFF})
    public Response staffBookingAccept(@QueryParam("bookingid") int bookingid, @QueryParam("bookingStatus") String bookingStatus, 
            @QueryParam("bookingAccepted") String bookingAccepted, @QueryParam("RoomNumber") int RoomNumber,
            @Context SecurityContext sc) {
        
        log.log(Level.INFO, "checking if booking exists");
        Booking bookingtba = em.find(Booking.class, bookingid);
        
        if (bookingtba != null) {
            log.log(Level.INFO, "booking exists, moving onto update");
            
            bookingtba.setBookingStatus(bookingStatus);
            bookingtba.setBookingAccepted(bookingAccepted);
            
            Room roomN = em.find(Room.class, RoomNumber);
            bookingtba.setRoom(roomN);
            
        }
        
        
        
        
        log.log(Level.INFO, "attempting to check if user OK", bookingid);
        return Response.ok().build();
    }
    
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
    @RolesAllowed({Group.STAFF})
    public Response addDamageReport(@FormParam("damageTitle") String damageTitle,
            @FormParam("damageDesc") String damageDesc, @FormParam("bookingid") int bookingid) {
        User reportAdder = this.getCurrentUser();
        log.log(Level.INFO, "checking if user is staff");
        log.log(Level.INFO, "user verified as staff, attempting to add report");
        DamageReport damageReport = new DamageReport();
        
        Booking booking = em.find(Booking.class, bookingid);

        damageReport.setDamageTitle(damageTitle);
        damageReport.setDamageDescription(damageDesc);
        damageReport.setBookingid(booking);
        
        em.persist(damageReport);
        //Photo things
        return Response.ok().build(); 
    }
    
    //removeDamageReport - DELETE, kun ansatt gruppe
    @DELETE
    @Path("removeDamageReport")
    @RolesAllowed({Group.STAFF})
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
    @RolesAllowed({Group.STAFF})
    public Response updateDamageReport(@FormParam("reportId") int reportId,
            @FormParam("damageTitle") String damageTitle,
            @FormParam("damageDesc") String damageDesc, 
            @FormParam("bookingid") int bookingid) {
        
        User reportAdder = this.getCurrentUser();
        log.log(Level.INFO, "user verified as staff, attempting to find report");
        DamageReport damageReport = em.find(DamageReport.class, reportId);
        
        if (damageReport != null) {
            log.log(Level.INFO, "report found, attempting update");
            
            Booking booking = em.find(Booking.class, bookingid);

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
    @RolesAllowed({Group.STAFF})
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
    @RolesAllowed({Group.STAFF})
    public Response removeRoomType(@QueryParam("roomType") String roomType) {
        User roomTypeRemover = this.getCurrentUser();
        
        log.log(Level.INFO, "user verified, checking if thing exists");
        em.remove(roomType);
        return Response.ok().build();
    }
    
    //updateRoomType - PUT, staff, really only to change price lol
    @PUT
    @Path("updateRoomType")
    @RolesAllowed({Group.STAFF})
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
    
    //Retrieves all roomtypes
    @GET
    @Path("getRoomTypes")
    @Produces(MediaType.APPLICATION_JSON)
    public List<RoomType> getAllRoomTypes() {
        log.log(Level.INFO, "attempting to retrieve all rooms");
        return em.createNativeQuery("SELECT * FROM RoomType", Room.class).getResultList();
    }
    
    
    @POST
    @Path("testimage")
    @RolesAllowed({Group.STAFF})
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response testImage(FormDataMultiPart images) {
        ResponseBuilder resp;
        resp = parseImage(images);
        return resp.build();
    }
    
    private ResponseBuilder parseImage(FormDataMultiPart images) {
        ResponseBuilder resp;
        try {
            Map<String, List<FormDataBodyPart>> fs = images.getFields();
            Set<String> set = fs.keySet();
            Iterator<String> it = set.iterator();
            List<FormDataBodyPart> itemImages = null;
            while (it.hasNext()) {
                String s = it.next();
                itemImages = images.getFields(s);
            }
            System.out.println("checking for photo");
            if (itemImages != null) {
                for (FormDataBodyPart imageParts : itemImages) {
                    InputStream is = imageParts.getEntityAs(InputStream.class);
                    ContentDisposition meta = imageParts.getContentDisposition();
                    //String pid = UUID.randomUUID().toString();
                    byte[] imageBytes = new byte[(int) is.available()];
                    is.read(imageBytes);
                    System.out.println("test");
                    
                    DamageImage itemImg = new DamageImage();
                    //itemImg.setImageId(pid);
                    itemImg.setImage(imageBytes);
                    itemImg.setMimeType(meta.getType());
                    itemImg.setFilesize(meta.getSize());
                    DamageReport dr = em.find(DamageReport.class, 101);
                    itemImg.setReport(dr);
                    System.out.println("Adding photo");
                    em.persist(itemImg);
                }
                
            }
            resp = Response.ok();
        } catch(Exception e) {
            System.out.println("error adding photo");
            e.printStackTrace();
            resp = Response.serverError();
        }
        return resp;
    }
    
    private void printFields(FormDataMultiPart images) {
        Map<String, List<FormDataBodyPart>> fs = images.getFields();
        Set<String> set = fs.keySet();
        Iterator<String> it = set.iterator();
        int i = 0;
        while(it.hasNext()) {
            String s = it.next();
            System.out.println("Field " + i + " : " + s);
            i++;
        }
    }

}