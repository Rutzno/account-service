package com.diarpy.accountservice.exception;

/**
 * @author Mack_TB
 * @since 23/06/2024
 * @version 1.0.5
 */

public class NotFoundException extends RuntimeException {
    public NotFoundException(String message) {
        super(message);
    }
}