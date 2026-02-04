package org.example.platform;

import org.example.model.LeadRecord;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class DrCashClient implements PlatformClient {
  private static final Logger logger = LoggerFactory.getLogger(DrCashClient.class);
  private static final String ENDPOINT_URL = "https://order.drcash.sh/v1/order";
  private static final String STREAM_CODE = "509d9";
  private static final String AUTH_TOKEN =
      "YZVLYZLHMMMTYZI4ZC00NZFHLTKZZTMTMDA5Y2RIZDA2ZTA3";

  private final RestTemplate restTemplate;

  public DrCashClient(RestTemplate restTemplate) {
    this.restTemplate = restTemplate;
  }

  @Override
  public Platform platform() {
    return Platform.DR_CASH;
  }

  @Override
  public boolean sendLead(LeadRecord record) {
    if (record == null) {
      return false;
    }
    String shortData = record.shortFormat();
    DrCashOrderRequest body = new DrCashOrderRequest(
        STREAM_CODE,
        new DrCashClientInfo(record.phone(), record.name())
    );
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    headers.setBearerAuth(AUTH_TOKEN);
    HttpEntity<DrCashOrderRequest> entity = new HttpEntity<>(body, headers);
    try {
      ResponseEntity<String> response = restTemplate.exchange(
          ENDPOINT_URL,
          HttpMethod.POST,
          entity,
          String.class
      );
      if (response.getStatusCode() == HttpStatus.OK) {
        logger.info("Lead sent to dr.cash: {}", shortData);
        return true;
      }
      logger.warn("Lead send failed: status={} data={}", response.getStatusCode(), shortData);
      return false;
    } catch (RestClientException ex) {
      logger.warn("Lead send failed: exception={} data={}", ex.getClass().getSimpleName(), shortData);
      return false;
    }
  }

  private record DrCashOrderRequest(String stream_code, DrCashClientInfo client) {
  }

  private record DrCashClientInfo(String phone, String name) {
  }
}
