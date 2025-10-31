package com.acme.telemetry.service.validation;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.acme.telemetry.api.dto.ErrorDetail;
import com.acme.telemetry.api.dto.EventDTO;

/**
 * Regras específicas de page_start.
 */
@Component
public class PageStartValidator extends BaseEventTypeValidator {

  @Override
  public String getType() { return "page_start"; }

  @Override
  public List<ErrorDetail> validate(EventDTO e, int index) {
    List<ErrorDetail> errors = new ArrayList<>();
    // pv_id é obrigatório para início de página
    req(errors, index, e.pv_id(), "pv_id");
    return errors;
  }
}