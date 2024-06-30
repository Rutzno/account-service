package accountservice.web;

import account.entities.MyUser;
import account.service.MyUserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

/**
 * @author Mack_TB
 * @since 23/06/2024
 * @version 1.0.2
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
    @ResponseStatus(HttpStatus.NOT_IMPLEMENTED)
    public void changePassword(String oldPassword, String newPassword) {

    }

    @GetMapping("/test")
    public String test() {
        return "test AAA";
    }
}