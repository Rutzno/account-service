package accountservice.repository;

import account.entities.MyUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * @author Mack_TB
 * @since 23/06/2024
 * @version 1.0.2
 */

public interface MyUserRepository extends JpaRepository<MyUser, Long> {
    Optional<MyUser> findByEmailIgnoreCase(String email);
}