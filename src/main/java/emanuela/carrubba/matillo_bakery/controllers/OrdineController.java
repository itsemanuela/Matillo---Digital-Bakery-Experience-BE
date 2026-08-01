package emanuela.carrubba.matillo_bakery.controllers;

import emanuela.carrubba.matillo_bakery.RequestDTO.DettaglioOrdineRequestDTO;
import emanuela.carrubba.matillo_bakery.RequestDTO.OrdineRequestDTO;
import emanuela.carrubba.matillo_bakery.entities.DettaglioOrdine;
import emanuela.carrubba.matillo_bakery.entities.Ordine;
import emanuela.carrubba.matillo_bakery.entities.Prodotto;
import emanuela.carrubba.matillo_bakery.entities.User;
import emanuela.carrubba.matillo_bakery.exceptions.QuantitaNonDisponibileException;
import emanuela.carrubba.matillo_bakery.services.OrdineService;
import emanuela.carrubba.matillo_bakery.services.ProdottoService;
import emanuela.carrubba.matillo_bakery.services.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@RestController
@RequestMapping("/api/ordini")
// TODO: proteggere questi endpoint quando l'autenticazione sarà pronta.

public class OrdineController {

    private final OrdineService ordineService;
    private final UserService userService;
    private final ProdottoService prodottoService;

    public OrdineController(OrdineService ordineService, UserService userService, ProdottoService prodottoService) {
        this.ordineService = ordineService;
        this.userService = userService;
        this.prodottoService = prodottoService;
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
    //
    // TODO TEMPORANEO: senza autenticazione

    @PostMapping
    public ResponseEntity<Ordine> createOrdine(
            @Valid @RequestBody OrdineRequestDTO dto,
            @RequestParam UUID utenteId) {

        User utente = userService.trovaPerId(utenteId);

        List<DettaglioOrdine> dettagli = new ArrayList<>();
        double totale = 0.0;

        for (DettaglioOrdineRequestDTO dettaglioDto : dto.dettagli()) {
            Prodotto prodotto = prodottoService.trovaPerId(dettaglioDto.idProdotto());
            int quantitaRichiesta = dettaglioDto.quantita();

            // Verifica che ci sia abbastanza stock prima di accettare l'ordine
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

        Ordine ordine = new Ordine(utente, dto.indirizzoSpedizione(), dto.note(), totale, dettagli);

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