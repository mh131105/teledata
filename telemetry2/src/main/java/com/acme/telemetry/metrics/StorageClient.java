package com.acme.telemetry.metrics;

import java.util.Map;

// Contrato agora expõe leitura pontual e leitura agregada por site.
public interface StorageClient {
  void incrementClick(String siteKey, String elementKey);
  void addSessionDuration(String siteKey, String pvId, long durationMs);

  long getClicks(String siteKey, String elementKey);
  SessionStat getSession(String siteKey, String pvId);

  // NOVO: listar tudo por site (para summary)
  Map<String, Long> findClicksBySite(String siteKey);        // elementKey -> total
  Map<String, SessionStat> findSessionsBySite(String siteKey); // pvId -> stat

  void resetAll();
}
