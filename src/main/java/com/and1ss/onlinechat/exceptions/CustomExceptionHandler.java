package com.and1ss.onlinechat.exceptions;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.NoSuchElementException;

@ControllerAdvice
@RestController
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {
    @Data
    @AllArgsConstructor
    public class ApiError {
        private final Timestamp timestamp;
        private final String message;
    }

    @ExceptionHandler(InvalidLoginCredentialsException.class)
    public final ResponseEntity<Object> handleInvalidLoginCredentialsException(
            InvalidLoginCredentialsException ex
    ) {
        ApiError exceptionResponse = new ApiError(
                Timestamp.from(Instant.now()),
                ex.getMessage()
        );
        return new ResponseEntity(exceptionResponse, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(NoSuchElementException.class)
    public final ResponseEntity<Object> handleNoSuchElementException() {
        ApiError exceptionResponse = new ApiError(
                Timestamp.from(Instant.now()),
                "No such element"
        );
        return new ResponseEntity(exceptionResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InvalidRegisterDataException.class)
    public final ResponseEntity<Object> handleInvalidRegisterDataException(
            InvalidRegisterDataException ex
    ) {
        ApiError exceptionResponse = new ApiError(
                Timestamp.from(Instant.now()),
                ex.getMessage()
        );
        return new ResponseEntity(exceptionResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UnauthorizedException.class)
    public final ResponseEntity<Object> handleUnauthorizedException(
            UnauthorizedException ex
    ) {
        ApiError exceptionResponse = new ApiError(
                Timestamp.from(Instant.now()),
                ex.getMessage()
        );
        return new ResponseEntity(exceptionResponse, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(InternalServerException.class)
    public final ResponseEntity<Object> handleInternalServerException(
            InternalServerException ex
    ) {
        ApiError exceptionResponse = new ApiError(
                Timestamp.from(Instant.now()),
                ex.getMessage()
        );
        return new ResponseEntity(exceptionResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(BadRequestException.class)
    public final ResponseEntity<Object> handleBadRequest(
            BadRequestException ex
    ) {
        ApiError exceptionResponse = new ApiError(
                Timestamp.from(Instant.now()),
                ex.getMessage()
        );
        return new ResponseEntity(exceptionResponse, HttpStatus.BAD_REQUEST);
    }
}