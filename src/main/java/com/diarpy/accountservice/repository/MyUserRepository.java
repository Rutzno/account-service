package accountservice.repository;

import accountservice.entities.MyUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * @author Mack_TB
 * @since 23/06/2024
 * @version 1.0.4
 */

public interface MyUserRepository extends JpaRepository<MyUser, Long> {
    Optional<MyUser> findByEmailIgnoreCase(String email);
    boolean existsByEmailIgnoreCase(String email);
}