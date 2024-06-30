package accountservice.repository;

import accountservice.entities.Payment;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    boolean existsByEmployeeAndPeriod(String employee, String period);
    List<Payment> findByEmployee(String employee, Sort sort);
    Payment findByEmployeeAndPeriod(String employee, String period);
}