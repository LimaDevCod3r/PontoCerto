package github.com.LimaDevCod3r.dtos.user;

import github.com.LimaDevCod3r.models.enums.UserRole;

import java.util.UUID;

public record UserResponseDto(UUID id, String username,UserRole role) {
}
