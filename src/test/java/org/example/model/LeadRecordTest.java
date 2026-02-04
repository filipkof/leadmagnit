package org.example.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class LeadRecordTest {

  @Test
  void fromRequestCopiesFields() {
    LeadRequest request = new LeadRequest("Ivan", "+79991234567", "dr.cash", "buyer", "offer");
    LeadRecord record = LeadRecord.fromRequest(request);

    assertEquals("Ivan", record.name());
    assertEquals("+79991234567", record.phone());
    assertEquals("dr.cash", record.platform());
    assertEquals("buyer", record.buyer());
    assertEquals("offer", record.offer());
  }

  @Test
  void fromRequestReturnsNullWhenRequestIsNull() {
    assertNull(LeadRecord.fromRequest(null));
  }

  @Test
  void isInvalidReturnsTrueWhenRequiredFieldsMissing() {
    LeadRecord noName = new LeadRecord(null, "123", "dr.cash", null, null);
    LeadRecord noPhone = new LeadRecord("Ivan", " ", "dr.cash", null, null);
    LeadRecord noPlatform = new LeadRecord("Ivan", "123", "", null, null);

    assertTrue(noName.isInvalid());
    assertTrue(noPhone.isInvalid());
    assertTrue(noPlatform.isInvalid());
  }

  @Test
  void isInvalidReturnsFalseForValidRecord() {
    LeadRecord record = new LeadRecord("Ivan", "123", "dr.cash", null, null);
    assertFalse(record.isInvalid());
  }

  @Test
  void shortFormatPrintsRawValues() {
    LeadRecord record = new LeadRecord("Ivan", "+7999", "dr.cash", null, null);
    assertEquals("name=Ivan, phone=+7999, platform=dr.cash", record.shortFormat());
  }
}
