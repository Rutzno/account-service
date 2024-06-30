package accountservice.service;

import account.entities.MyUser;
import account.exception.PasswordException;
import account.exception.UserAlreadyExistException;
import account.repository.MyUserRepository;
import account.security.SecurityConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;

/**
 * @author Mack_TB
 * @since 23/06/2024
 * @version 1.0.3
 */

@Service
public class AuthService {
    private static final Logger LOGGER = LoggerFactory.getLogger(MyUserService.class);
    private final MyUserRepository myUserRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(MyUserRepository myUserRepository, PasswordEncoder passwordEncoder) {
        this.myUserRepository = myUserRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public ResponseEntity<MyUser> register(MyUser myUser, Errors errors) {
        LOGGER.info("Register request for email {}", myUser.getEmail());
        if (myUserRepository.findByEmailIgnoreCase(myUser.getEmail()).isPresent()) {
            LOGGER.warn("User with email {} already exists", myUser.getEmail());
            throw new UserAlreadyExistException();
        }
        isPasswordLengthValid(myUser.getPassword());
        isPasswordBreached(myUser.getPassword());
        if (errors.hasErrors()) {
            LOGGER.warn("Validation errors; {}", errors.getAllErrors());
            return ResponseEntity.badRequest().build();
        }
        myUser.setEmail(myUser.getEmail().toLowerCase());
        myUser.setPassword(passwordEncoder.encode(myUser.getPassword()));
        myUser.setAuthority("ROLE_USER");
        MyUser newUser = myUserRepository.save(myUser);
        LOGGER.info("New user registered with email: {}", newUser.getEmail());
        return ResponseEntity.ok(newUser);
    }

    public MyUser getUserByEmail(Authentication auth) {
        return myUserRepository.findByEmailIgnoreCase(auth.getName())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "User not found"));
    }

    public ResponseEntity<?> changePassword(Authentication auth, String newPassword) {
        LOGGER.info("Change password request for email {}", auth.getName());
        MyUser currentUser = getUserByEmail(auth);
        String hashOfOldPassword = currentUser.getPassword();

        isPasswordLengthValid(newPassword);
        isPasswordBreached(newPassword);
        arePasswordsSame(newPassword, hashOfOldPassword);

        currentUser.setPassword(passwordEncoder.encode(newPassword));
        myUserRepository.save(currentUser);
        Map<String, String> response = Map.of("email", auth.getName(),
                "status", "The password has been updated successfully");
        LOGGER.info("The password has been updated successfully {}", newPassword);
        return ResponseEntity.ok(response);
    }

    private void isPasswordLengthValid(String password) {
        if (password == null || password.length() < 12) {
            LOGGER.warn("Password length must be 12 chars minimum!: {}", password);
            throw new PasswordException("Password length must be 12 chars minimum!");
        }
    }

    private void isPasswordBreached(String password) {
        if (password != null) {
            for (String bp : SecurityConfig.breachedPassword) {
                if (password.equals(bp)) {
                    LOGGER.warn("The password is in the hacker's database!: {}", password);
                    throw new PasswordException("The password is in the hacker's database!");
                }
            }
        }
    }

    private void arePasswordsSame(String newPassword, String hashOfOldPassword) {
        if (newPassword != null && passwordEncoder.matches(newPassword, hashOfOldPassword)) {
            LOGGER.warn("The passwords must be different!: {}", newPassword);
            throw new PasswordException("The passwords must be different!");
        }
    }
}