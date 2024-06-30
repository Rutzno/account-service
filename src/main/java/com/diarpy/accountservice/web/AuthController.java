package accountservice.web;

import accountservice.entities.MyUser;
import accountservice.service.AuthService;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

/**
 * @author Mack_TB
 * @since 23/06/2024
 * @version 1.0.4
 */

@RestController
@RequestMapping("api/auth")
public class AuthController {
    private final MyUserService myUserService;

    @Autowired
    public AuthController(MyUserService myUserService) {
        this.myUserService = myUserService;
    }

    /**
     * allows the user to register on the service
     */
    @PostMapping("/signup")
    public ResponseEntity<MyUser> register(@RequestBody @Valid MyUser user, Errors errors) {
        return myUserService.register(user, errors);
    }

    /**
     * changes a user password
     */
    @PostMapping("/changepass")
    public ResponseEntity<?> changePassword(Authentication auth,
                                            @RequestBody @Valid PasswordRequest passwordRequest) {
        return myUserService.changePassword(auth, passwordRequest.new_password());
    }

    record PasswordRequest(@JsonProperty("new_password") String newPassword) {}

}