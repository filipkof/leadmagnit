package org.example.model;

public record LeadRequest(
    String name,
    String phone,
    String platform,
    String buyer,
    String offer
) {
}
