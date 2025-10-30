package com.acme.telemetry.metrics;

import com.acme.telemetry.api.dto.EventDTO;
import org.springframework.stereotype.Service;
import java.util.List;

// Implementação padrão: despacha polimorficamente para agregadores.
@Service
public class DefaultAggregationService implements AggregationService {

  private final List<EventAggregator> aggregators;
  private final StorageClient store;

  public DefaultAggregationService(List<EventAggregator> aggregators, StorageClient store) {
    this.aggregators = aggregators;
    this.store = store;
  }

  @Override
  public void applyAll(List<EventDTO> events) {
    if (events == null || events.isEmpty()) return;
    for (EventDTO e : events) {
      if (e == null || e.type() == null) continue;
      for (EventAggregator ag : aggregators) {
        if (ag.supports(e.type())) {
          ag.apply(e, store); // polimorfismo em execução
          break;
        }
      }
    }
  }
}
