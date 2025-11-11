package leonardoferrante.progettofinale.services;

import leonardoferrante.progettofinale.DTO.UserRegisterDto;
import leonardoferrante.progettofinale.DTO.UserResponseDto;
import leonardoferrante.progettofinale.entities.User;

import java.util.List;

public interface UserService {

    UserResponseDto registerUser(UserRegisterDto dto);

    List<UserResponseDto> getAllUsers();

    UserResponseDto getUserById(Long id);

    UserResponseDto updateUser(Long id, UserRegisterDto dto);

    void deleteUser(Long id);

    User findByEmail(String email);
}