package com.thelocalmusicfinder.thelocalmusicfinderbackend.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "events")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Setter(AccessLevel.PROTECTED)
    private Long id;

    @ManyToMany
    @JoinTable(
            name = "event_bands",
            joinColumns = @JoinColumn(name = "event_id"),
            inverseJoinColumns = @JoinColumn(name = "band_id")
    )
    private Set<Band> bands;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "venue_id")
    @JsonBackReference
    private Venue venue;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Enumerated(EnumType.STRING)
    @Column(name = "creator_type")
    private EventCreator creatorType;

    @Column(name = "start_time")
    private LocalDateTime startTime;

    @Column(name = "end_time")
    private LocalDateTime endTime;

    @Column(name = "event_url")
    private String eventUrl;

    @Column(name = "event_email")
    private String eventEmail;

    @Column(name = "event_phone")
    private String eventPhone;

    @Column(name = "event_page_id")
    private String eventPageId;

    @Column(name = "email_sent")
    private boolean emailSent;

    @Column(name = "terms_accepted")
    private boolean termsAccepted;
}
