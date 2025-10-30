package com.acme.telemetry.service.validation;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.acme.telemetry.api.dto.ErrorDetail;

/**
 * Classe base com helpers reutilizáveis.
 */
public abstract class BaseEventTypeValidator implements EventTypeValidator {

  protected void req(List<ErrorDetail> errors, int idx, Object value, String field) {
    if (value == null || (value instanceof String s && isBlank(s))) {
      errors.add(err(idx, field, "REQUIRED", field + " é obrigatório"));
    }
  }

  protected boolean isBlank(String s) {
    return s == null || s.trim().isEmpty();
  }

  protected Map<String, Object> safeProps(Map<String, Object> props) {
    return props == null ? Collections.emptyMap() : props;
  }

  protected String getString(Map<String, Object> props, String key) {
    Object v = safeProps(props).get(key);
    return v == null ? null : String.valueOf(v);
  }

  @SuppressWarnings({"UnnecessaryTemporaryOnConversionFromString", "UseSpecificCatch"})
  protected Long getLong(Map<String, Object> props, String key) {
    Object v = safeProps(props).get(key);
    if (v == null) return null;
    if (v instanceof Number n) return n.longValue();
    try { return Long.parseLong(String.valueOf(v)); } catch (Exception e) { return null; }
  }

  protected ErrorDetail err(int index, String field, String code, String message) {
    return new ErrorDetail(index, field, code, message);
  }
}