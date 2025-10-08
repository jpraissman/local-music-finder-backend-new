//package com.thelocalmusicfinder.thelocalmusicfinderbackend.services;
//
//import com.thelocalmusicfinder.thelocalmusicfinderbackend.domain.BasicVenueInfo;
//import com.thelocalmusicfinder.thelocalmusicfinderbackend.domain.maps.Coordinates;
//import com.thelocalmusicfinder.thelocalmusicfinderbackend.domain.maps.DetailedAddressInfo;
//import com.thelocalmusicfinder.thelocalmusicfinderbackend.models.Venue;
//import com.thelocalmusicfinder.thelocalmusicfinderbackend.repositories.VenueRepository;
//
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Nested;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.bean.override.mockito.MockitoBean;
//
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertNull;
//import static org.mockito.Mockito.verify;
//import static org.mockito.Mockito.when;
//
//@SpringBootTest
//public class VenueServiceTest {
//  @Autowired
//  private VenueService venueService;
//
//  @MockitoBean
//  private VenueRepository venueRepository;
//  @MockitoBean
//  private MapsService mapsService;
//  @MockitoBean
//  private EmailService emailService;
//  @MockitoBean
//  private LoggerService loggerService;
//
//  @Nested
//  @DisplayName("When updating venues during upsertVenue")
//  class UpdateVenueFromUpsert {
//
//    String venueName = "Venue 1";
//    String address = "123 Place Road";
//    String facebookUrl = "newFacebook";
//    String instagramUrl = "newInstagram";
//    String phoneNumber = "1234561234";
//    BasicVenueInfo venueInfo = new BasicVenueInfo(venueName, address, facebookUrl, instagramUrl, null, phoneNumber);
//
//    double lat = 1.0;
//    double lng = 2.0;
//    DetailedAddressInfo addressInfo = new DetailedAddressInfo(new Coordinates(lat, lng), null, null, null);
//
//    Venue mockExistingVenue = Venue.builder()
//            .facebookUrl("oldFacebook")
//            .websiteUrl("oldWebsite")
//            .phoneNumber("1231231234")
//            .address("oldAddress").build();
//
//    @Test
//    @DisplayName("should only update phone number if includeUrls is false")
//    void shouldOnlyUpdatePhoneNumberIfIncludeUrlsIsFalse() {
//
//      when(venueRepository.findByVenueNameAndLatitudeAndLongitude(venueName, lat, lng)).thenReturn(Optional.of(mockExistingVenue));
//      when(mapsService.getDetailedAddressInfo(address)).thenReturn(addressInfo);
//
//      Venue result = venueService.upsertVenue(venueInfo, false);
//
//      verify(venueRepository).save(mockExistingVenue);
//      assertEquals("oldFacebook",  result.getFacebookUrl());
//      assertEquals("oldWebsite", result.getWebsiteUrl());
//      assertEquals("1234561234",  result.getPhoneNumber());
//      assertNull(result.getInstagramUrl());
//      assertEquals("oldAddress",  result.getAddress());
//    }
//  }
//
//
//}
