package com.WisdomMonkey.CinemaTicketBooking_Backend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * This class provides centralized exception handling across the entire application.
 * Acts as an interceptor for exceptions thrown by @RequestMapping methods
 */
@ControllerAdvice  // Makes this class handle exceptions from all controllers
public class GlobalExceptionHandler {

    /**
     * Handles Bean Validation failures (@Valid annotation)
     *
     * When does this trigger?
     * - Client sends JSON with missing required fields
     * - Client sends data that fails @NotNull, @Email, @Size validations
     * - Any @Valid annotation in controller methods fails
     *
     * @param ex The validation exception containing all validation failures
     * @return ResponseEntity with detailed field-level error messages
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationExceptions(
            MethodArgumentNotValidException ex) {

        /*
         * Extract field-level validation errors
         * Transform Spring's BindingResult into a user-friendly format
         * Each field error becomes a key-value pair: "fieldName" -> "error message"
         */
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();  // e.g., "email"
            String errorMessage = error.getDefaultMessage();      // e.g., "must not be null"
            errors.put(fieldName, errorMessage);
        });

        /*
         * Create standardized error response
         * - 400 Bad Request: Client error (invalid data)
         * - Include all field validation errors
         * - Add timestamp for debugging and logging
         */
        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                "Validation failed",
                errors,  // Map of field -> error message
                LocalDateTime.now()
        );

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles business logic violations and invalid arguments
     *
     * When does this trigger?
     * - Service layer throws IllegalArgumentException for business rule violations
     * - Invalid method parameters that don't violate Bean Validation but are logically wrong
     * - Custom validation in business logic fails
     *
     * @param ex The illegal argument exception with specific error message
     * @param request Web request context (for logging/debugging)
     * @return ResponseEntity with business logic error message
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(
            IllegalArgumentException ex, WebRequest request) {

        /*
         * Business Logic Error Response:
         * - 400 Bad Request: Client attempted invalid operation
         * - Use the exception message directl
         */
        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                ex.getMessage(),  // Business-specific error message
                null,             // No field-level errors for business logic violations
                LocalDateTime.now()
        );

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles unexpected system errors and runtime exceptions
     *
     * When does this trigger?
     * - Database connection failures
     * - External API calls fail (TMDB API down)
     * - Unexpected null pointer exceptions
     * - Any other RuntimeException not caught by specific handlers
     *
     * Security Note:
     * - Never expose internal error details to clients
     * - Log the actual exception for debugging
     * - Return generic error message to prevent information leakage
     *
     * @param ex The runtime exception that occurred
     * @param request Web request context (for logging/debugging)
     * @return ResponseEntity with generic server error message
     */
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> handleRuntimeException(
            RuntimeException ex, WebRequest request) {

        /*
         * TODO: Add proper logging here
         * logger.error("Unexpected error occurred: ", ex);
         */

        /*
         * System Error Response:
         * - 500 Internal Server Error: Server-side failure
         * - Generic message (don't expose system internals to client)
         * - Log the actual exception details for debugging
         */
        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Internal server error occurred",
                null,                               // No error details exposed
                LocalDateTime.now()
        );

        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Standarized Error Response DTO
     * This class defines the structure of all error responses from our API.
     */
    public static class ErrorResponse {
        private int status;
        private String message;
        private Object errors;
        private LocalDateTime timestamp;

        /**
         * Constructor for creating error responses
         *
         * @param status HTTP status code
         * @param message Main error message
         * @param errors Detailed error information (can be null)
         * @param timestamp When the error occurred
         */
        public ErrorResponse(int status, String message, Object errors, LocalDateTime timestamp) {
            this.status = status;
            this.message = message;
            this.errors = errors;
            this.timestamp = timestamp;
        }

        // Getters for JSON serialization
        public int getStatus() { return status; }
        public String getMessage() { return message; }
        public Object getErrors() { return errors; }
        public LocalDateTime getTimestamp() { return timestamp; }
    }
}