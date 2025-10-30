package com.acme.telemetry.metrics;

import com.acme.telemetry.api.dto.EventDTO;
import org.springframework.stereotype.Component;

// Agrega duração de sessão por pvId.
@Component
public class PageEndAggregator implements EventAggregator {
  @Override public boolean supports(String type) { return "page_end".equalsIgnoreCase(type); }

  @Override
  public void apply(EventDTO e, StorageClient store) {
    if (e.properties() == null) return;
    Object dur = e.properties().get("duration_ms");
    if (dur == null) return;
    long durationMs = (dur instanceof Number n) ? n.longValue() : parseLongSafe(dur.toString());
    if (durationMs < 0) return;
    store.addSessionDuration(e.site_key(), e.pv_id(), durationMs);
  }

  private long parseLongSafe(String s) {
    try { return Long.parseLong(s); } catch (Exception ex) { return -1; }
  }
}
