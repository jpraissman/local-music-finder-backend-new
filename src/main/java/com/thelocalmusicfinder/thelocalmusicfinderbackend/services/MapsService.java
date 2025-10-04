package com.thelocalmusicfinder.thelocalmusicfinderbackend.services;

import com.thelocalmusicfinder.thelocalmusicfinderbackend.domain.maps.DetailedAddressInfo;
import com.thelocalmusicfinder.thelocalmusicfinderbackend.ports.driven.ForFetchingMapInfo;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MapsService {
  private final ForFetchingMapInfo mapsService;

  public DetailedAddressInfo getDetailedAddressInfo(String address) {
    return mapsService.getDetailedAddressInfo(address);
  }
}
