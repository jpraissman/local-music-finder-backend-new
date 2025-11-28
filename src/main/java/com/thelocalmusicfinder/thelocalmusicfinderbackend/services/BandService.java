package com.thelocalmusicfinder.thelocalmusicfinderbackend.services;

import com.thelocalmusicfinder.thelocalmusicfinderbackend.domain.band.BandType;
import com.thelocalmusicfinder.thelocalmusicfinderbackend.domain.band.BasicBandInfo;
import com.thelocalmusicfinder.thelocalmusicfinderbackend.dto.band.BandDTO;
import com.thelocalmusicfinder.thelocalmusicfinderbackend.errors.exceptions.BandNotFound;
import com.thelocalmusicfinder.thelocalmusicfinderbackend.errors.exceptions.InvalidYoutubeUrl;
import com.thelocalmusicfinder.thelocalmusicfinderbackend.models.Band;
import com.thelocalmusicfinder.thelocalmusicfinderbackend.repositories.BandRepository;

import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BandService {

  private final BandRepository bandRepository;
  private final LoggerService loggerService;

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

  private Band updateBand(Band existingBand, BasicBandInfo updatedBandInfo) {
    existingBand.setBandType(updatedBandInfo.getBandType());
    existingBand.setTributeBandName(updatedBandInfo.getTributeBandName());
    existingBand.setGenres(updatedBandInfo.getGenres());
    existingBand.setFacebookUrl(updatedBandInfo.getFacebookUrl());
    existingBand.setInstagramUrl(updatedBandInfo.getInstagramUrl());
    existingBand.setWebsiteUrl(updatedBandInfo.getWebsiteUrl());

    return bandRepository.save(existingBand);
  }

  private Band createBand(BasicBandInfo bandInfo) {
    Band band = Band.builder()
            .bandName(bandInfo.getBandName().trim())
            .bandType(bandInfo.getBandType())
            .tributeBandName(bandInfo.getTributeBandName())
            .genres(bandInfo.getGenres())
            .facebookUrl(bandInfo.getFacebookUrl())
            .instagramUrl(bandInfo.getInstagramUrl())
            .websiteUrl(bandInfo.getWebsiteUrl())
            .build();
    return bandRepository.save(band);
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

  public void editBand(BandDTO bandDTO) {
    Optional<Band> optionalBand = bandRepository.findById(bandDTO.getId());
    if (optionalBand.isEmpty()) {
      throw new BandNotFound("Band with id " + bandDTO.getId() + " not found");
    }

    Band band = optionalBand.get();
    band.setBandType(bandDTO.getBandType());
    band.setTributeBandName(bandDTO.getTributeBandName());
    band.setGenres(bandDTO.getGenres());
    band.setFacebookUrl(bandDTO.getFacebookUrl());
    band.setInstagramUrl(bandDTO.getInstagramUrl());
    band.setWebsiteUrl(bandDTO.getWebsiteUrl());
    band.setBandName(bandDTO.getBandName());
    bandRepository.save(band);
  }

}
