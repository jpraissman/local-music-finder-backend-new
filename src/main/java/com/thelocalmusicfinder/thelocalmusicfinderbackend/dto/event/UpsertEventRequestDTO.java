package com.thelocalmusicfinder.thelocalmusicfinderbackend.dto.event;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.thelocalmusicfinder.thelocalmusicfinderbackend.domain.band.BandType;
import com.thelocalmusicfinder.thelocalmusicfinderbackend.domain.EventCreatorType;
import com.thelocalmusicfinder.thelocalmusicfinderbackend.domain.band.Genre;

import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
@Builder
public class UpsertEventRequestDTO {
  @NotBlank
  @Size(min = 1, max = 100)
  private String bandName;

  @NotNull
  private BandType bandType;

  @Size(min = 1, max = 100)
  private String tributeBandName;

  @NotNull
  @Size(min = 1)
  private List<Genre> genres;

  @NotBlank
  @Size(min = 1, max = 100)
  private String venueName;

  @NotBlank
  @Size(min = 1, max = 255)
  private String address;

  @Size(min = 1, max = 255)
  private String venueFacebookUrl;

  @Size(min = 1, max = 255)
  private String venueInstagramUrl;

  @Size(min = 1, max = 255)
  private String venueWebsiteUrl;

  @Size(min = 1, max = 255)
  private String bandFacebookUrl;

  @Size(min = 1, max = 255)
  private String bandInstagramUrl;

  @Size(min = 1, max = 255)
  private String bandWebsiteUrl;

  @Size(min = 1, max=20)
  private String venuePhone;

  @NotBlank
  @Size(min = 1, max = 255)
  private String posterEmail;

  @NotNull
  @DateTimeFormat(pattern = "yyyy-MM-dd")
  @JsonFormat(pattern = "yyyy-MM-dd")
  private LocalDate eventDate;

  @NotNull
  @DateTimeFormat(pattern = "HH:mm")
  @JsonFormat(pattern = "HH:mm")
  private LocalTime startTime;

  @DateTimeFormat(pattern = "HH:mm")
  @JsonFormat(pattern = "HH:mm")
  private LocalTime endTime;

  @NotNull
  @Min(0)
  private Double coverCharge;

  @Size(min = 1, max = 1000)
  private String additionalInfo;

  @NotNull
  private EventCreatorType eventCreator;

  @AssertTrue
  private boolean agreesToTermsAndPrivacy;
}
