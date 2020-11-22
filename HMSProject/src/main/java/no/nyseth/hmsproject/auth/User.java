package no.nyseth.hmsproject.auth;
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
//package ..../auth

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.json.bind.annotation.JsonbTransient;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQuery;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import static no.nyseth.hmsproject.auth.User.FIND_ALL_USERS;
import static no.nyseth.hmsproject.auth.User.FIND_USER_BY_IDS;

/**
 *
 * @author nyseth
 */
@Entity
@Table(name = "AUSER")
@Data 
@AllArgsConstructor 
@NoArgsConstructor
@NamedQuery(name = FIND_ALL_USERS, query = "select u from User u order by u.firstName")
public class User implements Serializable {
    public static final String FIND_USER_BY_IDS = "User.findUserByIds";
    public static final String FIND_ALL_USERS = "User.findAllUsers";
    
    @Id
    String username;
    
    @JsonbTransient
    String password;
    
    
    
    String firstName;
    String lastName;
    @Email
    String email;
    String phone;
    String role;
    
    /*
    @ManyToMany
    @JoinTable(name="AUSERGROUP",
            joinColumns = @JoinColumn(name="userid", referencedColumnName = "userid"),
            inverseJoinColumns = @JoinColumn(name="name",referencedColumnName = "name"))
    List<Group> groups;
    */
    
    /*
    public List<Group> getGroups() {
        if(groups == null) {
            groups = new ArrayList<>();
        }
        return groups;
    }*/
    
    
}