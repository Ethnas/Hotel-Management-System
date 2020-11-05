package no.nyseth.hmsproject.hms;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import no.nyseth.hmsproject.auth.User;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2020-11-05T10:13:01")
@StaticMetamodel(Booking.class)
public class Booking_ { 

    public static volatile SingularAttribute<Booking, Date> bookingStartDate;
    public static volatile SingularAttribute<Booking, String> bookingAccepted;
    public static volatile SingularAttribute<Booking, User> bookingGuest;
    public static volatile SingularAttribute<Booking, Date> bookingEndDate;
    public static volatile SingularAttribute<Booking, String> bookingStatus;
    public static volatile SingularAttribute<Booking, Integer> bookingId;

}