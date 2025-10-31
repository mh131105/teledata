package com.acme.telemetry.api.dto;

import java.util.List;

public record CollectResponse(
  int ingested,
  int duplicates,
  List<ErrorDetail> errors
) {}

