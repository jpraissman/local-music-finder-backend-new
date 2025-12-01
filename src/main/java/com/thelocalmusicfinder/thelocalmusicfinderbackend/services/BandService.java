package com.thelocalmusicfinder.thelocalmusicfinderbackend.services;

import com.thelocalmusicfinder.thelocalmusicfinderbackend.domain.band.BandType;
import com.thelocalmusicfinder.thelocalmusicfinderbackend.domain.band.BasicBandInfo;
import com.thelocalmusicfinder.thelocalmusicfinderbackend.errors.exceptions.BandNotFound;
import com.thelocalmusicfinder.thelocalmusicfinderbackend.errors.exceptions.InvalidYoutubeUrl;
import com.thelocalmusicfinder.thelocalmusicfinderbackend.mappers.BandMapper;
import com.thelocalmusicfinder.thelocalmusicfinderbackend.models.Band;
import com.thelocalmusicfinder.thelocalmusicfinderbackend.models.Venue;
import com.thelocalmusicfinder.thelocalmusicfinderbackend.repositories.BandRepository;
import com.thelocalmusicfinder.thelocalmusicfinderbackend.util.StringSimilarity;

import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BandService {

  private final BandRepository bandRepository;
  private final LoggerService loggerService;
  private final BandMapper bandMapper;
  private final EmailService emailService;
  private final LoggerService logger;

  public List<Band> getAllBands() {
    return bandRepository.findAll();
  }

  public Band upsertBand(BasicBandInfo bandInfo) {
    // Check data is correct
    if (bandInfo.getTributeBandName() == null
            && bandInfo.getBandType() == BandType.TRIBUTE_BAND) {
      throw new IllegalArgumentException("There must be a tributeBandName if BandType is TRIBUTE_BAND");
    }
    if (bandInfo.getTributeBandName() != null && bandInfo.getBandType() != BandType.TRIBUTE_BAND) {
      throw new IllegalArgumentException("BandType must be TRIBUTE_BAND if there is a tributeBandName");
    }

    // Check for existing band based on name
    String bandName = bandInfo.getBandName().trim();
    Optional<Band> existingBand = bandRepository.findByBandName(bandName);
    if (existingBand.isPresent()) {
      return this.updateBand(existingBand.get(), bandInfo);
    } else {
      return this.createBand(bandInfo);
    }
  }

  public Band updateBand(Band existingBand, BasicBandInfo updatedBandInfo) {
    emailService.sendBandUpdatedEmail(bandMapper.toBasicBand(existingBand), updatedBandInfo, existingBand.getId());

    String newBandName = updatedBandInfo.getBandName().trim();

    existingBand.setBandName(newBandName);
    existingBand.setBandType(updatedBandInfo.getBandType());
    existingBand.setTributeBandName(updatedBandInfo.getTributeBandName());
    existingBand.setGenres(updatedBandInfo.getGenres());
    existingBand.setFacebookUrl(updatedBandInfo.getFacebookUrl());
    existingBand.setInstagramUrl(updatedBandInfo.getInstagramUrl());
    existingBand.setWebsiteUrl(updatedBandInfo.getWebsiteUrl());

    Band savedBand  = bandRepository.save(existingBand);

    checkForDuplicates(newBandName);

    return savedBand;
  }

  private Band createBand(BasicBandInfo bandInfo) {
    String bandName =  bandInfo.getBandName().trim();

    Band band = Band.builder()
            .bandName(bandName)
            .bandType(bandInfo.getBandType())
            .tributeBandName(bandInfo.getTributeBandName())
            .genres(bandInfo.getGenres())
            .facebookUrl(bandInfo.getFacebookUrl())
            .instagramUrl(bandInfo.getInstagramUrl())
            .websiteUrl(bandInfo.getWebsiteUrl())
            .build();
    Band savedBand = bandRepository.save(band);

    checkForDuplicates(bandName);

    return savedBand;
  }

  private void checkForDuplicates(String bandName) {
    List<Band> allBands = bandRepository.findAll();
    List<Band> potentialDuplicateBands = new ArrayList<>();

    for (Band band : allBands) {
      double simScore = StringSimilarity.findSimilarity(bandName, band.getBandName());
      if (simScore > 20) {
        logger.info("Band with name " + band.getBandName() + " is potential duplicate to " + bandName + " with simScore of " + simScore);
        potentialDuplicateBands.add(band);
      }
    }

    if (potentialDuplicateBands.size() > 1) {
      logger.warn("Found " + potentialDuplicateBands.size() + " bands with similar names");
      for (Band band : potentialDuplicateBands) {
        logger.warn("Similarity: Band id " + band.getId() + ": " + band.getBandName());
      }
      emailService.sendDuplicateBandEmail(potentialDuplicateBands);
    }
  }

  public Band getBand(Long id) {
    Optional<Band> band = bandRepository.findById(id);
    if (band.isEmpty()) {
      throw new BandNotFound("Band with id " + id + " not found");
    }

    return band.get();
  }

  public void addBandVideo(Long id, String youtubeUrl) {
    Band band = getBand(id);
    String videoId = extractYouTubeVideoId(youtubeUrl);
    if (videoId == null) {
      loggerService.error("Error extracting youtube video id from " + youtubeUrl);
      throw new InvalidYoutubeUrl("Error extracting youtube video id from " + youtubeUrl);
    }
    if (band.getYoutubeVideoIds().contains(videoId)) {
      loggerService.error("Given youtubeUrl already exists for band. videoId: " + videoId);
      throw new InvalidYoutubeUrl("Given youtubeUrl already exists for band. videoId: " + videoId);
    }
    band.getYoutubeVideoIds().add(videoId);
    bandRepository.save(band);
  }

  private String extractYouTubeVideoId(String youtubeUrl) {
    try {
      URI uri = new URI(youtubeUrl);
      String host = uri.getHost();

      if (host != null && host.contains("youtube.com")) {
        String query = uri.getQuery();
        if (query != null) {
          for (String param : query.split("&")) {
            String[] pair = param.split("=");
            if (pair.length == 2 && pair[0].equals("v")) {
              return pair[1];
            }
          }
        }
      }
      else if (host != null && host.contains("youtu.be")) {
        return uri.getPath().replaceFirst("/", "");
      }

    } catch (URISyntaxException e) {
      loggerService.error("Error extracting youtube video id from " + youtubeUrl + ". Error message: " + e.getMessage());
      return null;
    }

    return null;
  }
}
