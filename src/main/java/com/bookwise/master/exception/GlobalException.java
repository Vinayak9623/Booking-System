package com.bookwise.master.exception;

import com.bookwise.common.ErrorResponse;
import com.bookwise.master.exception.customeEx.ReservationNotFound;
import com.bookwise.master.exception.customeEx.ResourceNotFound;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class GlobalException extends RuntimeException {

@ExceptionHandler(ResourceNotFound.class)
public ResponseEntity<ErrorResponse> handleResourceNotFound(ResourceNotFound ex, HttpServletRequest request) {
    ErrorResponse error = new ErrorResponse();
    error.setStatus(HttpStatus.NOT_FOUND.value());
    error.setMessage(ex.getMessage());
    error.setPath(request.getRequestURI());
    error.setTimestamp(LocalDateTime.now());
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
}

    @ExceptionHandler(ReservationNotFound.class)
    public ResponseEntity<ErrorResponse> handleReservationNotFound(ReservationNotFound ex, HttpServletRequest request) {
        ErrorResponse error = new ErrorResponse();
        error.setStatus(HttpStatus.NOT_FOUND.value());
        error.setMessage(ex.getMessage());
        error.setPath(request.getRequestURI());
        error.setTimestamp(LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }
}