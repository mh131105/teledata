package com.acme.telemetry.metrics;

// Estatísticas simples de sessão.
public record SessionStat(long totalMs, long count) {
  public long avgMs() { return count == 0 ? 0 : totalMs / count; }
}
