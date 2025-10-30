package com.acme.telemetry.metrics;

import org.springframework.stereotype.Component;
import java.util.concurrent.ConcurrentHashMap;

// Implementação em memória para o MVP.
@Component
public class InMemoryStorageClient implements StorageClient {

  private final ConcurrentHashMap<String, Long> clicks = new ConcurrentHashMap<>();
  private final ConcurrentHashMap<String, SessionAgg> sessions = new ConcurrentHashMap<>();

  @Override
  public void incrementClick(String siteKey, String elementKey) {
    String key = siteKey + "|" + elementKey;
    clicks.merge(key, 1L, Long::sum);
  }

  @Override
  public void addSessionDuration(String siteKey, String pvId, long durationMs) {
    String key = siteKey + "|" + pvId;
    sessions.compute(key, (k, agg) -> {
      if (agg == null) return new SessionAgg(durationMs, 1);
      return new SessionAgg(agg.totalMs + durationMs, agg.count + 1);
    });
  }

  @Override
  public long getClicks(String siteKey, String elementKey) {
    return clicks.getOrDefault(siteKey + "|" + elementKey, 0L);
  }

  @Override
  public SessionStat getSession(String siteKey, String pvId) {
    SessionAgg agg = sessions.get(siteKey + "|" + pvId);
    return agg == null ? new SessionStat(0, 0) : new SessionStat(agg.totalMs, agg.count);
  }

  @Override
  public void resetAll() {
    clicks.clear();
    sessions.clear();
  }

  // Aggregado interno simples.
  private record SessionAgg(long totalMs, long count) {}
}
