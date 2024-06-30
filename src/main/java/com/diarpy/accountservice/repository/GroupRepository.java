package accountservice.repository;

import accountservice.entities.Group;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * @author Mack_TB
 * @since 23/06/2024
 * @version 1.0.5
 */

public interface GroupRepository extends JpaRepository<Group, Long> {
    //    Group findByName(String name);
    Optional<Group> findByName(String name);
    boolean existsByName(String name);
}