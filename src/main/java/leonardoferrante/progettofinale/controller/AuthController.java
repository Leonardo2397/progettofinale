package leonardoferrante.progettofinale.controller;

import jakarta.validation.Valid;
import leonardoferrante.progettofinale.DTO.JwtResponse;
import leonardoferrante.progettofinale.DTO.LoginRequest;
import leonardoferrante.progettofinale.DTO.RegisterRequest;
import leonardoferrante.progettofinale.DTO.UserRegisterDto;
import leonardoferrante.progettofinale.entities.User;
import leonardoferrante.progettofinale.security.JwtUtils;
import leonardoferrante.progettofinale.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final UserService userService;

    public AuthController(AuthenticationManager authenticationManager,
                          JwtUtils jwtUtils,
                          UserService userService) {
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
        this.userService = userService;
    }

//    @PostMapping("/register")
//    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest req) {
//        try {
//            userService.registerUser(new UserRegisterDto(
//                    req.getFirstName(),
//                    req.getLastName(),
//                    req.getEmail(),
//                    req.getPassword()
//            ));
//            return ResponseEntity.status(201).body("Utente registrato con successo");
//        } catch (Exception e) {
//            e.printStackTrace();
//            return ResponseEntity.status(500).body("Errore nella registrazione: " + e.getMessage());
//        }
//    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest req) {

        try {
            userService.registerUser(new UserRegisterDto(
                    req.getFirstName(),
                    req.getLastName(),
                    req.getEmail(),
                    req.getPassword()
            ));

            return ResponseEntity.status(201).body("Utente registrato con successo");

        } catch (IllegalArgumentException e) {
            // Email già esistente
            return ResponseEntity.status(400).body(e.getMessage());

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Errore interno al server");
        }
    }


    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest req) {

        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(req.getEmail(), req.getPassword())
        );

        User user = userService.findByEmail(req.getEmail());

        String jwt = jwtUtils.generateJwtToken(user);

        return ResponseEntity.ok(new JwtResponse(jwt, "Bearer", user.getEmail(), user.getRole().name()));
    }
}



