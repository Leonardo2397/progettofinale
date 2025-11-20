package leonardoferrante.progettofinale.services.impl;

import jakarta.persistence.EntityNotFoundException;
import leonardoferrante.progettofinale.DTO.UserRegisterDto;
import leonardoferrante.progettofinale.DTO.UserResponseDto;
import leonardoferrante.progettofinale.entities.Role;
import leonardoferrante.progettofinale.repository.UserRepository;
import leonardoferrante.progettofinale.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import leonardoferrante.progettofinale.entities.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserResponseDto registerUser(UserRegisterDto dto) {
        System.out.println("[Register] Arrivato dto: " + dto);

        if (userRepository.existsByEmail(dto.getEmail())) {
            System.out.println(" Email già registrata: " + dto.getEmail());
            throw new IllegalArgumentException("Email già registrata!");
        }

        System.out.println("📌 [REGISTER] Email libera");

        System.out.println("📌 [REGISTER] Provo a criptare la password...");
        String encrypted = passwordEncoder.encode(dto.getPassword());
        System.out.println("📌 [REGISTER] Password criptata: " + encrypted);

        User user = User.builder()
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .email(dto.getEmail())
                .password(passwordEncoder.encode(dto.getPassword()))
                .role(Role.ROLE_USER)
                .build();


        System.out.println("📌 [REGISTER] Creo user: " + user);

        userRepository.save(user);

        System.out.println("✅ [REGISTER] Utente salvato nel database!");

        return mapToResponse(user);
    }

    @Override
    public List<UserResponseDto> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public UserResponseDto getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Utente non trovato"));
        return mapToResponse(user);
    }

    @Override
    public UserResponseDto updateUser(Long id, UserRegisterDto dto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Utente non trovato"));


        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setEmail(dto.getEmail());

        userRepository.save(user);
        return mapToResponse(user);
    }

    @Override
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new EntityNotFoundException("Utente non trovato");
        }
        userRepository.deleteById(id);
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("Utente non trovato"));
    }

    private UserResponseDto mapToResponse(User user) {
        return new UserResponseDto(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getRole().name()
        );
    }
}
