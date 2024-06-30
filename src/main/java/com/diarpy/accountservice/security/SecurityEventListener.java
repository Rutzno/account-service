package accountservice.security;

import accountservice.dtos.MyUserDto;
import accountservice.entities.MyUser;
import accountservice.entities.MyUserAdapter;
import accountservice.service.EmployeeService;
import accountservice.service.SecurityEventService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AbstractAuthenticationFailureEvent;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.TimeZone;

/**
 * @author Mack_TB
 * @since 23/06/2024
 * @version 1.0.6
 */

@Component
public class SecurityEventListener {
    @Autowired
    private SecurityEventService securityEventService;
    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private HttpServletRequest request;

    @EventListener
    public void onSuccess(AuthenticationSuccessEvent successEvent){
        Authentication auth = successEvent.getAuthentication();
        MyUserAdapter userDetails = (MyUserAdapter) auth.getPrincipal();
        MyUser myUser = userDetails.getMyUser();
        if (myUser.getFailedLoginAttempts() > 0) {
            employeeService.resetFailedAttempts(myUser.getEmail());
        }
    }

    @EventListener
    public void onAuthenticationFailure(AbstractAuthenticationFailureEvent failureEvent) {
        Authentication auth = failureEvent.getAuthentication();
        String username = auth.getName();
        String path = request.getRequestURI();
        MyUser user = employeeService.getUserByEmail(username.toLowerCase());
        if (user != null) {
            if (user.isAccountNonLocked()) {
                securityEventService.save("LOGIN_FAILED", username, path, path);
                if (user.getFailedLoginAttempts() < EmployeeService.MAX_FAILED_ATTEMPTS) {
                    employeeService.increaseFailedAttempts(user);
                    user.setFailedLoginAttempts(user.getFailedLoginAttempts() + 1);
                }
                if (user.getFailedLoginAttempts() == EmployeeService.MAX_FAILED_ATTEMPTS) {
                    securityEventService.save("BRUTE_FORCE", username, path, path);
                    MyUserDto myUserDto = new MyUserDto(user);
                    if (!myUserDto.getRoles().contains("ROLE_ADMINISTRATOR")) {
                        employeeService.lock(user);
                        securityEventService.save("LOCK_USER", user.getEmail(), "Lock user " + user.getEmail(), path);
                    }
                }
            }
        } else {
            securityEventService.save("LOGIN_FAILED", username, path, path);
        }
    }

    private LocalDateTime toLocalDateTime(long timestamp) {
        return LocalDateTime.ofInstant(
                Instant.ofEpochSecond(timestamp / 1000), TimeZone.getDefault().toZoneId()
        );
    }
}