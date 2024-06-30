package accountservice.service;

import accountservice.dtos.MyUserDto;
import accountservice.dtos.DataRequest;
import accountservice.entities.Group;
import accountservice.entities.MyUser;
import accountservice.enums.Operation;
import accountservice.exception.BadRequestException;
import accountservice.exception.NotFoundException;
import accountservice.exception.UserNotFoundException;
import accountservice.repository.GroupRepository;
import accountservice.repository.MyUserRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Mack_TB
 * @since 23/06/2024
 * @version 1.0.6
 */

@Service
public class AdminService {
    private static final Logger LOGGER = LoggerFactory.getLogger(AdminService.class);
    private final MyUserRepository myUserRepository;
    private final GroupRepository groupRepository;
    private final SecurityEventService securityEventService;
    private final HttpServletRequest request;

    @Autowired
    public AdminService(MyUserRepository myUserRepository, GroupRepository groupRepository, SecurityEventService securityEventService, HttpServletRequest request) {
        this.myUserRepository = myUserRepository;
        this.groupRepository = groupRepository;
        this.securityEventService = securityEventService;
        this.request = request;
    }

    public Group findGroupByName(String groupName) {
        return groupRepository.findByName(groupName)
                .orElseThrow(() -> new NotFoundException("Role not found!"));
    }

    public ResponseEntity<MyUserDto> updateRole(Authentication auth, DataRequest dataRequest) {
        LOGGER.info("Updating role: {}", dataRequest.getRole());
        MyUser myUser = getUserByEmail(dataRequest.getUser());
        MyUserDto myUserDto = new MyUserDto(myUser);
        String roleName = dataRequest.getRole().toUpperCase();
        String authorityToAdd = "ROLE_" + roleName;
        Group group = findGroupByName(authorityToAdd);
        checkRules(myUserDto, dataRequest);

        switch (dataRequest.getOperation()) {
            case GRANT -> {
                myUser.getUserGroups().add(group);
                securityEventService.save("GRANT_ROLE", auth.getName(),
                        "Grant role "+roleName+" to "+myUser.getEmail(), request.getRequestURI());
            }
            case REMOVE -> {
                myUser.getUserGroups().remove(group);
                securityEventService.save("REMOVE_ROLE", auth.getName(),
                        "Remove role "+roleName+" from "+myUser.getEmail(), request.getRequestURI());
            }
            default -> throw new BadRequestException("Invalid operation!");
        }
        myUserRepository.save(myUser);
        return ResponseEntity.ok(new MyUserDto(myUser));
    }

    private void checkRules(MyUserDto myUserDto, DataRequest dataRequest) {
        String authorityToAdd = "ROLE_" + dataRequest.getRole().toUpperCase();
        if (!groupRepository.existsByName(authorityToAdd)) {
            throw new NotFoundException("Role not found!");
        }

        if (dataRequest.getOperation().equals(Operation.REMOVE)) {
            // If admin want to delete a role that has not been provided to a user
            if (!myUserDto.getRoles().contains(authorityToAdd)) {
                throw new BadRequestException("The user does not have a role!");
            }
            // when remove the Administrator role
            if (myUserDto.getRoles().contains("ROLE_ADMINISTRATOR")) {
                throw new BadRequestException("Can't remove ADMINISTRATOR role!");
            }
            // If admin want to remove the only existing role of a user
            if (myUserDto.getRoles().size() == 1) {
                throw new BadRequestException("The user must have at least one role!");
            }
        }

        // If an administrative user is granted a business role or vice versa
        if (myUserDto.getRoles().contains("ROLE_ADMINISTRATOR") && !authorityToAdd.equals("ROLE_ADMINISTRATOR") ||
                !myUserDto.getRoles().contains("ROLE_ADMINISTRATOR") && authorityToAdd.equals("ROLE_ADMINISTRATOR")) {
            throw new BadRequestException("The user cannot combine administrative and business roles!");
        }
    }

    public ResponseEntity<?> deleteUser(Authentication auth, String email) {
        LOGGER.info("Deleting user: {}", email);
        MyUser myUser = getUserByEmail(email);
        MyUserDto myUserDto = new MyUserDto(myUser);
        // when remove the Administrator
        if (myUserDto.getRoles().contains("ROLE_ADMINISTRATOR")) {
            LOGGER.info("Can't remove ADMINISTRATOR role");
            throw new BadRequestException("Can't remove ADMINISTRATOR role!");
        }
        myUserRepository.delete(myUser);
        securityEventService.save("DELETE_USER", auth.getName(), myUser.getEmail(), "/api/admin/user");
        LOGGER.info("Deleted successfully!");
        Map<String, String> response = Map.of("user", email,
                "status", "Deleted successfully!");
        return ResponseEntity.ok(response);
    }

    public MyUser getUserByEmail(String email) {
        return myUserRepository.findByEmailIgnoreCase(email)
                .orElseThrow(() -> {
                    LOGGER.warn("User not found: {}", email);
                    return new UserNotFoundException("User not found!");
                });
    }

    public List<MyUserDto> findAllUsers() {
        LOGGER.info("Finding all users");
        Sort sortById = Sort.by("id").ascending();
        List<MyUser> myUsers = myUserRepository.findAll(sortById);
        List<MyUserDto> response = new ArrayList<>();
        for (MyUser myUser : myUsers) {
            response.add(new MyUserDto(myUser));
        }
        return response;
    }

    public ResponseEntity<?> updateUserLock(Authentication auth, DataRequest dataRequest) {
        LOGGER.info("Updating user lock: {}", dataRequest.getOperation());
        MyUser myUser = getUserByEmail(dataRequest.getUser());
        MyUserDto myUserDto = new MyUserDto(myUser);
        if (dataRequest.getOperation().equals(Operation.LOCK) ||
                dataRequest.getOperation().equals(Operation.UNLOCK)) { //
            if ((myUser.isAccountNonLocked() && dataRequest.getOperation().equals(Operation.LOCK)) ||
                    (!myUser.isAccountNonLocked() && dataRequest.getOperation().equals(Operation.UNLOCK))) {
                if (myUserDto.getRoles().contains("ROLE_ADMINISTRATOR")) {
                    throw new BadRequestException("Can't lock the ADMINISTRATOR!");
                }
                if (dataRequest.getOperation().equals(Operation.LOCK)) {
                    myUser.setAccountNonLocked(false);
                    securityEventService.save("LOCK_USER", auth.getName(), "Lock user "+ myUser.getEmail(), request.getRequestURI());
                } else {
                    myUser.setAccountNonLocked(true);
                    myUser.setFailedLoginAttempts(0);
                    securityEventService.save("UNLOCK_USER", auth.getName(), "Unlock user "+ myUser.getEmail(), request.getRequestURI());
                }
            } else {
                throw new BadRequestException("User is already " + dataRequest.getOperation().name());
            }
        } else {
            throw new BadRequestException("Invalid operation!");
        }
        myUserRepository.save(myUser);
        LOGGER.info("User {} {}ed!", myUser.getEmail(), dataRequest.getOperation().name().toLowerCase());
        Map<String, String> response = Map.of("status", String.format("User %s %sed!", myUser.getEmail(),
                dataRequest.getOperation().name().toLowerCase()));
        return ResponseEntity.ok(response);
    }
}