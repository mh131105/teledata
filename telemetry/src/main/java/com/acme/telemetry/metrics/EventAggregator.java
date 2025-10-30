package com.acme.telemetry.metrics;

import com.acme.telemetry.api.dto.EventDTO;

// Contrato para aplicar um evento em métricas.
public interface EventAggregator {
  boolean supports(String type);                     // ex.: "click", "page_end"
  void apply(EventDTO event, StorageClient store);   // escreve no StorageClient
}
