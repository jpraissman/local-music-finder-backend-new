package com.thelocalmusicfinder.thelocalmusicfinderbackend.dto;

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
import lombok.Data;

@Data
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
  private String facebookUrl;

  @Size(min = 1, max = 255)
  private String instagramUrl;

  @Size(min = 1, max = 255)
  private String websiteUrl;

  @Size(min = 1, max=20)
  private String venuePhone;

  @NotBlank
  @Size(min = 1, max = 255)
  private String posterEmail;

  @NotNull
  @DateTimeFormat(pattern = "yyyy-MM-dd")
  private LocalDate eventDate;

  @NotNull
  @DateTimeFormat(pattern = "HH:mm")
  private LocalTime startTime;

  @DateTimeFormat(pattern = "HH:mm")
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
