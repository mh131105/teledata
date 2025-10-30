package com.acme.telemetry.api;

import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.acme.telemetry.api.dto.CollectResponse;
import com.acme.telemetry.api.dto.EventDTO;
import com.acme.telemetry.service.IngestionService;

@RestController
@RequestMapping("/v1")
public class IngestionController {

  private final IngestionService ingestionService;

  public IngestionController(IngestionService ingestionService) {
    this.ingestionService = ingestionService;
  }

  @PostMapping(value = "/collect", consumes = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<CollectResponse> collect(@RequestBody List<EventDTO> events) {
    CollectResponse resp = ingestionService.processBatch(events);

    // ==> aqui entra a sua checagem de erros <==
    if (!resp.errors().isEmpty()) {
      return ResponseEntity.badRequest().body(resp); // 400
    }

    return ResponseEntity.accepted().body(resp); // 202
  }


  @GetMapping("/ping")
  public String ping() { return "pong"; }
}

