package com.thelocalmusicfinder.thelocalmusicfinderbackend.controllers;

import com.thelocalmusicfinder.thelocalmusicfinderbackend.dto.band.AddVideoRequestDTO;
import com.thelocalmusicfinder.thelocalmusicfinderbackend.dto.band.BandDTO;
import com.thelocalmusicfinder.thelocalmusicfinderbackend.dto.band.BandWithEventsDTO;
import com.thelocalmusicfinder.thelocalmusicfinderbackend.dto.band.SearchBandsResponseDTO;
import com.thelocalmusicfinder.thelocalmusicfinderbackend.mappers.BandMapper;
import com.thelocalmusicfinder.thelocalmusicfinderbackend.mappers.TopLevelBandMapper;
import com.thelocalmusicfinder.thelocalmusicfinderbackend.models.Band;
import com.thelocalmusicfinder.thelocalmusicfinderbackend.services.BandService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/bands")
@RequiredArgsConstructor
public class BandController {
  private final BandService bandService;
  private final BandMapper bandMapper;
  private final TopLevelBandMapper topLevelBandMapper;

  @GetMapping("/search/{bandNameQuery}")
  public ResponseEntity<SearchBandsResponseDTO> searchBands(@PathVariable String bandNameQuery) {
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
