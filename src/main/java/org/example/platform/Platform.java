package org.example.platform;

import java.util.Arrays;
import java.util.Locale;
import java.util.Optional;

public enum Platform {
  DR_CASH("dr.cash");

  private final String code;

  Platform(String code) {
    this.code = code;
  }

  public String code() {
    return code;
  }

  public static Optional<Platform> fromCode(String value) {
    if (value == null) {
      return Optional.empty();
    }
    String normalized = value.trim().toLowerCase(Locale.ROOT);
    return Arrays.stream(values())
        .filter(platform -> platform.code.equals(normalized))
        .findFirst();
  }
}
