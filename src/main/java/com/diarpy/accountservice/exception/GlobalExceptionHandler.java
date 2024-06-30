package accountservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;

/**
 * @author Mack_TB
 * @since 23/06/2024
 * @version 1.0.5
 */

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserAlreadyExistException.class)
    public ResponseEntity<CustomErrorMessage> handleUserAlreadyExistsException(UserAlreadyExistException ex,
                                                                               WebRequest request) {
        CustomErrorMessage customErrorMessage = new CustomErrorMessage(LocalDateTime.now(),
                400, "Bad Request", ex.getMessage(),
                request.getDescription(false).replace("uri=", ""));
        return new ResponseEntity<>(customErrorMessage, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<CustomErrorMessage> handleUserNotFoundException(UserNotFoundException ex,
                                                                          WebRequest request) {
        CustomErrorMessage customErrorMessage = new CustomErrorMessage(LocalDateTime.now(),
                404, "Not Found", ex.getMessage(),
                request.getDescription(false).replace("uri=", ""));
        return new ResponseEntity<>(customErrorMessage, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<CustomErrorMessage> handleNotFoundException(NotFoundException ex,
                                                                      WebRequest request) {
        CustomErrorMessage customErrorMessage = new CustomErrorMessage(LocalDateTime.now(),
                404, "Not Found", ex.getMessage(),
                request.getDescription(false).replace("uri=", ""));
        return new ResponseEntity<>(customErrorMessage, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<CustomErrorMessage> handleBadRequestException(BadRequestException ex,
                                                                        WebRequest request) {
        CustomErrorMessage customErrorMessage = new CustomErrorMessage(LocalDateTime.now(),
                400, "Bad Request", ex.getMessage(),
                request.getDescription(false).replace("uri=", ""));
        return new ResponseEntity<>(customErrorMessage, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(PasswordException.class)
    public ResponseEntity<CustomErrorMessage> handlePasswordException(PasswordException ex,
                                                                      WebRequest request) {
        CustomErrorMessage customErrorMessage = new CustomErrorMessage(LocalDateTime.now(),
                400, "Bad Request", ex.getMessage(),
                request.getDescription(false).replace("uri=", ""));
        return new ResponseEntity<>(customErrorMessage, HttpStatus.BAD_REQUEST);
    }
}