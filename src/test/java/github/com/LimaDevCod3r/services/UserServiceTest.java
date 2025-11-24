package github.com.LimaDevCod3r.services;

import github.com.LimaDevCod3r.exceptions.InsecureCredentialsException;
import github.com.LimaDevCod3r.exceptions.ResourcesAlreadyExistsException;
import github.com.LimaDevCod3r.models.User;
import github.com.LimaDevCod3r.models.enums.UserRole;
import github.com.LimaDevCod3r.repositories.UserRepository;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Testes para UserService")
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    @Nested
    @DisplayName("Testes para o método createUser")
    class CreateUserTests {

        @Test
        @DisplayName("Deve retornar o usuário salvo ao criar com sucesso")
        void whenCreateUser_thenReturnsSavedUser() {
            // Arrange
            User userToSave = new User();
            userToSave.setUsername("testuser");
            userToSave.setPassword("testpassword");
            userToSave.setRole(UserRole.USER);

            User savedUser = new User();
            savedUser.setId(UUID.randomUUID());
            savedUser.setUsername(userToSave.getUsername());
            savedUser.setPassword("encodedPassword"); // Senha já deve estar criptografada
            savedUser.setRole(userToSave.getRole());

            when(userRepository.save(any(User.class))).thenReturn(savedUser);
            when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");

            // Act
            User actualUser = userService.createUser(userToSave);

            // Assert
            assertNotNull(actualUser);
            assertEquals(savedUser.getId(), actualUser.getId());
            verify(userRepository).save(any(User.class));
        }

        @Test
        @DisplayName("Deve criptografar a senha ao criar um novo usuário")
        void whenCreateUser_thenPasswordShouldBeEncrypted() {
            // Arrange
            String rawPassword = "plainPassword123";
            String encodedPassword = "encodedPasswordABC";
            User userToSave = new User();
            userToSave.setUsername("cryptoUser");
            userToSave.setPassword(rawPassword);

            when(passwordEncoder.encode(rawPassword)).thenReturn(encodedPassword);
            when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

            // Act
            User savedUser = userService.createUser(userToSave);

            // Assert
            verify(passwordEncoder).encode(rawPassword);
            assertEquals(encodedPassword, savedUser.getPassword(), "A senha salva deve ser a versão criptografada");
            assertNotEquals(rawPassword, savedUser.getPassword());
        }

        @Test
        @DisplayName("Deve lançar exceção ao tentar criar usuário com username existente")
        void whenCreateUserWithSameUsername_thenThrowsResourcesAlreadyExistsException() {
            // Arrange
            String existingUsername = "LimaDev2001";
            User newUser = new User();
            newUser.setUsername(existingUsername);
            newUser.setPassword("password123");

            when(userRepository.findByUsername(existingUsername)).thenReturn(Optional.of(new User()));

            // Act & Assert
            ResourcesAlreadyExistsException exception = assertThrows(
                    ResourcesAlreadyExistsException.class,
                    () -> userService.createUser(newUser)
            );
            assertEquals(String.format("O nome de usuário '%s' já está em uso.", newUser.getUsername()), exception.getMessage());
            verify(userRepository, never()).save(any(User.class));
        }

        @Test
        @DisplayName("Deve lançar exceção ao criar usuário com senha semelhante ao username")
        void whenCreateUserWithSimilarUsernameAndPassword_thenThrowsInsecureCredentialsException() {
            // Arrange
            User userWithSimilarCredentials = new User();
            userWithSimilarCredentials.setUsername("LimaDev2007");
            userWithSimilarCredentials.setPassword("LimaDev2004");

            // Act & Assert
            InsecureCredentialsException exception = assertThrows(
                    InsecureCredentialsException.class,
                    () -> userService.createUser(userWithSimilarCredentials)
            );
            assertEquals("O nome de usuário não pode ser igual ou semelhante à senha.", exception.getMessage());
            verify(userRepository, never()).save(any(User.class));
        }
    }


}