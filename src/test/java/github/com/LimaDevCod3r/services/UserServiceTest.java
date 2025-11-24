package github.com.LimaDevCod3r.services;

import github.com.LimaDevCod3r.exceptions.InsecureCredentialsException;
import github.com.LimaDevCod3r.exceptions.ResourcesAlreadyExistsException;
import github.com.LimaDevCod3r.models.User;
import github.com.LimaDevCod3r.models.enums.UserRole;
import github.com.LimaDevCod3r.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.parameters.P;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.never;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;


    @InjectMocks
    private UserServiceImpl userService;


    @Test
    void whenCreateUser_thenReturnsSavedUser() {

        // Arrange (Given) - Preparamos os dados e o comportamento esperado.
        User userToSave = new User();
        userToSave.setUsername("testuser");
        userToSave.setRole(UserRole.USER);
        userToSave.setPassword("testpassword");

        // Este é o objeto que esperamos que o repositório retorne após salvar.
        User savedUser = new User();
        savedUser.setId(UUID.randomUUID());
        savedUser.setRole(userToSave.getRole());
        savedUser.setUsername(userToSave.getUsername());
        savedUser.setPassword(userToSave.getPassword());

        // Configuramos o mock: quando o método save for chamado, ele deve retornar nosso `savedUser`.
        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        // Act (When) - Executamos a ação que queremos testar.
        User actualUser = userService.createUser(userToSave);

        // Assert (Then) - Verificamos se o resultado é o esperado.
        assertNotNull(actualUser, "O usuário salvo não deve ser nulo");
        assertNotNull(actualUser.getId(), "O ID do usuário salvo não deve ser nulo");
        assertNotNull(actualUser.getUsername(), "O username do usuário salvo não deve ser nulo");
        assertNotNull(actualUser.getRole(), "O role do usuário salvo não deve ser nulo");
        assertNotNull(actualUser.getPassword(), "O password do usuário salvo não deve ser nulo");

        // verificar se o método `save` foi chamado no repositório.
        verify(userRepository).save(userToSave);

    }

    @Test
    void  whenCreateUser_thenPasswordShouldBeEncrypted(){
        String rawPassword = "plainPassword123";
        String encodedPassword = "encodedPasswordABC";

        User userToSave = new User();
        userToSave.setUsername("cryptoUser");
        userToSave.setPassword(rawPassword);

        when(passwordEncoder.encode(rawPassword)).thenReturn(encodedPassword);
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        User savedUser = userService.createUser(userToSave);

        verify(passwordEncoder).encode(rawPassword);

        assertNotEquals(rawPassword, savedUser.getPassword(), "A senha não deve ser salva em texto plano");

        assertEquals(encodedPassword, savedUser.getPassword(), "A senha salva deve ser a versão criptografada");
    }

    @Test
    void WhenCreateUserWithSameUsername_thenThrowsResourcesAlreadyExistsException() {

        // Arrange(Given)
        String existingUsername = "LimaDev2001";
        User newUser = new User();
        newUser.setUsername(existingUsername);
        newUser.setPassword("password123");

        // Simula que o repositório encontrou um usuário com o mesmo username.
        when(userRepository.findByUsername(existingUsername)).thenReturn(Optional.of(new User()));

        // Act & Assert (When & Then)
        // Verifica se a exceção correta é lançada.
        ResourcesAlreadyExistsException exception = assertThrows(
                ResourcesAlreadyExistsException.class,
                () -> userService.createUser(newUser)
        );

        // Verifica a mensagem da exceção.
        assertEquals( String.format("O nome de usuário '%s' já está em uso.", newUser.getUsername()), exception.getMessage());

        // Garante que o método save NUNCA foi chamado.
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void whenCreateUserWithSimilarUsernameAndPassword_thenThrowsInsecureCredentialsException() {
        // Arrange (Given)
        User userWithSimilarCredentials = new User();
        userWithSimilarCredentials.setUsername("LimaDev2007");
        userWithSimilarCredentials.setPassword("LimaDev2004");

        // Act & Assert (When & Then)
        InsecureCredentialsException exception = assertThrows(
                InsecureCredentialsException.class,
                () -> userService.createUser(userWithSimilarCredentials)
        );

        assertEquals("O nome de usuário não pode ser igual ou semelhante à senha.", exception.getMessage());

        verify(userRepository, never()).save(any(User.class));
    }
}
