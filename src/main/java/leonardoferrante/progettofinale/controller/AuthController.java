package leonardoferrante.progettofinale.controller;

import jakarta.validation.Valid;
import leonardoferrante.progettofinale.DTO.*;
import leonardoferrante.progettofinale.entities.User;
import leonardoferrante.progettofinale.services.UserService;
import leonardoferrante.progettofinale.security.JwtUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    public AuthController(AuthenticationManager authenticationManager,
                          JwtUtils jwtUtils,
                          UserService userService,
                          PasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest req) {
        // usa userService.registerUser ma assicurati che salvi password hashata
        // Qui mostro come creare direttamente (opzione rapida) sfruttando UserService.registerUser
        // Se UserService.registerUser già fa hashing allora basta chiamarlo qui.
        userService.registerUser(new leonardoferrante.progettofinale.DTO.UserRegisterDto(
                req.getFirstName(),
                req.getLastName(),
                req.getEmail(),
                passwordEncoder.encode(req.getPassword())
        ));
        return ResponseEntity.status(201).build();
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest req) {
        try {
            Authentication auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(req.getEmail(), req.getPassword())
            );

            // authentication success
            String jwt = jwtUtils.generateJwtToken(req.getEmail());

            // recupera ruolo
            User user = userService.findByEmail(req.getEmail());
            String role = user.getRole().name();

            return ResponseEntity.ok(new JwtResponse(jwt, "Bearer", req.getEmail(), role));
        } catch (BadCredentialsException ex) {
            return ResponseEntity.status(401).body("Credenziali non valide");
        }
    }
}
