package com.thelocalmusicfinder.thelocalmusicfinderbackend.models;

import com.thelocalmusicfinder.thelocalmusicfinderbackend.domain.EventCreatorType;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Event {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(length = 20, nullable = false, unique = true)
  private String eventCode;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "band_id", nullable = false)
  private Band band;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "venue_id", nullable = false)
  private Venue venue;

  @Column(nullable = false, updatable = false)
  private Instant createdAt;

  @Column(nullable = false)
  private String email;

  @Column(nullable = false)
  private LocalDate eventDate;

  @Column(nullable = false)
  private LocalTime startTime;

  @Column()
  private LocalTime endTime;

  @Column(nullable = false)
  private Double coverCharge;

  @Column(length = 1000)
  private String additionalInfo;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private EventCreatorType eventCreator;

  @Column(nullable = false)
  private Boolean agreesToTermsAndPrivacy;

  @PrePersist
  protected void onCreate() {
    this.createdAt = Instant.now();
  }
}
