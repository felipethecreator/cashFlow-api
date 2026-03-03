package com.cashflow.api.common.errors;

import java.time.Instant;
import java.util.List;

public record ApiErrorDto(
        Instant timestamp,
        int status,
        String error,
        String message,
        String path,
        String traceId,
        List<FieldError> fields
) {
    public record FieldError(String field, String message) {}
}