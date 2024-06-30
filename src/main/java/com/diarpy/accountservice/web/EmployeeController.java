package accountservice.web;

import accountservice.dtos.UserPaymentDto;
import accountservice.entities.Payment;
import accountservice.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.util.List;
/**
 * @author Mack_TB
 * @since 23/06/2024
 * @version 1.0.4
 */

@RestController
@RequestMapping("api")
public class EmployeeController {

    private final EmployeeService employeeService;

    @Autowired
    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    /**
     * gives access to the employee's payrolls
     */
    @GetMapping("/empl/payment")
    public List<UserPaymentDto> getPayments(Authentication auth, @RequestParam(required = false) String period) {
        return employeeService.findPayments(auth, period);
    }

    /**
     * uploads payrolls
     */
    @PostMapping("/acct/payments")
    public ResponseEntity<?> postPayrolls(@RequestBody List<Payment> payments) {
        return employeeService.savePayrolls(payments);
    }

    /**
     * changes the salary of a specific user
     */
    @PutMapping("/acct/payments")
    public ResponseEntity<?> putPayroll(@RequestBody Payment payment) {
        return employeeService.updatePayroll(payment);
    }
}}