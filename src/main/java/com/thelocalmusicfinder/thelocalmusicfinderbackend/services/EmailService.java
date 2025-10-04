package com.thelocalmusicfinder.thelocalmusicfinderbackend.services;

import com.thelocalmusicfinder.thelocalmusicfinderbackend.domain.EmailMessage;
import com.thelocalmusicfinder.thelocalmusicfinderbackend.models.Event;
import com.thelocalmusicfinder.thelocalmusicfinderbackend.ports.driven.ForSendingEmails;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmailService {
  @Value("${website.url}")
  private String websiteUrl;

  @Value("${from.email}")
  private String fromEmail;

  private final ForSendingEmails emailService;

  public void sendEventConfirmationEmail(Event event) {
    String htmlBody = String.format(
      """
      <p>Thanks for creating an event with The Local Music Finder! Your event has been posted and is available for all users to see.</p>
      <p>To <strong>edit</strong> or <strong>delete</strong> your event,\s
      <a href="%s"> click here</a> and paste in your Event ID:\s
      <strong> %s</strong>.
      </p>
      <p>
        <span style="text-decoration: underline;">Your Event Details:</span><br />
        <em>Venue Name:</em> %s<br />
        <em>Venue Address:</em> %s<br />
        <em>Band Name:</em> %s<br />
        <em>Band Type:</em> %s<br />
        <em>Band Genres:</em> %s<br />
        <em>Event Date:</em> %s<br />
        <em>Start Time:</em> %s
      </p>
      <br />
      <p>Sincerely,<br />The Local Music Finder</p>
     \s""",
      websiteUrl + "edit",
      event.getEventCode(),
      event.getVenue().getVenueName(),
      event.getVenue().getAddress(),
      event.getBand().getBandName(),
      event.getBand().getBandType(),
      event.getBand().getGenres().stream().map(Enum::name).collect(Collectors.joining(", ")),
      event.getEventDate().format(DateTimeFormatter.ofPattern("MMMM dd, yyyy")),
      event.getStartTime().format(DateTimeFormatter.ofPattern("hh:mm a"))
    );

    EmailMessage emailMessage = EmailMessage.builder()
            .fromEmail(fromEmail)
            .fromName("The Local Music Finder")
            .toEmail(event.getEmail())
            .toName(event.getEmail())
            .subject("Your event with The Local Music Finder has been created!")
            .htmlBody(htmlBody).build();
    emailService.sendEmail(emailMessage);
  }

  public void sendAdminEventCreatedEmail(Event event) {
    String htmlBody = String.format(
      """
      <p>An event has been created.</p>
      <p>
        <span style="text-decoration: underline;">Event Details:</span><br />
        <strong><em>Venue Name: </em></strong>%s<br />
        <strong><em>Venue Address: </em></strong>%s<br />
        <strong><em>Band Name:</em></strong> %s<br />
        <strong><em>Band Type:</em></strong> %s<br />
        <strong><em>Tribute Band Name:</em></strong> %s<br />
        <strong><em>Band Genres:</em></strong> %s<br />
        <strong><em>Event Date: </em></strong> %s<br />
        <strong><em>Start Time: </em></strong> %s<br />
        <strong><em>Cover Charge:</em></strong> %s<br />
        <strong><em>Other Info:</em></strong> %s<br />
        <strong><em>Venue Phone Number:</em></strong> %s<br />
        <strong><em>Email Address:</em></strong> %s
      </p>
      """,
      event.getVenue().getVenueName(),
      event.getVenue().getAddress(),
      event.getBand().getBandName(),
      event.getBand().getBandType(),
      event.getBand().getTributeBandName(),
      event.getBand().getGenres().stream().map(Enum::name).collect(Collectors.joining(", ")),
      event.getEventDate().format(DateTimeFormatter.ofPattern("MMMM dd, yyyy")),
      event.getStartTime().format(DateTimeFormatter.ofPattern("hh:mm a")),
      event.getCoverCharge(),
      event.getAdditionalInfo(),
      event.getVenue().getPhoneNumber(),
      event.getEmail()
    );

    EmailMessage emailMessage = EmailMessage.builder()
            .fromEmail(fromEmail)
            .fromName("The Local Music Finder")
            .toEmail(fromEmail)
            .toName("The Local Music Finder")
            .subject("An event has been created")
            .htmlBody(htmlBody).build();
    emailService.sendEmail(emailMessage);
  }

  public void sendDuplicateEventsEmail(List<Event> duplicateEvents) {
    StringBuilder htmlBody = new StringBuilder();
    htmlBody.append(String.format(
            "<p>There are %d potential duplicate events on <strong>%s</strong></p><br />",
            duplicateEvents.size(),
            duplicateEvents.getFirst().getEventDate().format(DateTimeFormatter.ofPattern("MMMM dd, yyyy"))
    ));
    for (Event event : duplicateEvents) {
      htmlBody.append(String.format(
              "<p>At <strong>%s</strong> with the address <strong>%s</strong>.</p>",
              event.getVenue().getVenueName(),
              event.getVenue().getAddress()
      ));
    }

    EmailMessage emailMessage = EmailMessage.builder()
            .fromEmail(fromEmail)
            .fromName("The Local Music Finder")
            .toEmail(fromEmail)
            .toName("The Local Music Finder")
            .subject("There are potential duplicate events")
            .htmlBody(htmlBody.toString()).build();
    emailService.sendEmail(emailMessage);
  }
}
