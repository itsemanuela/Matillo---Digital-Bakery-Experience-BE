package emanuela.carrubba.matillo_bakery.controllers;

import emanuela.carrubba.matillo_bakery.RequestDTO.PrenotazioneRequestDTO;
import emanuela.carrubba.matillo_bakery.entities.StatoPrenotazione;
import emanuela.carrubba.matillo_bakery.entities.Laboratorio;
import emanuela.carrubba.matillo_bakery.entities.Prenotazione;
import emanuela.carrubba.matillo_bakery.entities.User;
import emanuela.carrubba.matillo_bakery.exceptions.QuantitaNonDisponibileException;
import emanuela.carrubba.matillo_bakery.services.LaboratorioService;
import emanuela.carrubba.matillo_bakery.services.PrenotazioneService;
import emanuela.carrubba.matillo_bakery.services.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/prenotazioni")
public class PrenotazioneController {

    private final PrenotazioneService prenotazioneService;
    private final LaboratorioService laboratorioService;
    private final UserService userService;

    public PrenotazioneController(PrenotazioneService prenotazioneService, LaboratorioService laboratorioService,
                                  UserService userService) {
        this.prenotazioneService = prenotazioneService;
        this.laboratorioService = laboratorioService;
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<Prenotazione>> getAllPrenotazioni() {
        return ResponseEntity.ok(prenotazioneService.trovaTutte());
    }

    @GetMapping("/me")
    public ResponseEntity<List<Prenotazione>> getMiePrenotazioni() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        User utente = userService.trovaPerEmail(email);
        return ResponseEntity.ok(prenotazioneService.trovaPerUtente(utente));
    }

    @PostMapping
    public ResponseEntity<Prenotazione> createPrenotazione(@Valid @RequestBody PrenotazioneRequestDTO dto) {
        Laboratorio laboratorio = laboratorioService.trovaPerId(dto.laboratorioId());

        if (laboratorio.getPostiDisponibili() < dto.numeroPersone()) {
            throw new QuantitaNonDisponibileException(
                    "Posti non disponibili per \"" + laboratorio.getNome() + "\": " +
                            "richiesti " + dto.numeroPersone() + ", disponibili " + laboratorio.getPostiDisponibili()
            );
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        boolean isLoggato = authentication != null
                && authentication.isAuthenticated()
                && !"anonymousUser".equals(authentication.getPrincipal());

        if (isBlank(dto.nomeCliente()) || isBlank(dto.cognomeCliente())
                || isBlank(dto.emailCliente()) || isBlank(dto.telefonoCliente())) {
            throw new IllegalArgumentException(
                    "Nome, cognome, email e telefono sono obbligatori per la prenotazione."
            );
        }

        Prenotazione prenotazione;

        if (isLoggato) {
            String email = authentication.getName();
            User utente = userService.trovaPerEmail(email);

            if (prenotazioneService.esistePrenotazioneAttiva(laboratorio, utente)) {
                throw new IllegalArgumentException(
                        "Hai già una prenotazione attiva per \"" + laboratorio.getNome() + "\"."
                );
            }

            prenotazione = new Prenotazione(laboratorio, utente, dto.numeroPersone());
        } else {
            if (prenotazioneService.esistePrenotazioneAttiva(laboratorio, dto.emailCliente())) {
                throw new IllegalArgumentException(
                        "Esiste già una prenotazione attiva per \"" + laboratorio.getNome() + "\" con questa email."
                );
            }

            prenotazione = new Prenotazione(
                    laboratorio, dto.nomeCliente(), dto.cognomeCliente(),
                    dto.emailCliente(), dto.telefonoCliente(), dto.numeroPersone()
            );
        }

        prenotazione.setNomeCliente(dto.nomeCliente());
        prenotazione.setCognomeCliente(dto.cognomeCliente());
        prenotazione.setEmailCliente(dto.emailCliente());
        prenotazione.setTelefonoCliente(dto.telefonoCliente());

        laboratorio.setPostiDisponibili(laboratorio.getPostiDisponibili() - dto.numeroPersone());
        laboratorioService.salva(laboratorio);

        Prenotazione salvata = prenotazioneService.salva(prenotazione);
        return ResponseEntity.status(HttpStatus.CREATED).body(salvata);
    }

    @PatchMapping("/{id}/cancella")
    public ResponseEntity<Prenotazione> cancellaPrenotazione(@PathVariable UUID id) {
        Prenotazione prenotazione = prenotazioneService.trovaPerId(id);

        if (prenotazione.getStato() != StatoPrenotazione.CANCELLATA) {
            Laboratorio laboratorio = prenotazione.getLaboratorio();
            laboratorio.setPostiDisponibili(laboratorio.getPostiDisponibili() + prenotazione.getNumeroPersone());
            laboratorioService.salva(laboratorio);

            prenotazione.setStato(StatoPrenotazione.CANCELLATA);
            prenotazioneService.salva(prenotazione);
        }

        return ResponseEntity.ok(prenotazione);
    }

    private boolean isBlank(String s) {
        return s == null || s.isBlank();
    }

    @GetMapping("/cerca") //per utenti non registrati
    public ResponseEntity<List<Prenotazione>> getPrenotazioniPerEmail(@RequestParam String email) {
        List<Prenotazione> prenotazioni = prenotazioneService.trovaPerEmailCliente(email);
        return ResponseEntity.ok(prenotazioni);
    }

}