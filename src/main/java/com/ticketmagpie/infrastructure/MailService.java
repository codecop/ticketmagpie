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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class MailService {

  public final Properties sessionProperties = new Properties();
  public final Authenticator authenticator;
  private final String username;

  @Autowired
  public MailService(@Value("${mail.smtp.auth}") boolean smtpAuth,
                     @Value("${mail.smtp.starttls.enable}") boolean tlsEnabled,
                     @Value("${mail.smtp.host}") String smtpHost,
                     @Value("${mail.smtp.port}") int smtpPort,
                     @Value("${mail.smtp.username}") String username,
                     @Value("${mail.smtp.password}") String password) {

    sessionProperties.put("mail.smtp.auth", Boolean.toString(smtpAuth));
    sessionProperties.put("mail.smtp.starttls.enable", Boolean.toString(tlsEnabled));
    sessionProperties.put("mail.smtp.host", smtpHost);
    sessionProperties.put("mail.smtp.port", Integer.toString(smtpPort));

    authenticator = new Authenticator() {
      @Override
      protected PasswordAuthentication getPasswordAuthentication() {
        return new PasswordAuthentication(username, password);
      }
    };
    this.username = username;
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
