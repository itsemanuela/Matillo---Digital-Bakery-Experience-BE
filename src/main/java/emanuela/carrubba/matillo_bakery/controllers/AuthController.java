package emanuela.carrubba.matillo_bakery.controllers;

import emanuela.carrubba.matillo_bakery.ResponseDTO.AuthResponseDTO;
import emanuela.carrubba.matillo_bakery.RequestDTO.LoginRequestDTO;
import emanuela.carrubba.matillo_bakery.config.JwtService;
import emanuela.carrubba.matillo_bakery.entities.User;
import emanuela.carrubba.matillo_bakery.services.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthController(UserService userService, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    // POST /api/auth/login
    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequestDTO dto) {
        User utente;
        try {
            utente = userService.trovaPerEmail(dto.email());
        } catch (Exception e) {

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Email o password non corretti");
        }

        if (!passwordEncoder.matches(dto.password(), utente.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Email o password non corretti");
        }

        String token = jwtService.generaToken(utente);

        AuthResponseDTO risposta = new AuthResponseDTO(
                token,
                utente.getUuid(),
                utente.getNome(),
                utente.getEmail(),
                utente.getRuolo().name()
        );

        return ResponseEntity.ok(risposta);
    }
}