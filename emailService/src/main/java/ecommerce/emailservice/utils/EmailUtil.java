package ecommerce.emailservice.utils;

import java.util.Date;

import javax.mail.Message;

import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class EmailUtil {

    @Value("${spring.mail.username}")
    private String configuredEmail;

    /**
     * Utility method to send simple HTML email
     * @param session
     * @param toEmail
     * @param fullName
     */
    public void sendEmail(Session session, String toEmail, String fullName){
        try
        {
            String  subject = "Welcome to TrendWatch, " + fullName + "!";
            String body = "Welcome to TrendWatch, " + fullName +
                          "We are thrilled to have you on board. Get ready to embark on an exciting journey with us." +
                          "Best regards,\n Team TrendWatch";
            MimeMessage msg = new MimeMessage(session);
            //set message headers
            msg.addHeader("Content-type", "text/HTML; charset=UTF-8");
            msg.addHeader("format", "flowed");
            msg.addHeader("Content-Transfer-Encoding", "8bit");

            msg.setFrom(new InternetAddress(configuredEmail, "Team TrendWatch"));

            msg.setReplyTo(InternetAddress.parse(configuredEmail, false));

            msg.setSubject(subject, "UTF-8");

            msg.setText(body, "UTF-8");

            msg.setSentDate(new Date());

            msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail, false));
            System.out.println("Message is ready");
            Transport.send(msg);

            System.out.println("EMail Sent Successfully!!");
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
