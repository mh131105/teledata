package com.acme.telemetry.metrics;

// Contrato para gravar/ler métricas. Implementações podem variar.
public interface StorageClient {
  void incrementClick(String siteKey, String elementKey);
  void addSessionDuration(String siteKey, String pvId, long durationMs);

  long getClicks(String siteKey, String elementKey);
  SessionStat getSession(String siteKey, String pvId);

  void resetAll();
}
