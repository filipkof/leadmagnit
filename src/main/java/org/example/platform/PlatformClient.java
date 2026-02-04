package org.example.platform;

import org.example.model.LeadRecord;

public interface PlatformClient {
  Platform platform();

  boolean sendLead(LeadRecord record);
}
