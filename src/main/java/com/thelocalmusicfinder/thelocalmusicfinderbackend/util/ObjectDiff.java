package com.thelocalmusicfinder.thelocalmusicfinderbackend.util;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ObjectDiff {

  public static List<String> getObjectDiffs(Object obj1, Object obj2) {
    if (obj1 == null || obj2 == null) {
      return List.of();
    }

    Class<?> cls = obj1.getClass();
    if (!cls.equals(obj2.getClass())) {
      return List.of();
    }

    List<String> fieldDiffs = new ArrayList<>();

    for (Field field : cls.getDeclaredFields()) {
      field.setAccessible(true);

      try {
        Object v1 = field.get(obj1);
        Object v2 = field.get(obj2);

        String s1 = (v1 == null ? null : v1.toString());
        String s2 = (v2 == null ? null : v2.toString());

        if (!Objects.equals(s1, s2)) {
          fieldDiffs.add(field.getName() + ": " + s1 + " ---> " + s2);
        }
      } catch (IllegalAccessException e) {
        throw new RuntimeException(e);
      }
    }

    return fieldDiffs;
  }
}
