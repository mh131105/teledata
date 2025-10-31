package com.acme.telemetry.metrics;

import org.springframework.stereotype.Component;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;
import java.util.LinkedHashMap;

@Component
public class InMemoryStorageClient implements StorageClient {

  private final ConcurrentHashMap<String, Long> clicks = new ConcurrentHashMap<>();
  private final ConcurrentHashMap<String, SessionAgg> sessions = new ConcurrentHashMap<>();

  @Override
  public void incrementClick(String siteKey, String elementKey) {
    clicks.merge(keyClick(siteKey, elementKey), 1L, Long::sum);
  }

  @Override
  public void addSessionDuration(String siteKey, String pvId, long durationMs) {
    sessions.compute(keySession(siteKey, pvId), (k, agg) ->
        (agg == null) ? new SessionAgg(durationMs, 1) : new SessionAgg(agg.totalMs + durationMs, agg.count + 1));
  }

  @Override
  public long getClicks(String siteKey, String elementKey) {
    return clicks.getOrDefault(keyClick(siteKey, elementKey), 0L);
  }

  @Override
  public SessionStat getSession(String siteKey, String pvId) {
    SessionAgg agg = sessions.get(keySession(siteKey, pvId));
    return (agg == null) ? new SessionStat(0, 0) : new SessionStat(agg.totalMs, agg.count);
  }

  // ===== NOVOS MÉTODOS =====
  @Override
  public Map<String, Long> findClicksBySite(String siteKey) {
    String prefix = siteKey + "|";
    Map<String, Long> out = new LinkedHashMap<>();
    clicks.forEach((k, v) -> {
      if (k.startsWith(prefix)) {
        String elementKey = k.substring(prefix.length());
        out.put(elementKey, v);
      }
    });
    return out;
  }

  @Override
  public Map<String, SessionStat> findSessionsBySite(String siteKey) {
    String prefix = siteKey + "|";
    Map<String, SessionStat> out = new LinkedHashMap<>();
    sessions.forEach((k, agg) -> {
      if (k.startsWith(prefix)) {
        String pvId = k.substring(prefix.length());
        out.put(pvId, new SessionStat(agg.totalMs, agg.count));
      }
    });
    return out;
  }

  @Override
  public void resetAll() {
    clicks.clear();
    sessions.clear();
  }

  private String keyClick(String siteKey, String elementKey) { return siteKey + "|" + elementKey; }
  private String keySession(String siteKey, String pvId) { return siteKey + "|" + pvId; }

  private record SessionAgg(long totalMs, long count) {}
}
