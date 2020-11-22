package no.nyseth.hmsproject.hms;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import no.nyseth.hmsproject.hms.Booking;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2020-11-15T15:28:44")
@StaticMetamodel(DamageReport.class)
public class DamageReport_ { 

    public static volatile SingularAttribute<DamageReport, Integer> reportId;
    public static volatile SingularAttribute<DamageReport, String> DamageTitle;
    public static volatile SingularAttribute<DamageReport, Booking> bookingid;
    public static volatile SingularAttribute<DamageReport, String> damageDescription;

}