package com.thelocalmusicfinder.thelocalmusicfinderbackend.dto.band;

import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GetBandsDTO {
  Map<String, BandDTO> bands;
}
