package com.thelocalmusicfinder.thelocalmusicfinderbackend.adapters.driven;

import com.thelocalmusicfinder.thelocalmusicfinderbackend.domain.EmailMessage;
import com.thelocalmusicfinder.thelocalmusicfinderbackend.errors.exceptions.EmailSendException;
import com.thelocalmusicfinder.thelocalmusicfinderbackend.external.mailersend.MailerSendService;
import com.thelocalmusicfinder.thelocalmusicfinderbackend.ports.driven.ForSendingEmails;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ForSendingMailerSendEmails implements ForSendingEmails {
  private final MailerSendService mailerSendService;

  @Override
  public void sendEmail(EmailMessage emailMessage) throws EmailSendException {
    this.mailerSendService.sendEmail(emailMessage);
  }
}
