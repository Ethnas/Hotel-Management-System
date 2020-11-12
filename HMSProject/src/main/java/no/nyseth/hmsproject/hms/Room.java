/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package no.nyseth.hmsproject.hms;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
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


/**
 *
 * @author nyseth
 */
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Room implements Serializable {
    
    
    //@GeneratedValue(strategy = GenerationType.AUTO) //Nødvendig her?
    //private Long id;
    
    @Id
    private int roomNumber;
    private String roomStatus;
    private RoomType roomType;
    
    //@OneToMany
    //private List<RoomImages> roomImage;
    
    
}