package accountservice.service;

import account.entities.MyUser;
import account.exception.UserAlreadyExistException;
import account.repository.MyUserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;
import org.springframework.web.server.ResponseStatusException;

/**
 * @author Mack_TB
 * @since 23/06/2024
 * @version 1.0.2
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
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
    }
}