package no.nyseth.hmsproject.hms;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.imageio.ImageIO;
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
import org.apache.commons.io.IOUtils;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.jwt.JsonWebToken;
import org.glassfish.jersey.media.multipart.FormDataBodyPart;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
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

    
    private User getCurrentUser() {
        System.out.println("ASDOKASDOKASODKASODKASDK = " + principal.getName());
        return em.find(User.class, principal.getName());
    }
    

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
    @RolesAllowed({Group.USER, Group.STAFF})
    public Response addBooking(@FormParam("bookingRoomType") String bookingRoomType, 
                @FormParam("bookingStartDate") String bookingStartDate, 
                @FormParam("bookingEndDate") String bookingEndDate) {
        log.log(Level.INFO, "attempting to add booking");
        User booker = this.getCurrentUser();
        Booking bookingtbb = new Booking(); 
        
        RoomType bookingType = em.find(RoomType.class, bookingRoomType);
        bookingtbb.setRoomTypes(bookingType);
        
        LocalDate dateStart = LocalDate.now();
        LocalDate dateEnd = LocalDate.now();
        ResponseBuilder resp;
        
        try {
            log.log(Level.INFO, "attempting to add date");
            dateStart = dateParser(bookingStartDate);
            dateEnd = dateParser(bookingEndDate);
            
                
        } catch(ParseException e) {
            log.log(Level.INFO, "wrong when adding date");
            e.printStackTrace();
        }
        
        if (checkDatePast(dateStart) || checkDatePast(dateEnd)) {
            resp = Response.status(Response.Status.NOT_ACCEPTABLE);
        } else {
            bookingtbb.setBookingStartDate(dateStart);
            bookingtbb.setBookingEndDate(dateEnd);
            bookingtbb.setUser(booker);

            //default values
            bookingtbb.setBookingAccepted("false");
            bookingtbb.setBookingStatus("inactive");
            
            Room defaultRoom = em.find(Room.class, 0);
            bookingtbb.setRoom(defaultRoom);

            log.log(Level.INFO, "added apps");
            em.persist(bookingtbb);

            List bookingIdList = em.createNativeQuery("SELECT d.bookingid FROM Booking d WHERE d.username = " + "'" + booker.getUsername() + "'" 
                    + " AND d.bookingstartdate = " + "'" + dateStart + "'" + " AND d.bookingenddate = " + "'" + dateEnd + "'").getResultList();
            int i = (int) bookingIdList.get(bookingIdList.size()-1);
            MailService mail = new MailService();
            mail.sendMail(booker.getEmail(), "Booking recieved: " + i + " recieved", "We have received your booking with id " + i 
                    + ". You will receive a notification when your booking has been updated.");
            resp = Response.ok();
        }
        return resp.build();
    }
    
    
    public LocalDate dateParser(String bookingDate) throws ParseException {
        
        LocalDate date = LocalDate.parse(bookingDate/*, DateTimeFormatter.BASIC_ISO_DATE*/);
        
        return date;
    }
    
    private boolean checkDatePast(LocalDate date) {
        return date.isBefore(LocalDate.now());
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
    @RolesAllowed({Group.USER, Group.STAFF})
    public Response removeBooking(@QueryParam("bookingid") int bookingid) {
        log.log(Level.INFO, "checking for booking -1", bookingid);
        Booking bookingtbd = em.find(Booking.class, bookingid);
        if (bookingtbd != null) {
            log.log(Level.INFO, "checking if existing", bookingid);
            User bookingDeleter = this.getCurrentUser();
            
            if (bookingtbd.getUser().equals(bookingDeleter)) {
                log.log(Level.INFO, "user verified, moving onto deletion", bookingid);
                //em.remove(bookingtbd);
                
                bookingtbd.setBookingStatus("cancelled");
                
                MailService mail = new MailService();
                mail.sendMail(bookingtbd.getUser().getEmail(), "Booking: " + bookingid + " deleted", 
                        "Hi, your booking with id " + bookingid + " has been deleted from our systems.");
                return Response.ok().build();
            }
        }
        
        log.log(Level.INFO, "booking doesnt exist!");
        return Response.notModified().build();
        
    }
    
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
    @RolesAllowed({Group.USER, Group.STAFF})
    public Response updateBooking(@FormParam("bookingid") int bookingid, 
            @FormParam("bookingRoomType") String bookingRoomType, 
            @FormParam("bookingStartDate") String bookingStartDate,
            @FormParam("bookingEndDate") String bookingEndDate) {
        
        RoomType bookingType = em.find(RoomType.class, bookingRoomType);
        log.log(Level.INFO, "checking if booking exists", bookingid);
        Booking bookingtbu = em.find(Booking.class, bookingid);
        
        RoomType rt = bookingtbu.getRoomTypes();
        String sDate = bookingtbu.getBookingStartDate().toString();
        String eDate = bookingtbu.getBookingEndDate().toString();
        ResponseBuilder resp;
              
        if (bookingtbu != null) {
            log.log(Level.INFO, "Booking exists, moving to checking if correct user");
            
            User bookingUpdater = this.getCurrentUser();
            
             if (bookingtbu.getUser().equals(bookingUpdater)) {
                //em.getTransaction().begin();
                
                bookingtbu.setRoomTypes(bookingType);
                
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
                
                if (checkDatePast(dateStart) || checkDatePast(dateEnd)) {
                    resp = Response.status(Response.Status.NOT_ACCEPTABLE);
                }
                bookingtbu.setBookingStartDate(dateStart);
                bookingtbu.setBookingEndDate(dateEnd);
                //em.getTransaction().commit();
                em.merge(bookingtbu);
                
                MailService mail = new MailService();
                mail.sendMail(bookingUpdater.getEmail(), "Booking: " + bookingid + " updated", 
                        "Hi, your booking with id " + bookingid + " has been updated in our systems." +
                                "\nThese changes were made:" +
                                "\n Roomtype: " + rt + " changed to " + bookingType +
                                "\n Start date: " + sDate + " changed to " + dateStart.toString() +
                                "\n End date: " + eDate + "changed to" + dateEnd.toString());
                
                resp = Response.ok();
                 
             } else {
                log.log(Level.INFO, "user not verified, cancelling");
                resp = Response.status(Response.Status.BAD_REQUEST);
            }
            
        } 
            else {
                log.log(Level.INFO, "booking doesnt exist!");
                resp = Response.notModified();
        }
        return resp.build();
    }
    
    
    @PUT
    @Path("staff/updateBookingStaff")
    @RolesAllowed({Group.STAFF})
    public Response staffBookingAccept(@QueryParam("bookingid") int bookingid, 
            @QueryParam("bookingAccepted") String bookingAccepted, @QueryParam("RoomNumber") int RoomNumber,
            @Context SecurityContext sc) {
        
        log.log(Level.INFO, "checking if booking exists");
        Booking bookingtba = em.find(Booking.class, bookingid);
        ResponseBuilder resp;
        
        if (bookingtba != null) {
            log.log(Level.INFO, "booking exists, moving onto update");
            
            bookingtba.setBookingAccepted(bookingAccepted);
            
            Room roomN = em.find(Room.class, RoomNumber);
            bookingtba.setRoom(roomN);
            
            MailService mail = new MailService();
            mail.sendMail(bookingtba.getUser().getEmail(), "Booking accepted:" + bookingtba.getBookingId(), 
                    "Your booking " + bookingtba.getBookingId() + "has been accepted. We are excited for your stay with us.");
            resp = Response.ok();
            
        } else {
            resp = Response.notModified();
        }

        log.log(Level.INFO, "attempting to check if user OK", bookingid);
        return resp.build();
    }
    
    @PUT
    @Path("staff/checkin")
    @RolesAllowed(Group.STAFF)
    public Response checkIn(@QueryParam("bookingid") int bookingid) {
        Booking booking = em.find(Booking.class, bookingid);
        ResponseBuilder resp;
        if (booking != null) {
            booking.setBookingStatus("Active");
            Room room = booking.getRoom();
            room.setRoomStatus("occupied");
            resp = Response.ok();
        } else {
            resp = Response.notModified();
        }
        return resp.build();
    }
    
    @PUT
    @Path("staff/checkout")
    @RolesAllowed(Group.STAFF)
    public Response checkOut(@QueryParam("bookingid") int bookingid) {
        Booking booking = em.find(Booking.class, bookingid);
        ResponseBuilder resp;
        if (booking != null) {
            booking.setBookingStatus("Inactive");
            Room room = booking.getRoom();
            room.setRoomStatus("maintenance");
            resp = Response.ok();
        } else {
            resp = Response.notModified();
        }
        return resp.build();
    }
    
    @PUT
    @Path("staff/updateroomstatus")
    @RolesAllowed(Group.STAFF)
    public Response updateRoomStatus(@QueryParam("roomnumber") int roomnumber, @QueryParam("roomstatus") String roomstatus) {
        Room room = em.find(Room.class, roomnumber);
        ResponseBuilder resp;
        if (room != null) {
            room.setRoomStatus(roomstatus);
            resp = Response.ok();
        } else {
            resp = Response.notModified();
        }
        return resp.build();
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
        damageReport.setBooking(booking);
        
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
        
        DamageReport reporttbd = em.find(DamageReport.class, reportId);
        ResponseBuilder resp;
        
        if (reporttbd != null) {
            log.log(Level.INFO, "found");
            em.remove(reporttbd);
            resp = Response.ok();
        } else {
            log.log(Level.INFO, "not found");
            resp = Response.notModified();
        }

        return resp.build();
        
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
            damageReport.setBooking(booking);

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
    
    /**
     * 
     * @param reportid report id of damage report to retrieve
     * @return info about damage report if found
     */
    @GET
    @Path("getspecificreport")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getDamageReport(@QueryParam("reportid") int reportid) {
        log.log(Level.INFO, "attempting to retrieve damage report", reportid);
        DamageReport damageReport = em.find(DamageReport.class, reportid);
        return Response.ok(damageReport).build();
    }

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
        roomType.setType(roomtype);
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
        
        
        RoomType roomtypetbd = em.find(RoomType.class, roomType);
        ResponseBuilder resp;
        
        if (roomtypetbd != null) {
            log.log(Level.INFO, "found");
            em.remove(roomtypetbd);
            resp = Response.ok();
        } else {
            log.log(Level.INFO, "not found");
            resp = Response.notModified();
        }

        return resp.build();
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
        //saveImage();
        return resp.build();
    }
    
    private ResponseBuilder parseImage(FormDataMultiPart images) {
        ResponseBuilder resp;
        try {
            System.out.println("checking for photo");
            List<FormDataBodyPart> itemImages = images.getFields("images");
            if (itemImages != null) {
                for (FormDataBodyPart imageParts : itemImages) {
                    InputStream is = imageParts.getValueAs(InputStream.class);
                    FormDataContentDisposition meta = imageParts.getFormDataContentDisposition();
                    System.out.println("imagePrts filename: " + meta.getFileName());
                    byte[] imageBytes = IOUtils.toByteArray(is);
                    System.out.println("test");
                    
                    DamageImage itemImg = new DamageImage();
                    itemImg.setImage(imageBytes);
                    itemImg.setMimeType(meta.getType());
                    itemImg.setFilesize(imageBytes.length);
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
    
    private void saveImage() {
        DamageImage image = em.find(DamageImage.class, 552);
        byte[] data = image.getImage();
      try{
      ByteArrayInputStream bis = new ByteArrayInputStream(data);
      BufferedImage bImage2 = ImageIO.read(bis);
      ImageIO.write(bImage2, "png", new File("C:\\Users\\Erlend\\Desktop\\output.png") );
      System.out.println("image created");
      }catch(Exception e) {
          
      }
    }
    
    @GET
    @Path("getdamageimages")
    @RolesAllowed(Group.STAFF)
    @Produces(MediaType.APPLICATION_JSON)
    public List<DamageImage> getDamageImages(@QueryParam("reportid") int reportid) {
        return em.createNativeQuery("SELECT * FROM DamageImages WHERE reportid = " +
                "'" + reportid + "'", DamageImage.class).getResultList();
    }
    
    @POST
    @Path("contactus")
    public Response contactUs(@FormParam("contecterName") String contacterName, @FormParam("contacterMail") String contacterMail, 
            @FormParam("contacterSubject") String contacterSubject, @FormParam("contacterMessage") String contacterMessage) {
        
        MailService mail = new MailService();
            mail.sendMail("kakerull214056@gmail.com", 
                    "New message recieved from: " + contacterName + " regarding: " + contacterSubject, 
                    "A user has sent an email\n" 
                            + "Name: " + contacterName 
                            + "\nEmail: " + contacterMail
                            + "\nSubject: " + contacterSubject
                            + "\nMessage: " + contacterMessage);
            
        return Response.ok().build();
    }

}