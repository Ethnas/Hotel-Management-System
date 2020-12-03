package no.nyseth.hmsproject.hms;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import no.nyseth.hmsproject.hms.RoomType;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2020-12-03T11:49:17")
@StaticMetamodel(RoomImage.class)
public class RoomImage_ { 

    public static volatile SingularAttribute<RoomImage, byte[]> image;
    public static volatile SingularAttribute<RoomImage, Integer> imageId;
    public static volatile SingularAttribute<RoomImage, String> mimeType;
    public static volatile SingularAttribute<RoomImage, Long> filesize;
    public static volatile SingularAttribute<RoomImage, RoomType> roomType;

}