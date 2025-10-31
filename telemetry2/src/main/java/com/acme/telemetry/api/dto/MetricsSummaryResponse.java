package com.acme.telemetry.api.dto;

import java.util.List;

// Resumo por site: cliques e sessões.
public record MetricsSummaryResponse(
    String siteKey,
    List<ClickItem> clicks,
    List<SessionItem> sessions
) {
  public record ClickItem(String elementKey, long total) {}
  public record SessionItem(String pvId, long totalMs, long count, long avgMs) {}
}