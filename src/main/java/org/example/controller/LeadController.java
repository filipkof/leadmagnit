package org.example.controller;

import org.example.model.LeadRequest;
import org.example.service.LeadService;
import org.example.service.LeadService.Status;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/lead")
public class LeadController {
  private final LeadService leadService;

  public LeadController(LeadService leadService) {
    this.leadService = leadService;
  }

  @PostMapping
  public ResponseEntity<String> createLead(@RequestBody LeadRequest request) {
    LeadService.SubmissionResult result = leadService.submit(request);
    if (result.status() == Status.OK) {
      return ResponseEntity.ok().build();
    }
    if (result.status() == Status.DB_FAILED) {
      return ResponseEntity.badRequest().body("Не удалось сохранить данные в БД");
    }
    String platform = result.platform();
    if (platform == null) {
      platform = request != null ? request.platform() : "null";
    }
    return ResponseEntity.badRequest().body("Лид не получилось передать в " + platform);
  }
}
