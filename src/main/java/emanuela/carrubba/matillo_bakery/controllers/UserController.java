package emanuela.carrubba.matillo_bakery.controllers;

import emanuela.carrubba.matillo_bakery.entities.User;
import emanuela.carrubba.matillo_bakery.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/utenti")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
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
    @PostMapping
    public ResponseEntity<User> createUtente(@RequestBody User user) {
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
