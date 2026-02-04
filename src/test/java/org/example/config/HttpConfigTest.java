package org.example.config;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

class HttpConfigTest {

  @Test
  void restTemplateBeanIsCreated() {
    HttpConfig config = new HttpConfig();
    assertNotNull(config.restTemplate());
  }
}
