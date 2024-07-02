package com.diarpy.accountservice.web;

import com.diarpy.accountservice.entities.Payment;
import com.diarpy.accountservice.service.EmployeeService;
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
    public ResponseEntity<?> getPayments(Authentication auth, @RequestParam(required = false) String period) {
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

    @GetMapping(value = {"/security/events", "/security/events/"})
    public ResponseEntity<?> getEvents() {
        return employeeService.findAllEvents();
    }
}