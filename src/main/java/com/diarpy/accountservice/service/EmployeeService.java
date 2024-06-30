package accountservice.service;

import accountservice.dtos.UserPaymentDto;
import accountservice.entities.MyUser;
import accountservice.entities.Payment;
import accountservice.exception.PaymentException;
import accountservice.repository.MyUserRepository;
import accountservice.repository.PaymentRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;
import org.springframework.web.server.ResponseStatusException;

import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Mack_TB
 * @since 23/06/2024
 * @version 1.0.4
 */

@Service
public class EmployeeService {
    private final MyUserRepository myUserRepository;
    private final PaymentRepository paymentRepository;

    @Autowired
    public EmployeeService(MyUserRepository myUserRepository, PaymentRepository paymentRepository) {
        this.myUserRepository = myUserRepository;
        this.paymentRepository = paymentRepository;
    }

    public MyUser getUserByEmail(Authentication auth) {
        return myUserRepository.findByEmailIgnoreCase(auth.getName())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "User not found"));
    }

    @Transactional
    public ResponseEntity<?> savePayrolls(List<Payment> payments) {
        checkPaymentsValidity(payments);
        paymentRepository.saveAll(payments);
        Map<String, String> response = Map.of("status", "Added successfully!");
        return ResponseEntity.ok(response);
    }

    private void checkPaymentsValidity(List<Payment> payments) {
        StringBuilder errorMessage = new StringBuilder();
        List<Payment> processedPayments = new ArrayList<>();
        for (int i = 0; i < payments.size(); i++) {
            Payment payment = payments.get(i);
            if (payment.getSalary() < 0) {
                errorMessage.append("payments[%d].salary: Salary must be non negative!\n".formatted(i));
            }
            if (!isValidDate(payment.getPeriod())) {
                errorMessage.append("payments[%d].period: Wrong date!\n".formatted(i));
            }
            if (paymentRepository.existsByEmployeeAndPeriod(payment.getEmployee(), payment.getPeriod())) {
                errorMessage.append("payments[%d].period: Salary already exists!\n".formatted(i));
            }
            if (processedPayments.contains(payment)) {
                errorMessage.append("Error!");
            }
            if (!myUserRepository.existsByEmailIgnoreCase(payment.getEmployee())) {
                errorMessage.append("payments[%d].employee: Employee does not exist!\n".formatted(i));
            }
            processedPayments.add(payment);
        }
        if (!errorMessage.isEmpty()) {
            throw new PaymentException(errorMessage.toString());
        }
    }

    private boolean isValidDate(String dateStr) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-yyyy"); // Uppercase MM for month
        try {
            YearMonth yearMonth = YearMonth.parse(dateStr, formatter);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    @Transactional
    public ResponseEntity<?> updatePayroll(Payment payment) {
        StringBuilder errorMessage = new StringBuilder();
        if (payment.getSalary() < 0) {
            errorMessage.append("Salary must be non negative! ");
        }
        if (!isValidDate(payment.getPeriod())) {
            errorMessage.append("Wrong date! ");
        }
        if (!errorMessage.isEmpty()) {
            throw new PaymentException(errorMessage.toString());
        }
        Payment updatedPayment = paymentRepository.findByEmployeeAndPeriod(payment.getEmployee(), payment.getPeriod());
        updatedPayment.setSalary(payment.getSalary());
        paymentRepository.save(updatedPayment);
        Map<String, String> response = Map.of("status", "Updated successfully!");
        return ResponseEntity.ok(response);
    }

    public List<UserPaymentDto> findPayments(Authentication auth, String period) {
        MyUser myUser = getUserByEmail(auth);
        List<UserPaymentDto> userPaymentDtos = new ArrayList<>();
        if (period == null) {
            Sort sortByPeriod = Sort.by(Sort.Direction.DESC, "period");
            List<Payment> payments = paymentRepository.findByEmployee(auth.getName(), sortByPeriod);
            for (Payment payment : payments) {
                userPaymentDtos.add(new UserPaymentDto(myUser, payment));
            }
        } else {
            if (!isValidDate(period)) {
                throw new PaymentException("Wrong date!");
            }
            Payment payment = paymentRepository.findByEmployeeAndPeriod(auth.getName(), period);
            if (payment != null) {
                userPaymentDtos.add(new UserPaymentDto(myUser, payment));
            }
        }
        return userPaymentDtos;
    }
}