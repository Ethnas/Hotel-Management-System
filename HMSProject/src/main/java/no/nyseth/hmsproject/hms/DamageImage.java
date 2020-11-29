/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package no.nyseth.hmsproject.hms;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author Erlend
 */
@Entity
@Table(name = "DamageImages")
@Data
@AllArgsConstructor 
@NoArgsConstructor
public class DamageImage {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int imageId;
    
    private String mimeType;
    
    private long filesize;
    
    @Lob
    @Column(name="Image")
    private byte[] image;
    
    @ManyToOne
    @JoinColumn(name = "ReportID")
    private DamageReport report;
}
