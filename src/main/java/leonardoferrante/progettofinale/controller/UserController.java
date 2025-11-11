package leonardoferrante.progettofinale.controller;

import jakarta.validation.Valid;
import leonardoferrante.progettofinale.DTO.UserRegisterDto;
import leonardoferrante.progettofinale.DTO.UserResponseDto;
import leonardoferrante.progettofinale.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    //registrazione utente
    @PostMapping("/register")
    public ResponseEntity<UserResponseDto> registerUser(@Valid @RequestBody UserRegisterDto dto) {
        UserResponseDto createdUser = userService.registerUser(dto);
        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
    }

    //trovate tutti gli utenti
    @GetMapping
    public ResponseEntity<List<UserResponseDto>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    //trovare utente tramite id
    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDto> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    //aggiornare un utente
    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDto> updateUSer(
            @PathVariable Long id,
            @Valid @RequestBody UserRegisterDto dto) {
        return ResponseEntity.ok(userService.updateUser(id, dto));
    }

    //elimina utente
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
