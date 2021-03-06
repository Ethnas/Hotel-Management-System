/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
//package ..../auth
package no.nyseth.hmsproject.auth;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Set;
import java.util.logging.Level;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.security.enterprise.credential.UsernamePasswordCredential;
import javax.security.enterprise.identitystore.CredentialValidationResult;
import javax.security.enterprise.identitystore.IdentityStoreHandler;
import javax.security.enterprise.identitystore.PasswordHash;
import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;
import javax.validation.constraints.NotBlank;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import lombok.extern.java.Log;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.InvalidKeyException;
import javax.annotation.Resource;
import javax.ejb.EJBContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.FormParam;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.PathParam;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import org.eclipse.microprofile.jwt.JsonWebToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;
import javax.ws.rs.core.Response.ResponseBuilder;


/**
 *
 * @author nyseth
 */
@Path("auth")
@Stateless
@Log
public class AuthenticationService {
    private static final String INSERT_USERGROUP = "INSERT INTO AUSERGROUP(NAME,USERNAME) VALUES (?,?)";
    private static final String DELETE_USERGROUP = "DELETE FROM AUSERGROUP WHERE NAME = ? AND USERNAME = ?";
   
    @Inject
    KeyService keyService;
    
    @Inject
    IdentityStoreHandler identityStoreHandler;
    
    @Resource()
    DataSource ds;
    
    @PersistenceContext
    EntityManager em;
    
    @Inject
    PasswordHash hasher;
    
    @Inject
    @ConfigProperty(name = "mp.jwt.verify.issuer", defaultValue = "issuer")
    String issuer;
    
    @Inject
    JsonWebToken principal;
    
    @Context
    SecurityContext sc1;
    
    @Resource
    EJBContext securityContext;
    
    
    @POST
    @Path("create")
    @Produces(MediaType.APPLICATION_JSON)
    public Response createUser(@FormParam("uid") String uid, @FormParam("pwd") String pwd, 
    @FormParam("firstName") String firstName, @FormParam("lastName") String lastName, 
    @FormParam("eml") String eml, @FormParam("phoneNumber") String phoneNumber) {
        User user = em.find(User.class, uid);
        if (user != null) {
            log.log(Level.INFO, "user already exists {0}", uid);
            return Response.status(Response.Status.BAD_REQUEST).build();
        } else {
            user = new User();
            user.setUsername(uid);
            user.setPassword(hasher.generate(pwd.toCharArray()));
            user.setFirstName(firstName);
            user.setLastName(lastName);
            user.setEmail(eml);
            user.setPhone(phoneNumber);
            Group usergroup = em.find(Group.class, Group.USER);
            user.getGroups().add(usergroup);
            return Response.ok(em.merge(user)).build();
        }
    }
    
