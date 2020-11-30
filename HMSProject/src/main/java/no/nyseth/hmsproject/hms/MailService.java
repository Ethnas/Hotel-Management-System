/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package no.nyseth.hmsproject.hms;

import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 *
 * @author Erlend
 */
public class MailService {
    
    String mailHost = "smtp.gmail.com"; //Mailserver. E.g. smtp.gmail.com
    
    String mailUser = "kakerull214056@gmail.com"; //Email. E.g. *****@gmail.com
    String mailPwd = "Passord123"; //Password for said email.
    
    
    
    public void sendMail(String reciever, String subject, String message) {
        System.out.println(reciever + " . " + subject + " . " + message);
        Properties mailProp = new Properties();
        mailProp.put("mail.smtp.starttls.enable", true);
        mailProp.put("mail.smtp.auth", "true");
        mailProp.put("mail.smtp.host", mailHost);
        mailProp.put("mail.smtp.port", "587");
        
        Session mailSess = Session.getInstance(mailProp, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(mailUser, mailPwd);
            }
        });
        
        try {
            Message mailMsg = new MimeMessage(mailSess);
            mailMsg.setRecipient(Message.RecipientType.TO, new InternetAddress(reciever));
            mailMsg.setFrom(new InternetAddress(mailUser));
            mailMsg.setSubject(subject);
            mailMsg.setText(message);
            
            Transport.send(mailMsg);
        } catch (MessagingException ex) {
            Logger.getLogger(MailService.class.getName()).log(Level.SEVERE, null, ex);
        } 
        
    }
    
}
