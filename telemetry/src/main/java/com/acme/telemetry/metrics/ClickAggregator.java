package com.acme.telemetry.metrics;

import com.acme.telemetry.api.dto.EventDTO;
import org.springframework.stereotype.Component;

// Agrega cliques por elemento.
@Component
public class ClickAggregator implements EventAggregator {
  @Override public boolean supports(String type) { return "click".equalsIgnoreCase(type); }

  @Override
  public void apply(EventDTO e, StorageClient store) {
    if (e.properties() == null) return;
    Object v = e.properties().get("element_key");
    if (v == null) return;
    store.incrementClick(e.site_key(), String.valueOf(v));
  }
}
