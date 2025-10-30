package com.acme.telemetry.api.dto;

import java.time.Instant;
import java.util.Map;

public record EventDTO(
  String event_id,
  String site_key,
  String type,            // "page_start" | "page_end" | "click"
  Instant timestamp,
  User user,
  String session_id,
  String pv_id,
  Context context,
  Map<String, Object> properties
) {
  public record User(String user_id) {}
  public record Context(String url, String referrer, String user_agent) {}
}