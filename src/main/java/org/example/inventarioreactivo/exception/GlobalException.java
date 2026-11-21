package org.example.inventarioreactivo.exception;

import org.example.inventarioreactivo.model.dto.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;

import java.time.Instant;

@RestControllerAdvice
public class GlobalException {

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ErrorResponse> handleResponseStatusException(
            ResponseStatusException exception, ServerWebExchange exchange
    ) {
        HttpStatus status = HttpStatus.valueOf(
                exception.getStatusCode().value()
        );
        ErrorResponse response = new ErrorResponse(
                Instant.now(),
                status.value(),
                status.getReasonPhrase(),
                exception.getReason(),
                exchange.getRequest().getPath().value()
        );

        return ResponseEntity.status(status).body(response);


    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleUnexpectedException(Exception exception, ServerWebExchange exchange) {
        ErrorResponse response = new ErrorResponse(
                Instant.now(),
                500,
                "Internal Server Error",
                "An unexpected error ocurred",
                exchange.getRequest().getPath().value()
        );

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(response);
    }
}
