package leonardoferrante.progettofinale.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import leonardoferrante.progettofinale.DTO.JwtResponse;
import leonardoferrante.progettofinale.DTO.LoginRequest;
import leonardoferrante.progettofinale.DTO.RegisterRequest;
import leonardoferrante.progettofinale.DTO.UserRegisterDto;
import leonardoferrante.progettofinale.entities.User;
import leonardoferrante.progettofinale.security.JwtUtils;
import leonardoferrante.progettofinale.services.UserService;
import org.springframework.http.HttpHeaders;
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

        // Autenticazione
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getEmail(),
                        loginRequest.getPassword()
                )
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);

        //  Recuperiamo l'utente dal DB
        User user = userService.getUserByEmail(loginRequest.getEmail());
        if (user == null) {
            return ResponseEntity.status(404).body(null);
        }

        //  Generiamo JWT
        String jwt = jwtUtils.generateJwtToken(user);

        //  Creiamo cookie compatibile con localhost
        ResponseCookie cookie = ResponseCookie.from("jwt", jwt)
                .httpOnly(true)
                .secure(false)       // false per localhost
                .path("/")
                .maxAge(7 * 24 * 60 * 60)
                .sameSite("Lax")    // Lax funziona su localhost
                .build();

        // Risposta con cookie e body
        JwtResponse response = new JwtResponse(
                jwt,
                "Bearer",
                user.getEmail(),
                user.getRole().name()
        );

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(response);
    }



    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser(HttpServletRequest request) {

        String jwt = jwtUtils.getJwtFromCookies(request);
        if(jwt == null || !jwtUtils.validateJwtToken(jwt)) {
            return ResponseEntity.status(401).body("TOken non valido");
        }

        String email = jwtUtils.getUsernameFromToken(jwt);
        User user = userService.getUserByEmail(email);

        return ResponseEntity.ok(new JwtResponse(
                null,
                "Bearer",
                user.getEmail(),
                user.getRole().name()
        ));
    }
}



