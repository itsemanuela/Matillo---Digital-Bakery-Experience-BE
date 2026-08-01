package emanuela.carrubba.matillo_bakery.controllers;

import emanuela.carrubba.matillo_bakery.RequestDTO.UtenteRequestDTO;
import emanuela.carrubba.matillo_bakery.entities.User;
import emanuela.carrubba.matillo_bakery.services.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/utenti")
// TODO IMPORTANTE (sicurezza): User probabilmente contiene il campo password.

public class UserController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    public UserController(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    // GET /api/utenti — lista completa
    @GetMapping
    public ResponseEntity<List<User>> getAllUtenti() {
        return ResponseEntity.ok(userService.trovaTutti());
    }

    // GET /api/utenti/{id} — dettaglio singolo utente
    @GetMapping("/{id}")
    public ResponseEntity<User> getUtenteById(@PathVariable UUID id) {
        User utente = userService.trovaPerId(id);
        return ResponseEntity.ok(utente);
    }

    // GET /api/utenti/email/{email} — ricerca per email
    @GetMapping("/email/{email}")
    public ResponseEntity<User> getUtenteByEmail(@PathVariable String email) {
        User utente = userService.trovaPerEmail(email);
        return ResponseEntity.ok(utente);
    }

    // POST /api/utenti — crea un nuovo utente
    //  BCrypt prima del salvataggio:

    @PostMapping
    public ResponseEntity<User> createUtente(@Valid @RequestBody UtenteRequestDTO dto) {
        User user = new User();
        user.setNome(dto.nome());
        user.setCognome(dto.cognome());
        user.setEmail(dto.email());
        user.setTelefono(dto.telefono());
        user.setIndirizzo(dto.indirizzo());
        user.setCap(dto.cap());
        user.setCitta(dto.città());
        user.setPassword(passwordEncoder.encode(dto.password()));

        User salvato = userService.salvaUtente(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(salvato);
    }


    // DELETE /api/utenti/{id} — elimina un utente
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUtente(@PathVariable UUID id) {
        userService.eliminaUtenteById(id);
        return ResponseEntity.noContent().build();
    }
}