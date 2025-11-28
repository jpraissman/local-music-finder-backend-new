package com.thelocalmusicfinder.thelocalmusicfinderbackend.mappers;

import com.thelocalmusicfinder.thelocalmusicfinderbackend.models.Band;
import com.thelocalmusicfinder.thelocalmusicfinderbackend.models.Event;
import com.thelocalmusicfinder.thelocalmusicfinderbackend.models.Venue;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class CsvMapper {

  public String toEventsCsv(List<Event> events) {
    StringBuilder sb = new StringBuilder();

    sb.append(String.join(",",
            "eventId",
            "eventCode",
            "bandId",
            "bandName",
            "bandType",
            "tributeBandName",
            "bandGenres",
            "venueId",
            "venueName",
            "address",
            "town",
            "county",
            "createdAt",
            "email",
            "eventDate",
            "startTime",
            "endTime",
            "coverCharge",
            "additionalInfo",
            "eventCreator"
    )).append("\n");

    for (Event e : events) {
      sb.append(escape(e.getId()))
              .append(",").append(escape(e.getEventCode()))
              .append(",").append(escape(e.getBand().getId()))
              .append(",").append(escape(e.getBand().getBandName()))
              .append(",").append(escape(e.getBand().getBandType()))
              .append(",").append(escape(e.getBand().getTributeBandName()))
              .append(",").append(escape(listToString(e.getBand().getGenres())))
              .append(",").append(escape(e.getVenue().getId()))
              .append(",").append(escape(e.getVenue().getVenueName()))
              .append(",").append(escape(e.getVenue().getLocation().getFormattedAddress()))
              .append(",").append(escape(e.getVenue().getLocation().getTown()))
              .append(",").append(escape(e.getVenue().getLocation().getCounty()))
              .append(",").append(escape(e.getCreatedAt()))
              .append(",").append(escape(e.getEmail()))
              .append(",").append(escape(e.getEventDate()))
              .append(",").append(escape(e.getStartTime()))
              .append(",").append(escape(e.getEndTime()))
              .append(",").append(escape(e.getCoverCharge()))
              .append(",").append(escape(e.getAdditionalInfo()))
              .append(",").append(escape(e.getEventCreator()))
              .append("\n");
    }

    return sb.toString();
  }

  public String toBandsCsv(List<Band> bands) {
    StringBuilder sb = new StringBuilder();

    sb.append(String.join(",",
            "bandId",
            "bandName",
            "bandType",
            "genres",
            "tributeBandName",
            "facebookUrl",
            "instagramUrl",
            "websiteUrl"
    )).append("\n");

    for (Band b : bands) {
      sb.append(escape(b.getId()))
              .append(",").append(escape(b.getBandName()))
              .append(",").append(escape(b.getBandType()))
              .append(",").append(escape(listToString(b.getGenres())))
              .append(",").append(escape(b.getTributeBandName()))
              .append(",").append(escape(b.getFacebookUrl()))
              .append(",").append(escape(b.getInstagramUrl()))
              .append(",").append(escape(b.getWebsiteUrl()))
              .append("\n");
    }

    return sb.toString();
  }

  public String toVenuesCsv(List<Venue> venues) {
    StringBuilder sb = new StringBuilder();

    sb.append(String.join(",",
            "venueId",
            "venueName",
            "address",
            "county",
            "town",
            "phoneNumber",
            "facebookUrl",
            "instagramUrl",
            "websiteUrl"
    )).append("\n");

    for (Venue v : venues) {
      sb.append(escape(v.getId()))
              .append(",").append(escape(v.getVenueName()))
              .append(",").append(escape(v.getLocation().getFormattedAddress()))
              .append(",").append(escape(v.getLocation().getCounty()))
              .append(",").append(escape(v.getLocation().getTown()))
              .append(",").append(escape(v.getPhoneNumber()))
              .append(",").append(escape(v.getFacebookUrl()))
              .append(",").append(escape(v.getInstagramUrl()))
              .append(",").append(escape(v.getWebsiteUrl()))
              .append("\n");
    }

    return sb.toString();
  }

  private String escape(Object value) {
    if (value == null) return "";
    String s = value.toString();
    if (s.contains(",") || s.contains("\"")) {
      s = s.replace("\"", "\"\"");
      s = "\"" + s + "\"";
    }
    return s;
  }

  private String listToString(List<?> list) {
    if (list == null) return "";
    return list.stream()
            .map(x -> x.toString().replace(",", ";"))
            .collect(Collectors.joining(";"));
  }
}
