package org.example.storage;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.example.model.LeadRecord;
import org.junit.jupiter.api.Test;

class LeadDatabaseTest {

  @Test
  void saveReturnsFalseForNullRecord() {
    assertFalse(LeadDatabase.INSTANCE.save(null));
  }

  @Test
  void saveReturnsFalseForInvalidRecord() {
    LeadRecord invalid = new LeadRecord("", "123", "dr.cash", null, null);
    assertFalse(LeadDatabase.INSTANCE.save(invalid));
  }

  @Test
  void saveReturnsTrueForValidRecord() {
    LeadRecord valid = new LeadRecord("Ivan", "123", "dr.cash", null, null);
    assertTrue(LeadDatabase.INSTANCE.save(valid));
  }
}
