package emanuela.carrubba.matillo_bakery.controllers;

import emanuela.carrubba.matillo_bakery.ResponseDTO.AuthResponseDTO;
import emanuela.carrubba.matillo_bakery.RequestDTO.LoginRequestDTO;
import emanuela.carrubba.matillo_bakery.RequestDTO.RichiediResetRequestDTO;
import emanuela.carrubba.matillo_bakery.RequestDTO.ResetPasswordRequestDTO;
import emanuela.carrubba.matillo_bakery.Tools.EmailSender;
import emanuela.carrubba.matillo_bakery.config.JwtService;
import emanuela.carrubba.matillo_bakery.entities.User;
import emanuela.carrubba.matillo_bakery.exceptions.NotFoundException;
import emanuela.carrubba.matillo_bakery.services.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final EmailSender emailSender;

    public AuthController(UserService userService, PasswordEncoder passwordEncoder,
                          JwtService jwtService, EmailSender emailSender) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.emailSender = emailSender;
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

    // POST /api/auth/richiedi-reset — pubblico.
    // Risponde sempre 200, esista o no quell'email: altrimenti il
    // messaggio di risposta diventerebbe un modo per scoprire quali
    // indirizzi sono registrati sul sito.
    @PostMapping("/richiedi-reset")
    public ResponseEntity<String> richiediReset(@Valid @RequestBody RichiediResetRequestDTO dto) {
        Optional<User> utenteOpt = userService.generaTokenReset(dto.email());

        utenteOpt.ifPresent(utente -> {
            try {
                emailSender.sendPasswordResetEmail(
                        utente.getEmail(), utente.getNome(), utente.getResetToken()
                );
            } catch (Exception e) {
                System.err.println("[AuthController] Invio email di reset fallito per "
                        + utente.getEmail() + ": " + e.getMessage());
            }
        });

        return ResponseEntity.ok(
                "Se l'email è registrata, riceverai a breve un link per reimpostare la password."
        );
    }

    // POST /api/auth/reset-password — pubblico, protetto dal token stesso
    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@Valid @RequestBody ResetPasswordRequestDTO dto) {
        try {
            userService.resettaPassword(dto.token(), dto.nuovaPassword());
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }

        return ResponseEntity.ok("Password aggiornata con successo.");
    }
}