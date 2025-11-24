package github.com.LimaDevCod3r.models.enums;

import lombok.Getter;

@Getter
public enum UserRole {
    USER("ROLE_USER",0),
    RH("ROLE_RH",1),
    SYSADMIN("ROLE_SYSADMIN",2);

    private final String role;
    private final int level;

    UserRole(String role, int level) {
        this.role = role;
        this.level = level;
    }

}