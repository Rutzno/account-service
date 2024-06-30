package accountservice.service;

import accountservice.dtos.MyUserDto;
import accountservice.entities.Group;
import accountservice.entities.MyUser;
import accountservice.exception.PasswordException;
import accountservice.exception.UserAlreadyExistException;
import accountservice.repository.GroupRepository;
import accountservice.repository.MyUserRepository;
import accountservice.security.SecurityConfig;
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
 * @version 1.0.6
 */

@Service
public class AuthService {
    private static final Logger LOGGER = LoggerFactory.getLogger(AuthService.class);
    private final MyUserRepository myUserRepository;
    private final GroupRepository groupRepository;
    private final SecurityEventService securityEventService;
    private final PasswordEncoder passwordEncoder;

    public AuthService(MyUserRepository myUserRepository, GroupRepository groupRepository, SecurityEventService securityEventService, PasswordEncoder passwordEncoder) {
        this.myUserRepository = myUserRepository;
        this.groupRepository = groupRepository;
        this.securityEventService = securityEventService;
        this.passwordEncoder = passwordEncoder;
    }

    public ResponseEntity<MyUserDto> register(MyUser myUser, Errors errors) {
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
        myUser.setAccountNonLocked(true);
        updateMyUserGroup(myUser);
        MyUser newUser = myUserRepository.save(myUser);
        securityEventService.save("CREATE_USER", "Anonymous", newUser.getEmail(), "/api/auth/signup");
        LOGGER.info("New user registered with email: {}", newUser.getEmail());
        return ResponseEntity.ok(new MyUserDto(newUser));
    }

    private void updateMyUserGroup(MyUser myUser) {
        Group group = myUserRepository.count() > 0 ?
                groupRepository.findByName("ROLE_USER").get() : groupRepository.findByName("ROLE_ADMINISTRATOR").get();
        LOGGER.info("User role :{}", group.getName());
        myUser.getUserGroups().add(group);
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
        securityEventService.save("CHANGE_PASSWORD", currentUser.getEmail(), currentUser.getEmail(), "/api/auth/changepass");
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
            for (String bp : SecurityConfig.breachedPasswords) {
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