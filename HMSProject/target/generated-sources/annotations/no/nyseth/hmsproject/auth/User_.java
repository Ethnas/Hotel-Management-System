package no.nyseth.hmsproject.auth;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import no.nyseth.hmsproject.auth.Group;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2020-11-05T10:13:01")
@StaticMetamodel(User.class)
public class User_ { 

    public static volatile SingularAttribute<User, String> firstName;
    public static volatile SingularAttribute<User, String> lastName;
    public static volatile SingularAttribute<User, String> password;
    public static volatile SingularAttribute<User, String> phoneNumber;
    public static volatile SingularAttribute<User, Date> created;
    public static volatile ListAttribute<User, Group> groups;
    public static volatile SingularAttribute<User, String> userid;
    public static volatile SingularAttribute<User, String> email;

}