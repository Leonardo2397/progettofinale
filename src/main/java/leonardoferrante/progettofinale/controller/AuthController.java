package leonardoferrante.progettofinale.controller;

import jakarta.validation.Valid;
import leonardoferrante.progettofinale.DTO.JwtResponse;
import leonardoferrante.progettofinale.DTO.LoginRequest;
import leonardoferrante.progettofinale.DTO.RegisterRequest;
import leonardoferrante.progettofinale.DTO.UserRegisterDto;
import leonardoferrante.progettofinale.entities.User;
import leonardoferrante.progettofinale.security.JwtUtils;
import leonardoferrante.progettofinale.services.UserService;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
    public ResponseEntity<JwtResponse> login(@RequestBody LoginRequest loginRequest) {

        System.out.println("DEBUG EMAIL: " + loginRequest.getEmail());
        System.out.println("DEBUG PASSWORD: " + loginRequest.getPassword());

        // 1️⃣ Autenticazione tramite AuthenticationManager
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getEmail(),
                        loginRequest.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        // 2️⃣ Recuperiamo l'entità User dal DB tramite UserService
        User user = userService.getUserByEmail(loginRequest.getEmail());
        if (user == null) {
            return ResponseEntity.status(404).body(null); // Utente non trovato (per sicurezza)
        }

        // 3️⃣ Generiamo JWT usando l'entità User
        String jwt = jwtUtils.generateJwtToken(user);

        // CREA IL COOKIE JWT
        ResponseCookie cookie = ResponseCookie.from("jwt", jwt)
                .httpOnly(true)
                .secure(false) // metti true se usi HTTPS
                .path("/")
                .maxAge(7 * 24 * 60 * 60)
                .sameSite("None")
                .build();
        // 4️⃣ Creiamo la risposta
        JwtResponse response = new JwtResponse(
                jwt,
                "Bearer",
                user.getEmail(),
                user.getRole().name()
        );

        return ResponseEntity.ok(response);
    }




}



