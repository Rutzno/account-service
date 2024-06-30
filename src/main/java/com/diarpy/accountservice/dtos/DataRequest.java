package accountservice.dtos;

import accountservice.enums.Operation;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;

/**
 * @author Mack_TB
 * @since 23/06/2024
 * @version 1.0.6
 */

public class DataRequest {
    @NotBlank
    private String user;
    private String role;
    @Enumerated(EnumType.STRING)
    private Operation operation;

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Operation getOperation() {
        return operation;
    }

    public void setOperation(Operation operation) {
        this.operation = operation;
    }
}