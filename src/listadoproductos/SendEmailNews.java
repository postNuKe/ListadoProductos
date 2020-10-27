package listadoproductos;

import com.sun.mail.smtp.SMTPTransport;
import java.util.ArrayList;
import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.*;
import javax.activation.*;
import listadoproductos.info.Product;

public class SendEmailNews {
    // for example, smtp.mailgun.org
    private static final String SMTP_SERVER = "localhost";
    private static final String USERNAME = "";
    private static final String PASSWORD = "";

    private static final String EMAIL_FROM = "organicosvapeo@gmail.com";
    private static final String EMAIL_TO = "email@gmail.com,email2@gmail.com";
    private static final String EMAIL_TO_CC = "";

    private static final String EMAIL_SUBJECT = "Organicos Vapeo: Novedades y Ofertas";
    private static final String EMAIL_TEXT = "https://organicosvapeo.000webhostapp.com/\n\n";

    public SendEmailNews(String emails, ArrayList<Product> aNewProducts, ArrayList<Product> aSpecialProducts) {

        Properties prop = System.getProperties();
        prop.put("mail.smtp.auth", "false");

        Session session = Session.getInstance(prop, null);
        Message msg = new MimeMessage(session);

        try {

            msg.setFrom(new InternetAddress(EMAIL_FROM));

            msg.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(emails, false));

            msg.setSubject(EMAIL_SUBJECT);

            // text
            MimeBodyPart p1 = new MimeBodyPart();
            String txt = EMAIL_TEXT;
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
            p1.setText(txt);

            // file
            /*
            MimeBodyPart p2 = new MimeBodyPart();
            FileDataSource fds = new FileDataSource("path/example.txt");
            p2.setDataHandler(new DataHandler(fds));
            p2.setFileName(fds.getName());
            */
            Multipart mp = new MimeMultipart();
            mp.addBodyPart(p1);
            //mp.addBodyPart(p2);

            msg.setContent(mp);


            SMTPTransport t = (SMTPTransport) session.getTransport("smtp");

			// connect
            t.connect(SMTP_SERVER, USERNAME, PASSWORD);

			// send
            t.sendMessage(msg, msg.getAllRecipients());

            System.out.println("Response: " + t.getLastServerResponse());

            t.close();

        } catch (MessagingException e) {
            e.printStackTrace();
        }

    }
    
}