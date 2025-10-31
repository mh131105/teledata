package com.acme.telemetry.api;

import com.acme.telemetry.api.dto.ClickStatsResponse;
import com.acme.telemetry.api.dto.MetricsSummaryResponse;
import com.acme.telemetry.api.dto.MetricsSummaryResponse.ClickItem;
import com.acme.telemetry.api.dto.MetricsSummaryResponse.SessionItem;
import com.acme.telemetry.api.dto.SessionStatsResponse;
import com.acme.telemetry.metrics.SessionStat;
import com.acme.telemetry.metrics.StorageClient;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/metrics")
public class MetricsController {

  private final StorageClient storage;

  public MetricsController(StorageClient storage) {
    this.storage = storage;
  }

  // Ex.: GET /v1/metrics/clicks?siteKey=sk_dev&elementKey=btn_buy
  @GetMapping("/clicks")
  public ClickStatsResponse getClicks(
      @RequestParam String siteKey,
      @RequestParam String elementKey
  ) {
    long total = storage.getClicks(siteKey, elementKey);
    return new ClickStatsResponse(siteKey, elementKey, total);
  }

  // Ex.: GET /v1/metrics/session?siteKey=sk_dev&pvId=pv1
  @GetMapping("/session")
  public SessionStatsResponse getSession(
      @RequestParam String siteKey,
      @RequestParam String pvId
  ) {
    SessionStat stat = storage.getSession(siteKey, pvId);
    long avg = stat.avgMs();
    return new SessionStatsResponse(siteKey, pvId, stat.totalMs(), stat.count(), avg);
  }

    // NOVO: resumo por site
  @GetMapping("/summary")
  public MetricsSummaryResponse summary(@RequestParam String siteKey) {
    Map<String, Long> clicksByElement = storage.findClicksBySite(siteKey);
    Map<String, SessionStat> sessionsByPv = storage.findSessionsBySite(siteKey);

    List<ClickItem> clicks = new ArrayList<>();
    clicksByElement.forEach((elementKey, total) -> clicks.add(new ClickItem(elementKey, total)));

    List<SessionItem> sessions = new ArrayList<>();
    sessionsByPv.forEach((pvId, stat) ->
        sessions.add(new SessionItem(pvId, stat.totalMs(), stat.count(), stat.avgMs())));

    return new MetricsSummaryResponse(siteKey, clicks, sessions);
  }
}
