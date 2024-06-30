package accountservice.repository;

import accountservice.entities.SecurityEvent;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Mack_TB
 * @since 23/06/2024
 * @version 1.0.6
 */

public interface SecurityEventRepository extends JpaRepository<SecurityEvent, Long> {

}