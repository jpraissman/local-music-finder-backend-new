package com.thelocalmusicfinder.thelocalmusicfinderbackend.util;

public class UtilFunctions {

  public static String stringOrNull(String string) {
    if (string != null && !string.trim().isEmpty()) {
      return string;
    } else {
      return null;
    }
  }
}
