package accountservice.repository;

import accountservice.entities.MyUser;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

/**
 * @author Mack_TB
 * @since 23/06/2024
 * @version 1.0.6
 */

public interface MyUserRepository extends JpaRepository<MyUser, Long> {
    Optional<MyUser> findByEmailIgnoreCase(String email);
    boolean existsByEmailIgnoreCase(String email);
    MyUser findByEmail(String email);

    @Modifying
    @Transactional
    @Query("UPDATE MyUser u SET u.failedLoginAttempts = ?1 WHERE u.email = ?2")
    void updateFailedLoginAttempts(int failedLoginAttempts, String email);
}