    @POST
    @Path("logingoogle")
    public Response loginGoogle(@FormParam("idToken") String token, @FormParam("clientid") String clientId) {
        ResponseBuilder resp;
        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), new JacksonFactory())
            // Specify the CLIENT_ID of the app that accesses the backend:
            .setAudience(Collections.singletonList(clientId))
            .build();
        
        try {
            GoogleIdToken idToken = verifier.verify(token);
            if (verifyGoogleToken(idToken)) {
                String headerToken = authLoginGoogle(idToken);
                if (headerToken != null) {
                    resp = Response.ok(headerToken)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + headerToken);
                } else {
                    resp = Response.status(Response.Status.UNAUTHORIZED);
                }
            } else {
                resp = Response.status(Response.Status.UNAUTHORIZED);
            }
        } catch (GeneralSecurityException | IOException e) {
                e.printStackTrace();
                resp = Response.serverError();
        }
        return resp.build();
    }
    
    private boolean verifyGoogleToken(GoogleIdToken idToken) {
        boolean verified;            
            if (idToken != null ) {
                Payload payload = idToken.getPayload();

                // Print user identifier
                String userId = payload.getSubject();
                System.out.println("User ID: " + userId);
                User user = em.find(User.class, userId);
                if (user == null) {
                    // Get profile information from payload
                    user = new User();
                    user.setUsername(userId);
                    String p = userId + payload.get("name");
                    char[] pwd = p.toCharArray();
                    user.setPassword(hasher.generate(pwd));
                    user.setEmail(payload.getEmail());
                    user.setFirstName((String) payload.get("given_name"));
                    user.setLastName((String) payload.get("family_name"));
                    user.setPhone("12345678");
                    Group usergroup = em.find(Group.class, Group.USER);
                    user.getGroups().add(usergroup);
                    em.merge(user);
                    verified = true;
                } else {
                    
                    verified = true;
                }
 
            } else {
                System.out.println("Invalid ID token.");
                verified = false;
            }

        return verified;
    }
    
    private String authLoginGoogle(GoogleIdToken idToken) {
        String token = null;
        Payload payload = idToken.getPayload();
        String uid = payload.getSubject();
        String pwd = uid + payload.get("name");
        CredentialValidationResult result = identityStoreHandler.validate(
                new UsernamePasswordCredential(uid, pwd));
        if (result.getStatus() == CredentialValidationResult.Status.VALID) {
            token = issueToken(result.getCallerPrincipal().getName(),
                    result.getCallerGroups());
            
        } else {
            log.log(Level.INFO, "user not logged in {0}", uid);
            
        }
        return token;
    }
    

    @GET
    @Path("login")
    public Response login(
            @QueryParam("uid") @NotBlank String uid,
            @QueryParam("pwd") @NotBlank String pwd,
            @Context HttpServletRequest request) {
        CredentialValidationResult result = identityStoreHandler.validate(
                new UsernamePasswordCredential(uid, pwd));
        log.log(Level.INFO, "checking credentials", uid);
        if (result.getStatus() == CredentialValidationResult.Status.VALID) {
            String token = issueToken(result.getCallerPrincipal().getName(),
                    result.getCallerGroups());
            log.log(Level.INFO, "user logged in {0}", uid);
            return Response
                    .ok(token)
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                    .build();
        } else {
            log.log(Level.INFO, "user not logged in {0}", uid);
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
    }
    
      private String issueToken(String name, Set<String> groups) {
        try {
            Date now = new Date();
            Date expiration = Date.from(LocalDateTime.now().plusDays(1L).atZone(ZoneId.systemDefault()).toInstant());
            JwtBuilder jb = Jwts.builder()
                    .setHeaderParam("typ", "JWT")
                    .setHeaderParam("kid", "abc-1234567890")
                    .setSubject(name)
                    .setId("a-123")
                    //.setIssuer(issuer)
                    .claim("iss", issuer)
                    .setIssuedAt(now)
                    .setExpiration(expiration)
                    .claim("upn", name)
                    .claim("groups", groups)
                    .claim("aud", "aud")
                    .claim("auth_time", now)
                    .signWith(keyService.getPrivate());
            return jb.compact();
        } catch (InvalidKeyException t) {
            log.log(Level.SEVERE, "Failed to create token", t);
            throw new RuntimeException("Failed to create token", t);
        }
    }
    
    @GET
    @Path("currentuser")
    @RolesAllowed({Group.USER, Group.STAFF})
    @Produces(MediaType.APPLICATION_JSON)
    public User getCurrentUser() {
        /*if (principal == null) {
            System.out.println("sad");
        }
        
        System.out.println("---------");
        System.out.println(principal);
        System.out.println("---------");
        
        if (sc == null) {
            System.out.println("shit very fuck");
            System.out.println("shit very fuck");
            System.out.println("shit very fuck");
            System.out.println("shit very fuck");
            System.out.println("shit very fuck");
            System.out.println("shit very fuck");
        }
        //
        String username = securityContext.getCallerPrincipal().getName();
        System.out.println("---------");
        System.out.println(username);
        System.out.println("---------");
        
        System.out.println("---------");
        System.out.println(sc.getUserPrincipal());
        System.out.println(sc.getUserPrincipal().getName());
        System.out.println("---------");
        
        
        System.out.println("---------");
        System.out.println("---------");
        System.out.println("---------");
        System.out.println(principal.getAudience());
        System.out.println(principal.getClaimNames());
        System.out.println(principal.getGroups());
        System.out.println(principal.getIssuer());
        System.out.println(principal.getName());
        System.out.println(principal.getRawToken());
        System.out.println(principal.getSubject());
        System.out.println(principal.getTokenID());
        System.out.println(principal);
        System.out.println("---------");
        System.out.println("---------");
        System.out.println("---------");
        */
        
        return em.find(User.class, principal.getName());
    }
    
    private boolean roleExists(String role) {
        boolean result = false;
        
        if (role != null) {
            switch (role) {
                case Group.ADMIN:
                case Group.USER:
                    result = true;
                    break;
            }
        }
        
        return result;
    }
    
    @PUT
    @Path("addrole")
    @RolesAllowed(value = {Group.ADMIN})
    public Response addRole(@QueryParam("uid") String uid, @QueryParam("role") String role) {
        if (!roleExists(role)) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }
        
        try (Connection c = ds.getConnection();
                PreparedStatement psg = c.prepareStatement(INSERT_USERGROUP)) {
            psg.setString(1, role);
            psg.setString(2, uid);
            psg.executeUpdate();
        } catch (SQLException ex) {
            log.log(Level.SEVERE, null, ex);
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        
        return Response.ok().build();
    }
    
    @PUT
    @Path("removerole")
    @RolesAllowed(value = {Group.ADMIN})
    public Response removeRole(@QueryParam("uid") String uid, @QueryParam("role") String role) {
        if (!roleExists(role)) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }
        
        try (Connection c = ds.getConnection();
                PreparedStatement psg = c.prepareStatement(DELETE_USERGROUP)) {
            psg.setString(1, role);
            psg.setString(2, uid);
            psg.executeUpdate();
        } catch (SQLException ex) {
            log.log(Level.SEVERE, null, ex);
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        
        return Response.ok().build();
    }
   
}