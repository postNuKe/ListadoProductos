package listadoproductos;

//https://www.campusmvp.es/recursos/post/como-enviar-correo-electronico-con-java-a-traves-de-gmail.aspx

import com.sun.mail.smtp.SMTPTransport;
import java.util.ArrayList;
import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.*;
import listadoproductos.info.Product;

public class SendEmailNewsByGmail {
    private static final String EMAIL_SUBJECT = "Organicos Vapeo: Novedades y Ofertas";

    public SendEmailNewsByGmail(String gmailUserName, String gmailPassword, 
            String emails, ArrayList<Product> aNewProducts, 
            ArrayList<Product> aSpecialProducts) {        

        Properties props = System.getProperties();
        props.put("mail.smtp.host", "smtp.gmail.com");  //El servidor SMTP de Google
        props.put("mail.smtp.user", gmailUserName);
        props.put("mail.smtp.clave", gmailPassword);    //La clave de la cuenta
        props.put("mail.smtp.auth", "true");    //Usar autenticación mediante usuario y clave
        props.put("mail.smtp.starttls.enable", "true"); //Para conectar de manera segura al servidor SMTP
        props.put("mail.smtp.port", "587"); //El puerto SMTP seguro de Google

        Session session = Session.getDefaultInstance(props);
        MimeMessage message = new MimeMessage(session);

        try {
            message.setFrom(new InternetAddress(gmailUserName.concat("@gmail.com")));
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(emails, false));
            message.setSubject(EMAIL_SUBJECT);
            
            String txt = "ORGANICOS VAPEO\nhttps://organicosvapeo.000webhostapp.com\n\n\n\n";
            txt += "NOVEDADES\n";
            for (Product product : aNewProducts) { 	
                //System.out.println(productNew.getName());
                txt += product.toStringEmail() + "\n";
            }            
            txt += "\n\n\n\nOFERTAS\n";
            for (Product product : aSpecialProducts) { 	
                //System.out.println(productNew.getName());
                txt += product.toStringEmail() + "\n";
            }        
            
            message.setText(txt);
            SMTPTransport transport = (SMTPTransport)session.getTransport("smtp");
            transport.connect("smtp.gmail.com", gmailUserName, gmailPassword);
            transport.sendMessage(message, message.getAllRecipients());
            System.out.println("Response: " + transport.getLastServerResponse());
            transport.close();
        }
        catch (MessagingException me) {
            me.printStackTrace();   //Si se produce un error
        }

    }
    
}