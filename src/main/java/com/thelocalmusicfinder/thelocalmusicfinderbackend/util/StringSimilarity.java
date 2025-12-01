package com.thelocalmusicfinder.thelocalmusicfinderbackend.util;

import org.apache.commons.text.similarity.FuzzyScore;

import java.text.Normalizer;
import java.util.Locale;

public class StringSimilarity {

  /**
   * Returns a score to see how similar two strings are
   */
  public static double findSimilarity(String value1, String value2) {
    String value1Normalized = normalizeString(Normalizer.normalize(value1, Normalizer.Form.NFD));
    String value2Normalized = normalizeString(Normalizer.normalize(value2, Normalizer.Form.NFD));

    FuzzyScore score = new FuzzyScore(Locale.ENGLISH);
    return score.fuzzyScore(value1Normalized, value2Normalized);
  }

  private static String normalizeString(String value) {
    String normalized = Normalizer.normalize(value, Normalizer.Form.NFD);
    return normalized.replaceAll("\\p{M}", "") // remove accents
            .toLowerCase()
            .replaceAll("[^a-z0-9]", "");
  }
}
