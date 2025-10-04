package com.thelocalmusicfinder.thelocalmusicfinderbackend.ports.driven;

import com.thelocalmusicfinder.thelocalmusicfinderbackend.domain.EmailMessage;
import com.thelocalmusicfinder.thelocalmusicfinderbackend.errors.exceptions.EmailSendException;

public interface ForSendingEmails {
  void sendEmail(EmailMessage emailMessage) throws EmailSendException;
}
