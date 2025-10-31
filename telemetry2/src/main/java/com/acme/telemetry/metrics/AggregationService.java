package com.acme.telemetry.metrics;

import com.acme.telemetry.api.dto.EventDTO;
import java.util.List;

// Contrato de serviço que aplica eventos válidos nas métricas.
public interface AggregationService {
  void applyAll(List<EventDTO> events);
}
