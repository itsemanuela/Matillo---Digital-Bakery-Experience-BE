package emanuela.carrubba.matillo_bakery.controllers;


import emanuela.carrubba.matillo_bakery.entities.Ordine;
import emanuela.carrubba.matillo_bakery.entities.User;
import emanuela.carrubba.matillo_bakery.services.OrdineService;
import emanuela.carrubba.matillo_bakery.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
    @RequestMapping("/api/ordini")
// TODO: proteggere questi endpoint quando l'autenticazione sarà pronta.
    public class OrdineController {

        private final OrdineService ordineService;
        private final UserService userService;

        public OrdineController(OrdineService ordineService, UserService userService) {
            this.ordineService = ordineService;
            this.userService = userService;
        }

        // GET /api/ordini — lista completa (in futuro: solo per admin)
        @GetMapping
        public ResponseEntity<List<Ordine>> getAllOrdini() {
            return ResponseEntity.ok(ordineService.trovaTutti());
        }

        // GET /api/ordini/{id} — dettaglio singolo ordine
        @GetMapping("/{id}")
        public ResponseEntity<Ordine> getOrdineById(@PathVariable UUID id) {
            Ordine ordine = ordineService.trovaPerId(id);
            return ResponseEntity.ok(ordine);
        }

        // GET /api/ordini/utente/{utenteId} — tutti gli ordini di un utente
        @GetMapping("/utente/{utenteId}")
        public ResponseEntity<List<Ordine>> getOrdiniByUtente(@PathVariable UUID utenteId) {
            User utente = userService.trovaPerId(utenteId);
            List<Ordine> ordini = ordineService.trovaPerUtente(utente);
            return ResponseEntity.ok(ordini);
        }

        // POST /api/ordini — crea un nuovo ordine
        @PostMapping
        public ResponseEntity<Ordine> createOrdine(@RequestBody Ordine ordine) {
            Ordine salvato = ordineService.salva(ordine);
            return ResponseEntity.status(HttpStatus.CREATED).body(salvato);
        }

        // DELETE /api/ordini/{id} — elimina un ordine
        @DeleteMapping("/{id}")
        public ResponseEntity<Void> deleteOrdine(@PathVariable UUID id) {
            ordineService.elimina(id);
            return ResponseEntity.noContent().build();
        }
    }

