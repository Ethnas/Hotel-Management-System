package no.nyseth.hmsproject.auth;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import no.nyseth.hmsproject.auth.User;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2020-12-03T17:30:11")
@StaticMetamodel(Group.class)
public class Group_ { 

    public static volatile SingularAttribute<Group, String> name;
    public static volatile ListAttribute<Group, User> users;

}