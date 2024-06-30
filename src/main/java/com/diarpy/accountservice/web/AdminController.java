package accountservice.web;

import accountservice.dtos.MyUserDto;
import accountservice.dtos.RoleDto;
import accountservice.service.AdminService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Mack_TB
 * @since 23/06/2024
 * @version 1.0.5
 */

@RestController
@RequestMapping("api/admin")
public class AdminController {
    private final AdminService adminService;

    @Autowired
    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    /**
     * changes user roles (sets the roles)
     */
    @PutMapping("/user/role")
    public ResponseEntity<MyUserDto> updateRole(@RequestBody @Valid RoleDto roleDto) {
        return adminService.updateRole(roleDto);
    }

    /**
     * obtains information about all users
     */
    @GetMapping( path = {"/user","/user/"})
    public List<MyUserDto> getUsers() {
        return adminService.findAllUsers();
    }

    /**
     * deletes a user
     */
    @DeleteMapping("/user/{email}")
    public ResponseEntity<?> deleteUser(@PathVariable String email) {
        return adminService.deleteUser(email);
    }
}