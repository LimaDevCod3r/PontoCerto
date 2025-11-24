package github.com.LimaDevCod3r.dtos.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserCreateDTO(
        @NotBlank(message = "Username é obrigatório")
        @Size(min = 6, max = 20, message = "Username deve ter entre 6 e 20 caracteres")
        String username,
        @NotBlank(message = "Password é obrigatório")
        @Size(min = 8, max = 20, message = "Password deve ter entre 8 e 20 caracteres")
        String password

) {
}
