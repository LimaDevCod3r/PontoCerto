package github.com.LimaDevCod3r.models.enums;

import lombok.Getter;

@Getter
public enum UserRole {
    USER("ROLE_USER"),
    RH("ROLE_RH"),
    SYSADMIN("ROLE_SYSADMIN");

    private final String role;

    UserRole(String role) {
        this.role = role;
    }

}