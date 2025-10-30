package com.acme.telemetry.service.validation;

import java.util.List;

import com.acme.telemetry.api.dto.ErrorDetail;
import com.acme.telemetry.api.dto.EventDTO;

/**
 * Contrato comum para validadores por tipo de evento.
 */
public interface EventTypeValidator {
  // nome do tipo tratado: "page_start", "page_end" ou "click"
  String getType();

  // valida regras específicas do tipo
  List<ErrorDetail> validate(EventDTO e, int index);
}