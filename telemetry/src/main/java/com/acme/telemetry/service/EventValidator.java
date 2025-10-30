package com.acme.telemetry.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.acme.telemetry.api.dto.ErrorDetail;
import com.acme.telemetry.api.dto.EventDTO;
import com.acme.telemetry.service.validation.EventTypeValidator;

/**
 * Orquestra a validação:
 * - Regras comuns para todos os eventos;
 * - Encaminha para o validador do tipo (polimorfismo).
 */
@Component
public class EventValidator {

  private final Map<String, EventTypeValidator> validatorsByType;

  public EventValidator(List<EventTypeValidator> validators) {
    // indexa por tipo: "page_start", "page_end", "click"
    Map<String, EventTypeValidator> map = new HashMap<>();
    for (EventTypeValidator v : validators) {
      map.put(v.getType().toLowerCase(Locale.ROOT), v);
    }
    this.validatorsByType = Collections.unmodifiableMap(map);
  }

  public List<ErrorDetail> validate(EventDTO e, int index) {
    System.out.println(">> PageStartValidator.validate chamado");
    List<ErrorDetail> errors = new ArrayList<>();

    if (e == null) {
      errors.add(new ErrorDetail(index, "item", "NULL", "evento nulo"));
      return errors;
    }

    // -------- Regras comuns --------
    req(errors, index, e.event_id(), "event_id");
    req(errors, index, e.site_key(), "site_key");
    req(errors, index, e.type(), "type");
    req(errors, index, e.timestamp(), "timestamp");

    if (e.user() == null || isBlank(e.user().user_id())) {
      errors.add(new ErrorDetail(index, "user.user_id", "REQUIRED", "user.user_id é obrigatório"));
    }
    req(errors, index, e.session_id(), "session_id");

    if (e.context() == null || isBlank(e.context().url())) {
      errors.add(new ErrorDetail(index, "context.url", "REQUIRED", "context.url é obrigatório"));
    }

    String type = e.type() == null ? null : e.type().toLowerCase(Locale.ROOT);

    // se já tem erro crítico ou type ausente, retorna
    if (!errors.isEmpty()) return errors;

    // -------- Polimorfismo: chama o validador do tipo --------
    EventTypeValidator specific = validatorsByType.get(type);
    if (specific == null) {
      errors.add(new ErrorDetail(index, "type", "INVALID", "type deve ser page_start | page_end | click"));
      return errors;
    }

    errors.addAll(specific.validate(e, index));
    System.out.println(">> PageEndValidator.validate chamado");
    return errors;
  }

  // ---- helpers locais (simples) ----

  private void req(List<ErrorDetail> errors, int idx, Object value, String field) {
    if (value == null || (value instanceof String s && isBlank(s))) {
      errors.add(new ErrorDetail(idx, field, "REQUIRED", field + " é obrigatório"));
    }
  }

  private boolean isBlank(String s) {
    return s == null || s.trim().isEmpty();
  }
}