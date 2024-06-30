package accountservice.exception;

/**
 * @author Mack_TB
 * @since 23/06/2024
 * @version 1.0.5
 */

public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException(String message) {
        super(message);
    }
}