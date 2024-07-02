package com.diarpy.accountservice.web;

import com.diarpy.accountservice.dtos.MyUserDto;
import com.diarpy.accountservice.entities.MyUser;
import com.diarpy.accountservice.service.AuthService;
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
 * @version 1.0.5
 */

@RestController
@RequestMapping("api/auth")
public class AuthController {
    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    /**
     * allows the user to register on the service
     */
    @PostMapping("/signup")
    public ResponseEntity<MyUserDto> register(@RequestBody @Valid MyUser user, Errors errors) {
        return authService.register(user, errors);
    }

    /**
     * changes a user password
     */
    @PostMapping("/changepass")
    public ResponseEntity<?> changePassword(Authentication auth,
                                            @RequestBody PasswordRequest passwordRequest) {
        return authService.changePassword(auth, passwordRequest.newPassword());
    }

    record PasswordRequest(@JsonProperty("new_password") String newPassword) {}
}
