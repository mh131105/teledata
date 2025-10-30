package com.acme.telemetry.api.dto;

public record ErrorDetail(
  int index,
  String field,
  String code,
  String message
) {}

