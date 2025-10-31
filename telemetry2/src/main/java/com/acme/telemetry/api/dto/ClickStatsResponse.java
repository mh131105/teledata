package com.acme.telemetry.api.dto;

// Resposta de leitura de cliques por siteKey + elementKey
public record ClickStatsResponse(
    String siteKey,
    String elementKey,
    long totalClicks
) {}