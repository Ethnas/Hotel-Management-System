package no.nyseth.hmsproject.hms;

import java.time.LocalDate;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import no.nyseth.hmsproject.auth.User;
import no.nyseth.hmsproject.hms.Room;
import no.nyseth.hmsproject.hms.RoomType;

<<<<<<< Updated upstream
<<<<<<< Updated upstream
@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2020-11-26T11:50:32")
=======
@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2020-11-29T13:28:03")
>>>>>>> Stashed changes
=======
@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2020-11-29T13:01:51")
>>>>>>> Stashed changes
@StaticMetamodel(Booking.class)
public class Booking_ { 

    public static volatile SingularAttribute<Booking, LocalDate> bookingStartDate;
    public static volatile SingularAttribute<Booking, String> bookingAccepted;
    public static volatile SingularAttribute<Booking, LocalDate> bookingEndDate;
    public static volatile SingularAttribute<Booking, String> bookingStatus;
    public static volatile SingularAttribute<Booking, User> user;
    public static volatile SingularAttribute<Booking, Integer> bookingId;
    public static volatile SingularAttribute<Booking, RoomType> roomType;
    public static volatile SingularAttribute<Booking, Room> room;

}