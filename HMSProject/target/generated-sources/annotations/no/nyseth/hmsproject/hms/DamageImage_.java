package no.nyseth.hmsproject.hms;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import no.nyseth.hmsproject.hms.DamageReport;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2020-11-29T13:28:03")
@StaticMetamodel(DamageImage.class)
public class DamageImage_ { 

    public static volatile SingularAttribute<DamageImage, byte[]> image;
    public static volatile SingularAttribute<DamageImage, Integer> imageId;
    public static volatile SingularAttribute<DamageImage, DamageReport> report;
    public static volatile SingularAttribute<DamageImage, String> mimeType;
    public static volatile SingularAttribute<DamageImage, Long> filesize;

}