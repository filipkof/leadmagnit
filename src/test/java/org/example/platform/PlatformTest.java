package org.example.platform;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class PlatformTest {

  @Test
  void fromCodeMatchesIgnoringCaseAndWhitespace() {
    assertTrue(Platform.fromCode("dr.cash").isPresent());
    assertTrue(Platform.fromCode(" DR.CASH ").isPresent());
    assertEquals(Platform.DR_CASH, Platform.fromCode("dr.cash").orElseThrow());
  }

  @Test
  void fromCodeReturnsEmptyForNullOrUnknown() {
    assertTrue(Platform.fromCode(null).isEmpty());
    assertTrue(Platform.fromCode("unknown").isEmpty());
    assertFalse(Platform.fromCode("unknown").isPresent());
  }
}
