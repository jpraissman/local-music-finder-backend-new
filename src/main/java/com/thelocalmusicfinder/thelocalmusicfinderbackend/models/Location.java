package com.thelocalmusicfinder.thelocalmusicfinderbackend.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Location {

  @Id
  private String locationId;

  @Column(length = 500, nullable = false)
  private String formattedAddress;

  @Column(nullable = false)
  private double latitude;

  @Column(nullable = false)
  private double longitude;

  @Column(length = 250)
  private String town;

  @Column(length = 250)
  private String county;

}
