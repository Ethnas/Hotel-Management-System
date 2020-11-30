package no.nyseth.hmsproject.hms;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import no.nyseth.hmsproject.hms.RoomType;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2020-11-30T15:31:09")
@StaticMetamodel(Room.class)
public class Room_ { 

    public static volatile SingularAttribute<Room, String> roomStatus;
    public static volatile SingularAttribute<Room, Integer> roomNumber;
    public static volatile SingularAttribute<Room, RoomType> roomType;

}