package com.diarpy.accountservice.exception;

/**
 * @author Mack_TB
 * @since 23/06/2024
 * @version 1.0.5
 */

public class BadRequestException extends RuntimeException {
    public BadRequestException(String message) {
        super(message);
    }
}