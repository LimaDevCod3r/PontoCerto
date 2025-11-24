package github.com.LimaDevCod3r.config;

import github.com.LimaDevCod3r.models.User;
import github.com.LimaDevCod3r.models.enums.UserRole;
import github.com.LimaDevCod3r.models.enums.UserStatus;
import github.com.LimaDevCod3r.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DataInitializerTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private DataInitializer dataInitializer;

    @BeforeEach
    void setUp() {
        // Injetamos manualmente os valores que viriam do application.yaml
        ReflectionTestUtils.setField(dataInitializer, "initialAdminUsername", "testadmin");
        ReflectionTestUtils.setField(dataInitializer, "initialAdminPassword", "testpassword");
    }

    @Test
    @DisplayName("Deve criar usuário SYSADMIN quando nenhum existir")
    void whenSysAdminDoesNotExist_shouldCreateNewUser() throws Exception {
        // Arrange
        when(userRepository.existsByRole(UserRole.SYSADMIN)).thenReturn(false);
        when(passwordEncoder.encode("testpassword")).thenReturn("encodedPassword");

        // Act
        dataInitializer.run();

        // Assert
        // Capturamos o usuário que foi passado para o método save()
        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(userCaptor.capture());
        verify(passwordEncoder).encode("testpassword");

        User savedUser = userCaptor.getValue();
        assertThat(savedUser.getUsername()).isEqualTo("testadmin");
        assertThat(savedUser.getPassword()).isEqualTo("encodedPassword");
        assertThat(savedUser.getRole()).isEqualTo(UserRole.SYSADMIN);
        assertThat(savedUser.getStatus()).isEqualTo(UserStatus.ACTIVE);
    }

    @Test
    @DisplayName("Não deve criar usuário SYSADMIN se um já existir")
    void whenSysAdminExists_shouldNotCreateNewUser() throws Exception {
        // Arrange
        when(userRepository.existsByRole(UserRole.SYSADMIN)).thenReturn(true);

        // Act
        dataInitializer.run();

        // Assert
        // Verificamos que o método save() nunca foi chamado
        verify(userRepository, never()).save(any(User.class));
        verify(passwordEncoder, never()).encode(anyString());
    }
}
