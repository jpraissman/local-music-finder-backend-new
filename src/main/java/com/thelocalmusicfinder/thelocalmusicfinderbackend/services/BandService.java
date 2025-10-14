package com.thelocalmusicfinder.thelocalmusicfinderbackend.services;

import com.thelocalmusicfinder.thelocalmusicfinderbackend.domain.band.BandType;
import com.thelocalmusicfinder.thelocalmusicfinderbackend.domain.band.BasicBandInfo;
import com.thelocalmusicfinder.thelocalmusicfinderbackend.errors.exceptions.BandNotFound;
import com.thelocalmusicfinder.thelocalmusicfinderbackend.models.Band;
import com.thelocalmusicfinder.thelocalmusicfinderbackend.repositories.BandRepository;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BandService {

  private final BandRepository bandRepository;

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

}
