package accountservice.web;

import account.entities.MyUser;
import account.service.MyUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

/**
 * @author Mack_TB
 * @since 23/06/2024
 * @version 1.0.2
 */

@RestController
@RequestMapping("api")
public class EmployeeController {

    private final MyUserService myUserService;

    @Autowired
    public EmployeeController(MyUserService myUserService) {
        this.myUserService = myUserService;
    }

    /**
     * gives access to the employee's payrolls
     */
    @GetMapping("/empl/payment")
    public MyUser getPayments(Authentication auth) {
        return myUserService.getUserByEmail(auth);
    }

    /**
     * uploads payrolls
     */
    @PostMapping("/acct/payments")
    @ResponseStatus(HttpStatus.NOT_IMPLEMENTED)
    public void postPayrolls() {

    }

    /**
     * updates payment information.
     */
    @PutMapping("/acct/payments")
    @ResponseStatus(HttpStatus.NOT_IMPLEMENTED)
    public void putPayrolls() {

    }
}