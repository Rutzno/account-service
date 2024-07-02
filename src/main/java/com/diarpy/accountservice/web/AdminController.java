package com.diarpy.accountservice.web;

import com.diarpy.accountservice.dtos.MyUserDto;
import com.diarpy.accountservice.dtos.DataRequest;
import com.diarpy.accountservice.service.AdminService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Mack_TB
 * @since 23/06/2024
 * @version 1.0.6
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
    public ResponseEntity<MyUserDto> updateRole(Authentication auth,
                                                @RequestBody @Valid DataRequest dataRequest) {
        return adminService.updateRole(auth, dataRequest);
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
    public ResponseEntity<?> deleteUser(Authentication auth,
                                        @PathVariable String email) {
        return adminService.deleteUser(auth, email);
    }

    @PutMapping("/user/access")
    public ResponseEntity<?> updateUserLock(Authentication auth,
                                            @RequestBody @Valid DataRequest dataRequest) {
        return adminService.updateUserLock(auth, dataRequest);
    }
}