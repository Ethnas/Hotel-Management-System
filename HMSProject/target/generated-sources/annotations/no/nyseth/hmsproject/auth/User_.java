package no.nyseth.hmsproject.auth;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import no.nyseth.hmsproject.auth.Group;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2020-11-30T15:31:09")
@StaticMetamodel(User.class)
public class User_ { 

    public static volatile SingularAttribute<User, String> firstName;
    public static volatile SingularAttribute<User, String> lastName;
    public static volatile SingularAttribute<User, String> password;
    public static volatile SingularAttribute<User, String> phone;
    public static volatile ListAttribute<User, Group> groups;
    public static volatile SingularAttribute<User, String> email;
    public static volatile SingularAttribute<User, String> username;

}