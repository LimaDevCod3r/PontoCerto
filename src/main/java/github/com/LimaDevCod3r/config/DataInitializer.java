package github.com.LimaDevCod3r.config;

import github.com.LimaDevCod3r.models.User;
import github.com.LimaDevCod3r.models.enums.UserRole;
import github.com.LimaDevCod3r.models.enums.UserStatus;
import github.com.LimaDevCod3r.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${app.initial-admin.username}")
    private String initialAdminUsername;

    @Value("${app.initial-admin.password}")
    private String initialAdminPassword;

    @Override
    public void run(String... args) throws Exception {
        // 1. Verifica se um SYSADMIN já existe para não criar de novo
        if (userRepository.existsByRole(UserRole.SYSADMIN)) {
            log.info("Usuário SYSADMIN já existe. Nenhuma ação necessária.");
            return;
        }

        // 2. Se não existir, cria o usuário
        log.info("Nenhum usuário SYSADMIN encontrado. Criando conta de administrador inicial...");

        // Cria o usuário SYSADMIN
        User admin = User.builder()
                .username(initialAdminUsername)
                .password(passwordEncoder.encode(initialAdminPassword))
                .role(UserRole.SYSADMIN)
                .status(UserStatus.ACTIVE)
                .build();

        // Salva o usuário no banco de dados
        userRepository.save(admin);

        log.info("Usuário SYSADMIN inicial criado com sucesso. Username: '{}'", initialAdminUsername);
    }
}
