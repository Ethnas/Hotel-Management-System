package no.nyseth.hmsproject.hms;


import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
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
@Entity //(name = "Booking")
@Table(name = "Booking")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Booking implements Serializable {
    
    //@Id
    //@GeneratedValue(strategy = GenerationType.AUTO) //Nødvendig her?
    //private Long id;
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int bookingId;
    
    public LocalDate bookingStartDate;
    public LocalDate bookingEndDate;
    public String bookingAccepted; //String or boolean?
    public String bookingStatus;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="UserName")
    public User user;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="RoomType")
    public RoomType roomType;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="RoomNumber")
    public Room room;
    
}