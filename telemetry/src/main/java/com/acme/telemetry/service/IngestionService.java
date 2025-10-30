package com.acme.telemetry.service;

import com.acme.telemetry.api.dto.CollectResponse;
import com.acme.telemetry.api.dto.ErrorDetail;
import com.acme.telemetry.api.dto.EventDTO;
import com.acme.telemetry.metrics.AggregationService;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class IngestionService {

  private final EventValidator eventValidator;
  private final AggregationService aggregationService;
  private final int maxBatchSize = 1000; // limite simples

  public IngestionService(EventValidator eventValidator,
                          AggregationService aggregationService) {
    this.eventValidator = eventValidator;
    this.aggregationService = aggregationService;
  }

  public CollectResponse processBatch(List<EventDTO> events) {
    if (events == null) return new CollectResponse(0, 0, Collections.emptyList());
    if (events.size() > maxBatchSize) {
      var err = new ErrorDetail(-1, "batch", "TOO_LARGE", "lote excede " + maxBatchSize + " eventos");
      return new CollectResponse(0, 0, List.of(err));
    }
    List<EventDTO> validEvents = new ArrayList<>();
    List<ErrorDetail> errors = new ArrayList<>();
    for (int i = 0; i < events.size(); i++) {
      var e = events.get(i);
      if (e == null) { errors.add(new ErrorDetail(i, "item", "NULL", "evento nulo")); continue; }
      var itemErrors = eventValidator.validate(e, i);
      if (itemErrors != null && !itemErrors.isEmpty()) {
        errors.addAll(itemErrors);
        continue;
      }
      validEvents.add(e);
    }
    aggregationService.applyAll(validEvents);
    return errors.isEmpty()
      ? new CollectResponse(validEvents.size(), 0, Collections.emptyList())
      : new CollectResponse(validEvents.size(), 0, errors);
  }
}
