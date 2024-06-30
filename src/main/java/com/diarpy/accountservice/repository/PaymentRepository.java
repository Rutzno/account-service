package accountservice.repository;

import accountservice.entities.Payment;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author Mack_TB
 * @since 23/06/2024
 * @version 1.0.4
 */

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    boolean existsByEmployeeAndPeriod(String employee, String period);
    List<Payment> findByEmployee(String employee, Sort sort);
    Payment findByEmployeeAndPeriod(String employee, String period);
}