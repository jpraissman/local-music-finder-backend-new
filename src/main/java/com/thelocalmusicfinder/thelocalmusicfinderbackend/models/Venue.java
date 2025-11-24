package com.thelocalmusicfinder.thelocalmusicfinderbackend.models;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Venue {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(length = 100, nullable = false)
  private String venueName;

  @OneToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "location_id", referencedColumnName = "locationId", nullable = false)
  private Location location;

  @Column()
  private String phoneNumber;

  @Column()
  private String facebookUrl;

  @Column()
  private String instagramUrl;

  @Column()
  private String websiteUrl;

  @OneToMany(mappedBy = "venue", cascade = CascadeType.ALL, orphanRemoval = true)
  @Builder.Default
  private List<Event> events = List.of();

}
