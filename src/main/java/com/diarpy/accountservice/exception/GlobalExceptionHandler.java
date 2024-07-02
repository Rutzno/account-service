package com.diarpy.accountservice.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

/**
 * @author Mack_TB
 * @since 23/06/2024
 * @version 1.0.6
 */

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserAlreadyExistException.class)
    public ResponseEntity<CustomErrorMessage> handleUserAlreadyExistsException(UserAlreadyExistException ex,
                                                                               HttpServletRequest request) {
        CustomErrorMessage customErrorMessage = new CustomErrorMessage(LocalDateTime.now(),
                400, "Bad Request", ex.getMessage(),
                request.getRequestURI());
        return new ResponseEntity<>(customErrorMessage, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<CustomErrorMessage> handleUserNotFoundException(UserNotFoundException ex,
                                                                          HttpServletRequest request) {
        CustomErrorMessage customErrorMessage = new CustomErrorMessage(LocalDateTime.now(),
                404, "Not Found", ex.getMessage(),
                request.getRequestURI());
        return new ResponseEntity<>(customErrorMessage, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<CustomErrorMessage> handleNotFoundException(NotFoundException ex,
                                                                      HttpServletRequest request) {
        CustomErrorMessage customErrorMessage = new CustomErrorMessage(LocalDateTime.now(),
                404, "Not Found", ex.getMessage(),
                request.getRequestURI());
        return new ResponseEntity<>(customErrorMessage, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<CustomErrorMessage> handleBadRequestException(BadRequestException ex,
                                                                        HttpServletRequest request) {
        CustomErrorMessage customErrorMessage = new CustomErrorMessage(LocalDateTime.now(),
                400, "Bad Request", ex.getMessage(),
                request.getRequestURI());
        return new ResponseEntity<>(customErrorMessage, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(PasswordException.class)
    public ResponseEntity<CustomErrorMessage> handlePasswordException(PasswordException ex,
                                                                      HttpServletRequest request) {
        CustomErrorMessage customErrorMessage = new CustomErrorMessage(LocalDateTime.now(),
                400, "Bad Request", ex.getMessage(),
                request.getRequestURI());
        return new ResponseEntity<>(customErrorMessage, HttpStatus.BAD_REQUEST);
    }
}