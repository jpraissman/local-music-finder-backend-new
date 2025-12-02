package com.thelocalmusicfinder.thelocalmusicfinderbackend.dto.band;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SearchBandsResponseDTO {
  List<BandDTO> bands;
}
