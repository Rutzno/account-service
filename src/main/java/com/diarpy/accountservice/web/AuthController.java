package account.web;

import account.entities.MyUser;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/auth")
public class AuthController {

    public AuthenticationController() {

    }

    /**
     * allows the user to register on the service
     */
    @PostMapping("/signup")
    public ResponseEntity<?> register(@RequestBody @Valid MyUser user, Errors errors) {
        if (errors.hasErrors()) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(user);
    }

    /**
     * changes a user password
     */
    @PostMapping("/changepass")
    @ResponseStatus(HttpStatus.NOT_IMPLEMENTED)
    public void changePassword(String oldPassword, String newPassword) {

    }
}