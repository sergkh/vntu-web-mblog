package edu.vntu.mblog.services;

import com.sun.mail.smtp.SMTPTransport;
import edu.vntu.mblog.domain.User;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import java.security.Security;
import java.util.Date;
import java.util.Properties;

public class EmailService {
    private static EmailService instance = new EmailService();

    private String senderEmail = "vntuwebmblog@gmail.com";
    private String senderPassword = "vntuwebmblo";

    private EmailService(){}

    public boolean sendRegisterEmail(User user, String password) {
        String subject = "Вітаємо! Ви успішно зареєстровані на vntu-web-mblog!";
        String message = "Вітаємо! Ви успішно зареєстровані на vntu-web-mblog! Ваш логін:" + user.getLogin()+ "." +
                "Ваш пароль: " + password;


        try {
            Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());
            final String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";

            Properties props = System.getProperties();
            props.setProperty("mail.smtps.host", "smtp.gmail.com");
            props.setProperty("mail.smtp.socketFactory.class", SSL_FACTORY);
            props.setProperty("mail.smtp.socketFactory.fallback", "false");
            props.setProperty("mail.smtp.port", "465");
            props.setProperty("mail.smtp.socketFactory.port", "465");
            props.setProperty("mail.smtps.auth", "true");

            props.put("mail.smtps.quitwait", "false");

            Session session = Session.getInstance(props, null);

            final MimeMessage msg = new MimeMessage(session);

            msg.setFrom(new InternetAddress(senderEmail));
            msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(user.getEmail(), false));

            msg.setSubject(subject);

            msg.setText(message, "utf-8");
            msg.setSentDate(new Date());

            SMTPTransport t = (SMTPTransport) session.getTransport("smtps");

            t.connect("smtp.gmail.com", senderEmail, senderPassword);
            t.sendMessage(msg, msg.getAllRecipients());
            t.close();

        } catch (MessagingException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static EmailService getInstance() {
        return instance;
    }
}
