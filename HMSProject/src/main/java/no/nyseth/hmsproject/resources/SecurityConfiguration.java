/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package no.nyseth.hmsproject.resources;

import javax.annotation.security.DeclareRoles;
import javax.security.enterprise.identitystore.DatabaseIdentityStoreDefinition;
import javax.security.enterprise.identitystore.PasswordHash;
import org.eclipse.microprofile.auth.LoginConfig;
import no.nyseth.hmsproject.auth.Group;

/**
 *
 * @author mikael
 */
@DatabaseIdentityStoreDefinition(
    dataSourceLookup=DatasourceProducer.JNDI_NAME,
    callerQuery="select password from auser where username = ?",
    groupsQuery="select name from ausergroup where username  = ?",
    hashAlgorithm = PasswordHash.class,
    priority = 80)
@DeclareRoles({Group.ADMIN,Group.USER,Group.STAFF})
@LoginConfig(authMethod = "MP-JWT",realmName = "template")
public class SecurityConfiguration {    
}
