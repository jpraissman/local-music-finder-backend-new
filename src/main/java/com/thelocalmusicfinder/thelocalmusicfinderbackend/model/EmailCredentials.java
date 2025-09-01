package com.thelocalmusicfinder.thelocalmusicfinderbackend.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class EmailCredentials {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "google_access_token", nullable = false)
  private String googleAccessToken;

  @Column(name = "google_expiry", nullable = false)
  private String googleExpiry;

  // Default constructor needed by JPA
  public EmailCredentials() {}
}
