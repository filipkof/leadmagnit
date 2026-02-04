package org.example.storage;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import org.example.model.LeadRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public enum LeadDatabase {
  INSTANCE;

  private static final Logger logger = LoggerFactory.getLogger(LeadDatabase.class);
  private final List<LeadRecord> records = new CopyOnWriteArrayList<>();

  public boolean save(LeadRecord record) {
    if (record == null || record.isInvalid()) {
      logger.warn("Lead save failed: {}", LeadRecord.shortFormat(record));
      return false;
    }
    boolean saved = records.add(record);
    if (saved) {
      logger.info("Lead saved: {}", record.shortFormat());
    } else {
      logger.warn("Lead save failed: {}", record.shortFormat());
    }
    return saved;
  }
}
