package accountservice.exception;

import java.time.LocalDateTime;

/**
 * @author Mack_TB
 * @since 23/06/2024
 * @version 1.0.2
 */

public class CustomErrorMessage {
    private LocalDateTime timestamp;
    private int status;
    private String error;
    private String message;
    private String path;

    public CustomErrorMessage(LocalDateTime timestamp, int status, String error,
                              String message, String path) {
        this.timestamp = timestamp;
        this.status = status;
        this.error = error;
        this.message = message;
        this.path = path;
    }

    public LocalDateTime getTimestamp() {
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

    public String getPath() {
        return path;
    }
}