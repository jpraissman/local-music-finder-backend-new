package com.thelocalmusicfinder.thelocalmusicfinderbackend.external.mailersend;

import com.mailersend.sdk.MailerSend;
import com.mailersend.sdk.MailerSendResponse;
import com.mailersend.sdk.emails.Email;
import com.mailersend.sdk.exceptions.MailerSendException;
import com.thelocalmusicfinder.thelocalmusicfinderbackend.domain.EmailMessage;
import com.thelocalmusicfinder.thelocalmusicfinderbackend.errors.exceptions.EmailSendException;
import com.thelocalmusicfinder.thelocalmusicfinderbackend.ports.driven.ForSendingEmails;
import com.thelocalmusicfinder.thelocalmusicfinderbackend.services.LoggerService;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MailerSendService {
  @Value("${mailersend.api.key}")
  private String mailersendApiKey;

  private final LoggerService logger;

  public void sendEmail(EmailMessage emailMessage) throws EmailSendException {
    Email email = new Email();
    email.setFrom(emailMessage.getFromName(), emailMessage.getFromEmail());
    email.addRecipient(emailMessage.getToName(), emailMessage.getToEmail());
    email.setSubject(emailMessage.getSubject());
    email.setHtml(emailMessage.getHtmlBody());

    MailerSend ms = new MailerSend();
    ms.setToken(mailersendApiKey);

    try {
      MailerSendResponse response = ms.emails().send(email);
    } catch (MailerSendException e) {
      logger.error("Error sending email with mailersend. EmailMessage: "
              + emailMessage.toString() + ". Error: " + e.responseBody);
      throw new EmailSendException("Error sending email with mailersend");
    }
  }
}
