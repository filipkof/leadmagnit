package org.example.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;
import org.example.model.LeadRequest;
import org.example.platform.Platform;
import org.example.platform.PlatformClient;
import org.example.service.LeadService.Status;
import org.junit.jupiter.api.Test;

class LeadServiceTest {

  @Test
  void submitReturnsOkWhenClientSends() {
    PlatformClient client = mock(PlatformClient.class);
    when(client.platform()).thenReturn(Platform.DR_CASH);
    when(client.sendLead(any())).thenReturn(true);

    LeadService service = new LeadService(List.of(client));
    LeadService.SubmissionResult result = service.submit(
        new LeadRequest("Ivan", "123", "dr.cash", "buyer", "offer")
    );

    assertEquals(Status.OK, result.status());
    assertNull(result.platform());
  }

  @Test
  void submitReturnsDbFailedWhenRecordInvalid() {
    PlatformClient client = mock(PlatformClient.class);
    when(client.platform()).thenReturn(Platform.DR_CASH);
    when(client.sendLead(any())).thenReturn(true);

    LeadService service = new LeadService(List.of(client));
    LeadService.SubmissionResult result = service.submit(
        new LeadRequest("Ivan", null, "dr.cash", "buyer", "offer")
    );

    assertEquals(Status.DB_FAILED, result.status());
  }

  @Test
  void submitReturnsPlatformFailedWhenPlatformUnknown() {
    PlatformClient client = mock(PlatformClient.class);
    when(client.platform()).thenReturn(Platform.DR_CASH);
    when(client.sendLead(any())).thenReturn(true);

    LeadService service = new LeadService(List.of(client));
    LeadService.SubmissionResult result = service.submit(
        new LeadRequest("Ivan", "123", "unknown", "buyer", "offer")
    );

    assertEquals(Status.PLATFORM_FAILED, result.status());
    assertEquals("unknown", result.platform());
  }

  @Test
  void submitReturnsPlatformFailedWhenClientMissing() {
    LeadService service = new LeadService(List.of());
    LeadService.SubmissionResult result = service.submit(
        new LeadRequest("Ivan", "123", "dr.cash", "buyer", "offer")
    );

    assertEquals(Status.PLATFORM_FAILED, result.status());
    assertEquals("dr.cash", result.platform());
  }

  @Test
  void submitReturnsPlatformFailedWhenClientSendFails() {
    PlatformClient client = mock(PlatformClient.class);
    when(client.platform()).thenReturn(Platform.DR_CASH);
    when(client.sendLead(any())).thenReturn(false);

    LeadService service = new LeadService(List.of(client));
    LeadService.SubmissionResult result = service.submit(
        new LeadRequest("Ivan", "123", "dr.cash", "buyer", "offer")
    );

    assertEquals(Status.PLATFORM_FAILED, result.status());
    assertEquals("dr.cash", result.platform());
  }

  @Test
  void submitAcceptsPlatformIgnoringCase() {
    PlatformClient client = mock(PlatformClient.class);
    when(client.platform()).thenReturn(Platform.DR_CASH);
    when(client.sendLead(any())).thenReturn(true);

    LeadService service = new LeadService(List.of(client));
    LeadService.SubmissionResult result = service.submit(
        new LeadRequest("Ivan", "123", "DR.CASH", "buyer", "offer")
    );

    assertEquals(Status.OK, result.status());
  }
}
