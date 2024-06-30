package accountservice.enums;

/**
 * @author Mack_TB
 * @since 23/06/2024
 * @version 1.0.6
 */

public enum SecurityEventType {
    CREATE_USER,
    CHANGE_PASSWORD,
    ACCESS_DENIED,
    LOGIN_FAILED,
    GRANT_ROLE,
    REMOVE_ROLE,
    LOCK_USER,
    UNLOCK_USER,
    DELETE_USER,
    BRUTE_FORCE
}