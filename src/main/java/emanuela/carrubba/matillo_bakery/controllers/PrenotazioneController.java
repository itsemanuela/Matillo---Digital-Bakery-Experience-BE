package emanuela.carrubba.matillo_bakery.controllers;

import emanuela.carrubba.matillo_bakery.RequestDTO.PrenotazioneRequestDTO;
import emanuela.carrubba.matillo_bakery.entities.Laboratorio;
import emanuela.carrubba.matillo_bakery.entities.Prenotazione;
import emanuela.carrubba.matillo_bakery.entities.StatoPrenotazione;
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
    public ResponseEntity<List<Prenotazione>> getAllPrenotazioni(
            @RequestParam(required = false) UUID laboratorioId,
            @RequestParam(required = false) StatoPrenotazione stato) {

        List<Prenotazione> prenotazioni;

        if (laboratorioId != null && stato != null) {
            prenotazioni = prenotazioneService.trovaPerLaboratorioEStato(laboratorioId, stato);
        } else if (laboratorioId != null) {
            prenotazioni = prenotazioneService.trovaPerLaboratorio(laboratorioId);
        } else if (stato != null) {
            prenotazioni = prenotazioneService.trovaPerStato(stato);
        } else {
            prenotazioni = prenotazioneService.trovaTutte();
        }

        return ResponseEntity.ok(prenotazioni);
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
        String email = authentication.getName();
        User utente = userService.trovaPerEmail(email);

        if (prenotazioneService.esistePrenotazioneAttiva(laboratorio, utente)) {
            throw new IllegalArgumentException(
                    "Hai già una prenotazione attiva per \"" + laboratorio.getNome() + "\"."
            );
        }

        Prenotazione prenotazione = new Prenotazione(laboratorio, utente, dto.numeroPersone());

        laboratorio.setPostiDisponibili(laboratorio.getPostiDisponibili() - dto.numeroPersone());
        laboratorioService.salva(laboratorio);

        Prenotazione salvata = prenotazioneService.salva(prenotazione);
        return ResponseEntity.status(HttpStatus.CREATED).body(salvata);
    }

    @PatchMapping("/{id}/cancella")
    public ResponseEntity<Prenotazione> cancellaPrenotazione(@PathVariable UUID id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        User utente = userService.trovaPerEmail(email);

        Prenotazione prenotazione = prenotazioneService.trovaPerId(id);


        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        if (!isAdmin && !prenotazione.getUtente().equals(utente)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        if (prenotazione.getStato() != StatoPrenotazione.CANCELLATA) {
            Laboratorio laboratorio = prenotazione.getLaboratorio();
            laboratorio.setPostiDisponibili(laboratorio.getPostiDisponibili() + prenotazione.getNumeroPersone());
            laboratorioService.salva(laboratorio);

            prenotazione.setStato(StatoPrenotazione.CANCELLATA);
            prenotazioneService.salva(prenotazione);
        }

        return ResponseEntity.ok(prenotazione);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Prenotazione> aggiornaPrenotazione(
            @PathVariable UUID id,
            @RequestBody Prenotazione datiAggiornati) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        User utente = userService.trovaPerEmail(email);

        Prenotazione prenotazioneAggiornata = prenotazioneService.modificaPrenotazione(id, utente, datiAggiornati);
        return ResponseEntity.ok(prenotazioneAggiornata);
    }
}