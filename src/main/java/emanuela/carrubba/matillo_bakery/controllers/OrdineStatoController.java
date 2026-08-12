package emanuela.carrubba.matillo_bakery.controllers;

import emanuela.carrubba.matillo_bakery.ResponseDTO.OrdineStatoResponseDTO;
import emanuela.carrubba.matillo_bakery.entities.Ordine;
import emanuela.carrubba.matillo_bakery.exceptions.NotFoundException;
import emanuela.carrubba.matillo_bakery.services.OrdineService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/ordini")
public class OrdineStatoController {

    private final OrdineService ordineService;

    public OrdineStatoController(OrdineService ordineService) {
        this.ordineService = ordineService;
    }

    @GetMapping("/stato")
    public ResponseEntity<?> statoOspite(@RequestParam UUID numeroOrdine, @RequestParam String email) {
        try {
            Ordine ordine = ordineService.trovaPerUuidEEmailOspite(numeroOrdine, email);
            return ResponseEntity.ok(new OrdineStatoResponseDTO(ordine.getUuid(), ordine.getStato(), ordine.getDataOrdine()));
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Ordine non trovato");
        }
    }

    @GetMapping("/{uuid}/stato")
    public ResponseEntity<?> statoUtente(@PathVariable UUID uuid, Authentication authentication) {
        String emailUtente = authentication.getName();
        try {
            Ordine ordine = ordineService.trovaPerUuidEUtente(uuid, emailUtente);
            return ResponseEntity.ok(new OrdineStatoResponseDTO(ordine.getUuid(), ordine.getStato(), ordine.getDataOrdine()));
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Ordine non trovato");
        }
    }

    @GetMapping("/miei")
    public ResponseEntity<List<OrdineStatoResponseDTO>> miei(Authentication authentication) {
        String emailUtente = authentication.getName();
        List<Ordine> ordini = ordineService.trovaPerEmailUtente(emailUtente);

        List<OrdineStatoResponseDTO> risultato = ordini.stream()
                .map(o -> new OrdineStatoResponseDTO(o.getUuid(), o.getStato(), o.getDataOrdine()))
                .collect(Collectors.toList());

        return ResponseEntity.ok(risultato);
    }
}