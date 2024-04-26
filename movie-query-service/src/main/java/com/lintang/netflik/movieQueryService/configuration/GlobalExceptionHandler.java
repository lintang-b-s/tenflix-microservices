package com.lintang.netflik.movieQueryService.configuration;

import com.lintang.netflik.movieQueryService.exception.BadRequestException;
import com.lintang.netflik.movieQueryService.exception.ErrorResponse;
import com.lintang.netflik.movieQueryService.exception.InternalServerEx;
import com.lintang.netflik.movieQueryService.exception.ResourceNotFoundException;
import com.lintang.netflik.movieQueryService.exception.UnauthorizedError;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleException(ResourceNotFoundException exc) {

        ErrorResponse error = new ErrorResponse();
        error.setStatus(HttpStatus.NOT_FOUND.value());
        error.setMessage(exc.getMessage());
        error.setTimeStamp(System.currentTimeMillis());

        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorResponse> handleException(BadRequestException exc) {
        ErrorResponse error = new ErrorResponse();
        error.setStatus(HttpStatus.BAD_REQUEST.value());
        error.setMessage(exc.getMessage());
        error.setTimeStamp(System.currentTimeMillis());

        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InternalServerEx.class)
    public ResponseEntity<ErrorResponse> handleException(InternalServerEx exc) {
        ErrorResponse error = new ErrorResponse();
        error.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        error.setMessage(exc.getMessage());
        error.setTimeStamp(System.currentTimeMillis());

        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(UnauthorizedError.class)
    public ResponseEntity<ErrorResponse> handleException(UnauthorizedError exc) {
        ErrorResponse error = new ErrorResponse();
        error.setStatus(HttpStatus.UNAUTHORIZED.value());
        error.setMessage(exc.getMessage());
        error.setTimeStamp(System.currentTimeMillis());

        return new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);
    }




}
