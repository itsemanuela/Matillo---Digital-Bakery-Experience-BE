package emanuela.carrubba.matillo_bakery.controllers;

import emanuela.carrubba.matillo_bakery.RequestDTO.DettaglioOrdineRequestDTO;
import emanuela.carrubba.matillo_bakery.RequestDTO.OrdineRequestDTO;
import emanuela.carrubba.matillo_bakery.RequestDTO.OrdineStatoRequestDTO;
import emanuela.carrubba.matillo_bakery.exceptions.QuantitaNonDisponibileException;
import emanuela.carrubba.matillo_bakery.entities.DettaglioOrdine;
import emanuela.carrubba.matillo_bakery.entities.Ordine;
import emanuela.carrubba.matillo_bakery.entities.Prodotto;
import emanuela.carrubba.matillo_bakery.entities.User;
import emanuela.carrubba.matillo_bakery.services.OrdineService;
import emanuela.carrubba.matillo_bakery.services.ProdottoService;
import emanuela.carrubba.matillo_bakery.services.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/ordini")
public class OrdineController {

    private final OrdineService ordineService;
    private final UserService userService;
    private final ProdottoService prodottoService;

    public OrdineController(OrdineService ordineService, UserService userService, ProdottoService prodottoService) {
        this.ordineService = ordineService;
        this.userService = userService;
        this.prodottoService = prodottoService;
    }

    // GET /api/ordini — lista completa
    // TODO: in futuro andrebbe riservata solo agli admin con hasRole("ADMIN"))
    @GetMapping
    public ResponseEntity<List<Ordine>> getAllOrdini() {
        return ResponseEntity.ok(ordineService.trovaTutti());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Ordine> getOrdineById(@PathVariable UUID id) {
        Ordine ordine = ordineService.trovaPerId(id);
        return ResponseEntity.ok(ordine);
    }

    @GetMapping("/utente/{utenteId}")
    public ResponseEntity<List<Ordine>> getOrdiniByUtente(@PathVariable UUID utenteId) {
        User utente = userService.trovaPerId(utenteId);
        List<Ordine> ordini = ordineService.trovaPerUtente(utente);
        return ResponseEntity.ok(ordini);
    }

    // POST /api/ordini — pubblico: funziona sia per utenti loggati sia per ospiti.

    @PostMapping
    public ResponseEntity<Ordine> createOrdine(@Valid @RequestBody OrdineRequestDTO dto) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        boolean isLoggato = authentication != null
                && authentication.isAuthenticated()
                && !"anonymousUser".equals(authentication.getPrincipal());

        List<DettaglioOrdine> dettagli = new ArrayList<>();
        double totale = 0.0;

        for (DettaglioOrdineRequestDTO dettaglioDto : dto.dettagli()) {
            Prodotto prodotto = prodottoService.trovaPerId(dettaglioDto.idProdotto());
            int quantitaRichiesta = dettaglioDto.quantita();

            if (prodotto.getQuantità() < quantitaRichiesta) {
                throw new QuantitaNonDisponibileException(
                        "Quantità non disponibile per \"" + prodotto.getNome() + "\": " +
                                "richiesti " + quantitaRichiesta + ", disponibili " + prodotto.getQuantità()
                );
            }

            double prezzoUnitario = prodotto.getPrezzo();
            DettaglioOrdine dettaglio = new DettaglioOrdine(prodotto, quantitaRichiesta, prezzoUnitario);
            dettagli.add(dettaglio);
            totale += prezzoUnitario * quantitaRichiesta;

            prodotto.setQuantità(prodotto.getQuantità() - quantitaRichiesta);
            prodottoService.salvaProdotto(prodotto);
        }

        Ordine ordine;

        if (isLoggato) {
            // authentication.getName() è l'email, impostata come "subject" del
            // token dentro JwtAuthFilter — vedi JwtService.generaToken.
            String email = authentication.getName();
            User utente = userService.trovaPerEmail(email);

            ordine = new Ordine(utente, dto.indirizzoSpedizione(), dto.note(), totale, dettagli);
        } else {

            if (isBlank(dto.nomeCliente()) || isBlank(dto.cognomeCliente())
                    || isBlank(dto.emailCliente()) || isBlank(dto.telefonoCliente())) {
                throw new IllegalArgumentException(
                        "Per un ordine senza account sono obbligatori nome, cognome, email e telefono."
                );
            }

            ordine = new Ordine(
                    dto.nomeCliente(), dto.cognomeCliente(), dto.emailCliente(), dto.telefonoCliente(),
                    dto.indirizzoSpedizione(), dto.note(), totale, dettagli
            );
        }

        Ordine salvato = ordineService.salva(ordine);
        return ResponseEntity.status(HttpStatus.CREATED).body(salvato);
    }

    // PATCH /api/ordini/{id}/stato — aggiorna solo lo stato di un ordine

    @PatchMapping("/{id}/stato")
    public ResponseEntity<Ordine> updateStato(
            @PathVariable UUID id,
            @Valid @RequestBody OrdineStatoRequestDTO dto) {

        Ordine ordine = ordineService.trovaPerId(id);
        ordine.setStato(dto.stato());
        Ordine aggiornato = ordineService.salva(ordine);
        return ResponseEntity.ok(aggiornato);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrdine(@PathVariable UUID id) {
        ordineService.elimina(id);
        return ResponseEntity.noContent().build();
    }

    private boolean isBlank(String s) {
        return s == null || s.isBlank();
    }
}