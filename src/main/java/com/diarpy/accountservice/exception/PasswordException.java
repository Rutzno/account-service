package com.diarpy.accountservice.exception;

/**
 * @author Mack_TB
 * @since 23/06/2024
 * @version 1.0.3
 */

public class PasswordException extends RuntimeException {

    public PasswordException(String message) {
        super(message);
    }
}