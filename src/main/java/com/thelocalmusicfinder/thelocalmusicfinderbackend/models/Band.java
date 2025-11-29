package com.thelocalmusicfinder.thelocalmusicfinderbackend.models;

import com.thelocalmusicfinder.thelocalmusicfinderbackend.domain.band.BandType;
import com.thelocalmusicfinder.thelocalmusicfinderbackend.domain.band.Genre;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Band {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(length = 100, nullable = false)
  private String bandName;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private BandType bandType;

  @Column(length = 100)
  private String tributeBandName;

  @ElementCollection
  @Enumerated(EnumType.STRING)
  @CollectionTable(name = "band_genres", joinColumns = @JoinColumn(name = "band_id"))
  @Column(name = "genre")
  private List<Genre> genres = new ArrayList<>();

  @Column()
  private String facebookUrl;

  @Column()
  private String instagramUrl;

  @Column()
  private String websiteUrl;

  @ElementCollection
  @CollectionTable(name = "band_youtube_video_ids", joinColumns = @JoinColumn(name = "band_id"))
  @Column(name = "youtube_video_id")
  private List<String> youtubeVideoIds = new ArrayList<>();

  @OneToMany(mappedBy = "band", cascade = CascadeType.ALL, orphanRemoval = true)
  @Builder.Default
  private List<Event> events = List.of();

}
