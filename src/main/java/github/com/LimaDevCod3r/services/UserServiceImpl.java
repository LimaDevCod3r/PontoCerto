package github.com.LimaDevCod3r.services;

import github.com.LimaDevCod3r.exceptions.InsecureCredentialsException;
import github.com.LimaDevCod3r.exceptions.ResourcesAlreadyExistsException;
import github.com.LimaDevCod3r.models.User;
import github.com.LimaDevCod3r.models.enums.UserRole;
import github.com.LimaDevCod3r.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.text.similarity.LevenshteinDistance;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl  implements UserService{

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private static final int SIMILARITY_THRESHOLD = 2;

    @Override
    public User createUser(User entity) {

        LevenshteinDistance levenshteinDistance = LevenshteinDistance.getDefaultInstance();
        int distance = levenshteinDistance.apply(entity.getUsername().toLowerCase(), entity.getPassword().toLowerCase());

        if (distance <= SIMILARITY_THRESHOLD) {
            throw new InsecureCredentialsException("O nome de usuário não pode ser igual ou semelhante à senha.");
        }

        userRepository.findByUsername(entity.getUsername()).ifPresent(u -> {
            throw new ResourcesAlreadyExistsException(
                    String.format("O nome de usuário '%s' já está em uso.", entity.getUsername())
            );
        });
        // Criptografa a senha antes de salvar
        entity.setPassword(passwordEncoder.encode(entity.getPassword()));
        entity.setRole(UserRole.USER);
        return userRepository.save(entity);
    }
}
