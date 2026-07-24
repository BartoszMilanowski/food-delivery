package com.food_delivery.restaurant_service.exception;

import java.time.Instant;
import java.util.Map;

public class ErrorResponse {

    private final Instant timestamp;
    private final int status;
    private final String error;
    private final String message;
    private final Map<String, String> fieldError;

    public ErrorResponse(Instant timestamp, int status, String error, String message) {
        this(timestamp, status, error, message, null);
    }

    public ErrorResponse(Instant timestamp, int status, String error, String message, Map<String, String> fieldError) {
        this.timestamp = timestamp;
        this.status = status;
        this.error = error;
        this.message = message;
        this.fieldError = fieldError;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public int getStatus() {
        return status;
    }

    public String getError() {
        return error;
    }

    public String getMessage() {
        return message;
    }

    public Map<String, String> getFieldError() {
        return fieldError;
    }
}
