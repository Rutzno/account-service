package account.web;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api")
public class EmployeeController {

    /**
     * gives access to the employee's payrolls
     */
    @GetMapping("/empl/payment")
    @ResponseStatus(HttpStatus.NOT_IMPLEMENTED)
    public void getPayments() {

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