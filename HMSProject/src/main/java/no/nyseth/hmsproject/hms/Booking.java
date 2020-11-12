package no.nyseth.hmsproject.hms;


import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import no.nyseth.hmsproject.auth.User;
import no.nyseth.hmsproject.hms.Room;
import no.nyseth.hmsproject.hms.RoomType;

/**
 *
 * @author nyseth
 */
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Booking implements Serializable {
    
    //@Id
    //@GeneratedValue(strategy = GenerationType.AUTO) //Nødvendig her?
    //private Long id;
    
    @Id
    private int bookingId;
    
    public Date bookingStartDate;
    public Date bookingEndDate;
    public String bookingAccepted; //String or boolean?
    public String bookingStatus;
    
    public User bookingGuest;
    
    public RoomType roomType;
    
    public Room room;
    
}