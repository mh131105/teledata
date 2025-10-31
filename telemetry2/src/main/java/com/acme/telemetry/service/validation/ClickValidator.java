package com.acme.telemetry.service.validation;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.acme.telemetry.api.dto.ErrorDetail;
import com.acme.telemetry.api.dto.EventDTO;

/**
 * Regras específicas de click.
 */
@Component
public class ClickValidator extends BaseEventTypeValidator {

  @Override
  public String getType() { return "click"; }

  @Override
  public List<ErrorDetail> validate(EventDTO e, int index) {
    List<ErrorDetail> errors = new ArrayList<>();
    // element_key obrigatório nas propriedades
    String elementKey = getString(e.properties(), "element_key");
    if (isBlank(elementKey)) {
      errors.add(err(index, "properties.element_key", "REQUIRED", "element_key é obrigatório"));
    }
    return errors;
  }
}