package org.example.service;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.example.model.LeadRecord;
import org.example.model.LeadRequest;
import org.example.platform.Platform;
import org.example.platform.PlatformClient;
import org.example.storage.LeadDatabase;
import org.springframework.stereotype.Service;

@Service
public class LeadService {
  private final Map<Platform, PlatformClient> clients;

  public LeadService(List<PlatformClient> clientList) {
    Map<Platform, PlatformClient> resolved = clientList.stream()
        .collect(Collectors.toMap(PlatformClient::platform, Function.identity()));
    EnumMap<Platform, PlatformClient> map = new EnumMap<>(Platform.class);
    map.putAll(resolved);
    this.clients = map;
  }

  public SubmissionResult submit(LeadRequest request) {
    LeadRecord record = LeadRecord.fromRequest(request);
    if (!LeadDatabase.INSTANCE.save(record)) {
      return SubmissionResult.dbFailed();
    }
    Platform platform = Platform.fromCode(record.platform()).orElse(null);
    if (platform == null) {
      return SubmissionResult.platformFailed(record.platform());
    }
    PlatformClient client = clients.get(platform);
    if (client == null || !client.sendLead(record)) {
      return SubmissionResult.platformFailed(record.platform());
    }
    return SubmissionResult.success();
  }

  public record SubmissionResult(Status status, String platform) {
    public static SubmissionResult success() {
      return new SubmissionResult(Status.OK, null);
    }

    public static SubmissionResult dbFailed() {
      return new SubmissionResult(Status.DB_FAILED, null);
    }

    public static SubmissionResult platformFailed(String platform) {
      return new SubmissionResult(Status.PLATFORM_FAILED, platform);
    }
  }

  public enum Status {
    OK,
    DB_FAILED,
    PLATFORM_FAILED
  }
}
