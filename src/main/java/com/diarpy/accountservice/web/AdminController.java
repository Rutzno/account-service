package account.web;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/admin")
public class AdminController {

    /**
     * changes user roles
     */
    @PutMapping("/user/role")
    @ResponseStatus(HttpStatus.NOT_IMPLEMENTED)
    public void updateRole() {

    }

    /**
     * deletes a user
     */
    @PutMapping("/user")
    @ResponseStatus(HttpStatus.NOT_IMPLEMENTED)
    public void deleteUser() {

    }

    /**
     * displays information about all users
     */
    @GetMapping("/user")
    @ResponseStatus(HttpStatus.NOT_IMPLEMENTED)
    public void getUsers() {

    }
}