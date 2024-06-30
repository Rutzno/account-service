package accountservice.service;

import accountservice.dtos.MyUserDto;
import accountservice.dtos.RoleDto;
import accountservice.entities.Group;
import accountservice.entities.MyUser;
import accountservice.enums.Operation;
import accountservice.exception.BadRequestException;
import accountservice.exception.NotFoundException;
import accountservice.exception.UserNotFoundException;
import accountservice.repository.GroupRepository;
import accountservice.repository.MyUserRepository;
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
 * @version 1.0.5
 */

@Service
public class AdminService {
    private static final Logger LOGGER = LoggerFactory.getLogger(AdminService.class);
    private final MyUserRepository myUserRepository;
    private final GroupRepository groupRepository;

    @Autowired
    public AdminService(MyUserRepository myUserRepository, GroupRepository groupRepository) {
        this.myUserRepository = myUserRepository;
        this.groupRepository = groupRepository;
    }

    public Group findGroupByName(String groupName) {
        return groupRepository.findByName(groupName)
                .orElseThrow(() -> new NotFoundException("Role not found!"));
    }

    public ResponseEntity<MyUserDto> updateRole(RoleDto roleDto) {
        LOGGER.info("Updating role: {}", roleDto.getRole());
        MyUser myUser = getUserByEmail(roleDto.getUser());
        MyUserDto myUserDto = new MyUserDto(myUser);
        String authorityToAdd = "ROLE_" + roleDto.getRole().toUpperCase();
        Group group = findGroupByName(authorityToAdd);
        checkRules(myUserDto, roleDto);

        switch (roleDto.getOperation()) {
            case GRANT -> myUser.getUserGroups().add(group);
            case REMOVE -> myUser.getUserGroups().remove(group);
            default -> throw new BadRequestException("Invalid operation!");
        }
        myUserRepository.save(myUser);
        return ResponseEntity.ok(new MyUserDto(myUser));
    }

    private void checkRules(MyUserDto myUserDto, RoleDto roleDto) {
        String authorityToAdd = "ROLE_" + roleDto.getRole().toUpperCase();
        if (!groupRepository.existsByName(authorityToAdd)) {
            throw new NotFoundException("Role not found!");
        }

        if (roleDto.getOperation().equals(Operation.REMOVE)) {
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

    public ResponseEntity<?> deleteUser(String email) {
        LOGGER.info("Deleting user: {}", email);
        MyUser myUser = getUserByEmail(email);
        MyUserDto myUserDto = new MyUserDto(myUser);
        // when remove the Administrator
        if (myUserDto.getRoles().contains("ROLE_ADMINISTRATOR")) {
            LOGGER.info("Can't remove ADMINISTRATOR role");
            throw new BadRequestException("Can't remove ADMINISTRATOR role!");
        }
        myUserRepository.delete(myUser);
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
}