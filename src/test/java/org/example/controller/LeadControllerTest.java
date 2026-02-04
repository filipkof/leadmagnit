package org.example.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.example.model.LeadRequest;
import org.example.service.LeadService;
import org.example.service.LeadService.Status;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

class LeadControllerTest {

  @Test
  void createLeadReturnsOkOnSuccess() {
    LeadService service = mock(LeadService.class);
    when(service.submit(new LeadRequest("Ivan", "123", "dr.cash", "buyer", "offer")))
        .thenReturn(new LeadService.SubmissionResult(Status.OK, null));

    LeadController controller = new LeadController(service);
    ResponseEntity<String> response = controller.createLead(
        new LeadRequest("Ivan", "123", "dr.cash", "buyer", "offer")
    );

    assertEquals(HttpStatus.OK, response.getStatusCode());
  }

  @Test
  void createLeadReturnsBadRequestWhenDbFailed() {
    LeadService service = mock(LeadService.class);
    when(service.submit(new LeadRequest("Ivan", "123", "dr.cash", "buyer", "offer")))
        .thenReturn(new LeadService.SubmissionResult(Status.DB_FAILED, null));

    LeadController controller = new LeadController(service);
    ResponseEntity<String> response = controller.createLead(
        new LeadRequest("Ivan", "123", "dr.cash", "buyer", "offer")
    );

    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    assertEquals("Не удалось сохранить данные в БД", response.getBody());
  }

  @Test
  void createLeadReturnsBadRequestWhenPlatformFailed() {
    LeadService service = mock(LeadService.class);
    when(service.submit(new LeadRequest("Ivan", "123", "dr.cash", "buyer", "offer")))
        .thenReturn(new LeadService.SubmissionResult(Status.PLATFORM_FAILED, "dr.cash"));

    LeadController controller = new LeadController(service);
    ResponseEntity<String> response = controller.createLead(
        new LeadRequest("Ivan", "123", "dr.cash", "buyer", "offer")
    );

    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    assertEquals("Лид не получилось передать в dr.cash", response.getBody());
  }
}
