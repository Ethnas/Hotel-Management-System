package no.nyseth.hmsproject.hms;
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
//package ..../hms

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
//import no.nyseth.hmsproject.auth.User;

/**
 *
 * @author nyseth
 */
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DamageReport implements Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO) //Nødvendig her?
    private Long id;
    
    public int reportId;
    public String damageDescription;
    
    
}