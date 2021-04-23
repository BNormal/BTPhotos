package com.utils;
 
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Properties;
 
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
 
public class JavaMail {
	
	// message info
    private static String host = "smtp.gmail.com";
    private static String port = "587";
    private static String mailFrom = "lifedeathtest@gmail.com";
    private static String password = "Pizzapizza!";
    
    public static void sendEmailWithAttachments(final String address, final String subject, final String message, final ArrayList<String> attachments) throws AddressException, MessagingException {
    	sendEmailWithAttachments(address, subject, message, attachments, mailFrom, password);
    }
    
    public static void sendEmailWithAttachments(final String address, final String subject, final String message, final ArrayList<String> attachments, final String userName, final String password)
            throws AddressException, MessagingException {
        // sets SMTP server properties
        Properties properties = new Properties();
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", port);
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.user", userName);
        properties.put("mail.password", password);
 
        // creates a new session with an authenticator
        Authenticator auth = new Authenticator() {
            public PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(userName, password);
            }
        };
        Session session = Session.getInstance(properties, auth);
 
        // creates a new e-mail message
        Message msg = new MimeMessage(session);
 
        msg.setFrom(new InternetAddress(userName));
        InternetAddress[] toAddresses = { new InternetAddress(address) };
        msg.setRecipients(Message.RecipientType.TO, toAddresses);
        msg.setSubject(subject);
        msg.setSentDate(new Date());
 
        // creates message part
        MimeBodyPart messageBodyPart = new MimeBodyPart();
        messageBodyPart.setContent(message, "text/html");
 
        // creates multi-part
        Multipart multipart = new MimeMultipart();
        multipart.addBodyPart(messageBodyPart);
 
        // adds attachments
        if (attachments != null && attachments.size() > 0) {
            for (String filePath : attachments) {
                MimeBodyPart attachPart = new MimeBodyPart();
 
                try {
                    attachPart.attachFile(filePath);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
 
                multipart.addBodyPart(attachPart);
            }
        }
 
        // sets the multi-part as e-mail's content
        msg.setContent(multipart);
 
        // sends the e-mail
        Transport.send(msg);
 
    }
 
    /**
     * Test sending e-mail with attachments
     */
    /*public static void main(String[] args) {
        // attachments
        //attachFiles[1] = "e:/Test/Music.mp3";
        //attachFiles[2] = "e:/Test/Video.mp4";
        Email mail = new Email("life@emma.wtf");
        mail.addAttachment("F:/Sync/Life and Death/School/CS 499 - Senior Project/BTPhotos.jpg");
        
        try {
            sendEmailWithAttachments(mail.getAddress(), mail.getSubject(), mail.getMessage(), mail.getAttachments(), mailFrom, password);
            System.out.println("Email sent.");
        } catch (Exception ex) {
            System.out.println("Could not send email.");
            ex.printStackTrace();
        }
    }*/
}