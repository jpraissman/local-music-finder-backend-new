package com.thelocalmusicfinder.thelocalmusicfinderbackend.controllers;

import com.thelocalmusicfinder.thelocalmusicfinderbackend.dto.band.AddVideoRequestDTO;
import com.thelocalmusicfinder.thelocalmusicfinderbackend.dto.band.BandDTO;
import com.thelocalmusicfinder.thelocalmusicfinderbackend.dto.band.BandWithEventsDTO;
import com.thelocalmusicfinder.thelocalmusicfinderbackend.dto.band.SearchBandsResponseDTO;
import com.thelocalmusicfinder.thelocalmusicfinderbackend.mappers.BandMapper;
import com.thelocalmusicfinder.thelocalmusicfinderbackend.mappers.TopLevelBandMapper;
import com.thelocalmusicfinder.thelocalmusicfinderbackend.models.Band;
import com.thelocalmusicfinder.thelocalmusicfinderbackend.services.BandService;
import com.thelocalmusicfinder.thelocalmusicfinderbackend.services.LoggerService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/bands")
@RequiredArgsConstructor
public class BandController {
  private final BandService bandService;
  private final BandMapper bandMapper;
  private final TopLevelBandMapper topLevelBandMapper;
  private final LoggerService logger;

  @GetMapping()
  public ResponseEntity<?> deprecatedGetBandsEndpoint(HttpServletRequest request) {
    logger.warn("Deprecated endpoint /api/band hit by: " + request.getHeader("User-Agent"));
    return ResponseEntity.status(HttpStatus.GONE).body("Deprecated");
  }

  @GetMapping("/search")
  public ResponseEntity<SearchBandsResponseDTO> searchBands(
          @RequestParam(required = false) String bandNameQuery) {
    if (bandNameQuery == null || bandNameQuery.length() < 2) {
      return ResponseEntity.status(HttpStatus.OK).body(new SearchBandsResponseDTO(List.of()));
    }

    List<Band> bands = bandService.searchBands(bandNameQuery);

    List<BandDTO> bandDTOs = new ArrayList<>();
    for (Band band : bands) {
      bandDTOs.add(bandMapper.toBandDTO(band));
    }

    return ResponseEntity.status(HttpStatus.OK).body(new SearchBandsResponseDTO(bandDTOs));
  }

  @GetMapping("/{id}")
  public ResponseEntity<BandWithEventsDTO> getBand(@PathVariable Long id) {
    Band band = bandService.getBand(id);
    return ResponseEntity.status(HttpStatus.OK).body(topLevelBandMapper.toBandWithEventsDTO(band));
  }

  @PostMapping("/{id}/add-video")
  public ResponseEntity<Void> addBandVideo(@PathVariable Long id, @Valid @RequestBody AddVideoRequestDTO addVideoRequest) {
    bandService.addBandVideo(id, addVideoRequest.getYoutubeUrl());
    return  ResponseEntity.ok().build();
  }
}
