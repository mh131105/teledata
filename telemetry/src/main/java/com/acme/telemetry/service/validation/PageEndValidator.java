package com.acme.telemetry.service.validation;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.acme.telemetry.api.dto.ErrorDetail;
import com.acme.telemetry.api.dto.EventDTO;

/**
 * Regras específicas de page_end.
 */
@Component
public class PageEndValidator extends BaseEventTypeValidator {

  @Override
  public String getType() { return "page_end"; }

  @Override
  public List<ErrorDetail> validate(EventDTO e, int index) {
    List<ErrorDetail> errors = new ArrayList<>();
    // pv_id obrigatório
    req(errors, index, e.pv_id(), "pv_id");
    // duration_ms obrigatório e >= 0
    Long dur = getLong(e.properties(), "duration_ms");
    if (dur == null || dur < 0) {
      errors.add(err(index, "properties.duration_ms", "INVALID", "duration_ms deve ser >= 0"));
    }
    return errors;
  }
}