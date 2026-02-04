package org.example.platform;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import org.example.model.LeadRecord;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

class DrCashClientTest {
  private static final String ENDPOINT_URL = "https://order.drcash.sh/v1/order";
  private static final String AUTH_TOKEN =
      "YZVLYZLHMMMTYZI4ZC00NZFHLTKZZTMTMDA5Y2RIZDA2ZTA3";

  @Test
  void sendLeadReturnsFalseForNullRecord() {
    RestTemplate restTemplate = mock(RestTemplate.class);
    DrCashClient client = new DrCashClient(restTemplate);

    assertFalse(client.sendLead(null));
    verifyNoInteractions(restTemplate);
  }

  @Test
  void sendLeadReturnsTrueForOkResponse() {
    RestTemplate restTemplate = mock(RestTemplate.class);
    DrCashClient client = new DrCashClient(restTemplate);
    LeadRecord record = new LeadRecord("Ivan", "+7999", "dr.cash", null, null);

    ArgumentCaptor<HttpEntity> captor = ArgumentCaptor.forClass(HttpEntity.class);
    when(restTemplate.exchange(eq(ENDPOINT_URL), eq(HttpMethod.POST), captor.capture(), eq(String.class)))
        .thenReturn(new ResponseEntity<>("", HttpStatus.OK));

    assertTrue(client.sendLead(record));

    HttpEntity entity = captor.getValue();
    assertNotNull(entity);
    assertEquals(MediaType.APPLICATION_JSON, entity.getHeaders().getContentType());
    assertEquals("Bearer " + AUTH_TOKEN, entity.getHeaders().getFirst(HttpHeaders.AUTHORIZATION));
    String bodyText = String.valueOf(entity.getBody());
    assertTrue(bodyText.contains("509d9"));
    assertTrue(bodyText.contains("+7999"));
    assertTrue(bodyText.contains("Ivan"));
  }

  @Test
  void sendLeadReturnsFalseForNonOkResponse() {
    RestTemplate restTemplate = mock(RestTemplate.class);
    DrCashClient client = new DrCashClient(restTemplate);
    LeadRecord record = new LeadRecord("Ivan", "+7999", "dr.cash", null, null);

    when(restTemplate.exchange(eq(ENDPOINT_URL), eq(HttpMethod.POST), org.mockito.ArgumentMatchers.any(), eq(String.class)))
        .thenReturn(new ResponseEntity<>("", HttpStatus.BAD_REQUEST));

    assertFalse(client.sendLead(record));
  }

  @Test
  void sendLeadReturnsFalseOnException() {
    RestTemplate restTemplate = mock(RestTemplate.class);
    DrCashClient client = new DrCashClient(restTemplate);
    LeadRecord record = new LeadRecord("Ivan", "+7999", "dr.cash", null, null);

    when(restTemplate.exchange(eq(ENDPOINT_URL), eq(HttpMethod.POST), org.mockito.ArgumentMatchers.any(), eq(String.class)))
        .thenThrow(new RestClientException("boom"));

    assertFalse(client.sendLead(record));
  }
}
