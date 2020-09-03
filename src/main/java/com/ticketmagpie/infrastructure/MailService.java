package com.ticketmagpie.infrastructure;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MailService {

  public final Properties sessionProperties = new Properties();
  public final Authenticator authenticator;
  private final String username;

  @Autowired
  public MailService(MailConfiguration smtp) {
    sessionProperties.put("mail.smtp.auth", Boolean.toString(smtp.isAuth()));
    sessionProperties.put("mail.smtp.starttls.enable", Boolean.toString(smtp.isTls()));
    sessionProperties.put("mail.smtp.host", smtp.getHost());
    sessionProperties.put("mail.smtp.port", Integer.toString(smtp.getPort()));

    authenticator = new Authenticator() {
      @Override
      protected PasswordAuthentication getPasswordAuthentication() {
        return new PasswordAuthentication(username, smtp.getPassword());
      }
    };
    this.username = smtp.getUsername();
  }

  public void sendEmail(String to, String subject, String body) {
    Session session = Session.getInstance(sessionProperties, authenticator);

    try {

      Message message = new MimeMessage(session);
      message.setFrom(new InternetAddress(username));
      message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
      message.setSubject(subject);
      message.setText(body);

      Transport.send(message);

    } catch (MessagingException e) {
      throw new RuntimeException(e);
    }
  }
}
