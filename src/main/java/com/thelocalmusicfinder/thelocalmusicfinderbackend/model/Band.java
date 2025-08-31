package com.thelocalmusicfinder.thelocalmusicfinderbackend.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.Set;

@Entity
@Table(name = "bands")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Band {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Setter(AccessLevel.PROTECTED)
    private Long id;

    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "band_type")
    private BandType bandType;

    @ManyToMany(mappedBy = "bands")
    private Set<Event> events;

    @ElementCollection(targetClass = Genre.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "band_genres", joinColumns = @JoinColumn(name = "band_id"))
    @Enumerated(EnumType.STRING)
    @Column(name = "genre")
    private Set<Genre> genres;

    @Column(name = "tribute_band_name")
    private String tributeBandName;

    @Column(name = "facebook_url")
    private String facebookUrl;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "band_youtube_ids", joinColumns = @JoinColumn(name = "band_id"))
    @Column(name = "youtube_id")
    private List<String> youtubeIds;

    @Column(name = "instagram_url")
    private String instagramUrl;

    @Column(name = "website_url")
    private String websiteUrl;
}
