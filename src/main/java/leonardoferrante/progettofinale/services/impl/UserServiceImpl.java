package leonardoferrante.progettofinale.services.impl;

import jakarta.persistence.EntityNotFoundException;
import leonardoferrante.progettofinale.DTO.UserRegisterDto;
import leonardoferrante.progettofinale.DTO.UserResponseDto;
import leonardoferrante.progettofinale.DTO.UserUpdateDto;
import leonardoferrante.progettofinale.entities.Role;
import leonardoferrante.progettofinale.repository.UserRepository;
import leonardoferrante.progettofinale.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import leonardoferrante.progettofinale.entities.User;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

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

        System.out.println(" [REGISTER] Email libera");

        System.out.println(" [REGISTER] Provo a criptare la password...");
        String encrypted = passwordEncoder.encode(dto.getPassword());
        System.out.println(" [REGISTER] Password criptata: " + encrypted);

        User user = User.builder()
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .email(dto.getEmail())
                .password(passwordEncoder.encode(dto.getPassword()))
                .role(Role.ROLE_USER)
                .build();


        System.out.println(" [REGISTER] Creo user: " + user);

        userRepository.save(user);

        System.out.println(" [REGISTER] Utente salvato nel database!");

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
    public UserResponseDto updateUser(Long id, UserUpdateDto dto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Utente non trovato"));

        if (dto.getFirstName() != null) user.setFirstName(dto.getFirstName());
        if (dto.getLastName() != null) user.setLastName(dto.getLastName());
        if (dto.getEmail() != null) user.setEmail(dto.getEmail());


        if (dto.getRole() != null && !dto.getRole().isBlank()) {
            try {
                user.setRole(Role.valueOf(dto.getRole()));
            } catch (IllegalArgumentException ex) {
                // Ruolo non valido -> 400 Bad Request
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Ruolo non valido: " + dto.getRole());
            }
        }

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
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("Utente non trovato con email: " + email));
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
