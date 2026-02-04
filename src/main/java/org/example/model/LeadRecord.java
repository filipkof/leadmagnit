package org.example.model;

public record LeadRecord(
    String name,
    String phone,
    String platform,
    String buyer,
    String offer
) {
  public static LeadRecord fromRequest(LeadRequest request) {
    if (request == null) {
      return null;
    }
    return new LeadRecord(
        request.name(),
        request.phone(),
        request.platform(),
        request.buyer(),
        request.offer()
    );
  }

  public boolean isInvalid() {
    return isBlank(name) || isBlank(phone) || isBlank(platform);
  }

  public String shortFormat() {
    return "name=" + valueOrNull(name)
        + ", phone=" + valueOrNull(phone)
        + ", platform=" + valueOrNull(platform);
  }

  public static String shortFormat(LeadRecord record) {
    return record == null ? "null" : record.shortFormat();
  }

  private static String valueOrNull(String value) {
    return value == null ? "null" : value;
  }

  private static boolean isBlank(String value) {
    return value == null || value.trim().isEmpty();
  }
}
