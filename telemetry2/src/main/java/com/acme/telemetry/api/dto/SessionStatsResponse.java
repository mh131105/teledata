package com.acme.telemetry.api.dto;

// Resposta de leitura de sessão por siteKey + pvId (total, contagem e média)
public record SessionStatsResponse(
    String siteKey,
    String pvId,
    long totalMs,
    long count,
    long avgMs
) {}