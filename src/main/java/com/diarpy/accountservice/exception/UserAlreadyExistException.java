package accountservice.exception;

/**
 * @author Mack_TB
 * @since 23/06/2024
 * @version 1.0.2
 */

//@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class UserAlreadyExistException extends RuntimeException {

    public UserAlreadyExistException() {
        super("User exist!");
    }
}