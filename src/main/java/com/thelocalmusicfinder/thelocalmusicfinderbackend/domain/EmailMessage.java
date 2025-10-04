package com.thelocalmusicfinder.thelocalmusicfinderbackend.domain;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@Data
@Builder
@ToString
public class EmailMessage {
  private final String fromEmail;
  private final String fromName;
  private final String toEmail;
  private final String toName;
  private final String subject;
  private final String htmlBody;
}
