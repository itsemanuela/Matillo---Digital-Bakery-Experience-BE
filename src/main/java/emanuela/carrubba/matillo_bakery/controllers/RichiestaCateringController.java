package emanuela.carrubba.matillo_bakery.controllers;

import emanuela.carrubba.matillo_bakery.RequestDTO.RichiestaCateringRequestDTO;
import emanuela.carrubba.matillo_bakery.entities.PacchettoCatering;
import emanuela.carrubba.matillo_bakery.entities.RichiestaCatering;
import emanuela.carrubba.matillo_bakery.entities.StatoRichiesta;
import emanuela.carrubba.matillo_bakery.entities.User;
import emanuela.carrubba.matillo_bakery.services.PacchettoCateringService;
import emanuela.carrubba.matillo_bakery.services.RichiestaCateringService;
import emanuela.carrubba.matillo_bakery.services.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/richieste-catering")
public class RichiestaCateringController {

    private final RichiestaCateringService richiestaCateringService;
    private final PacchettoCateringService pacchettoCateringService;
    private final UserService userService;

    public RichiestaCateringController(RichiestaCateringService richiestaCateringService,
                                       PacchettoCateringService pacchettoCateringService,
                                       UserService userService) {
        this.richiestaCateringService = richiestaCateringService;
        this.pacchettoCateringService = pacchettoCateringService;
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<RichiestaCatering>> getAllRichieste() {
        return ResponseEntity.ok(richiestaCateringService.trovaTutte());
    }

    @PostMapping
    public ResponseEntity<RichiestaCatering> createRichiesta(@Valid @RequestBody RichiestaCateringRequestDTO dto) {
        PacchettoCatering pacchetto = pacchettoCateringService.trovaPerId(dto.pacchettoId());

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User utente = null;
        if (authentication != null && authentication.isAuthenticated()
                && !"anonymousUser".equals(authentication.getPrincipal())) {
            utente = userService.trovaPerEmail(authentication.getName());
        }

        RichiestaCatering richiesta = new RichiestaCatering(
                pacchetto, utente, dto.nomeCliente(), dto.cognomeCliente(),
                dto.emailCliente(), dto.telefonoCliente(), dto.dataEvento(),
                dto.numeroPersone(), dto.note()
        );

        RichiestaCatering salvata = richiestaCateringService.salva(richiesta);
        return ResponseEntity.status(HttpStatus.CREATED).body(salvata);
    }

    @PatchMapping("/{id}/stato")
    public ResponseEntity<RichiestaCatering> aggiornaStato(
            @PathVariable UUID id, @RequestBody Map<String, String> body) {

        RichiestaCatering richiesta = richiestaCateringService.trovaPerId(id);
        richiesta.setStato(StatoRichiesta.valueOf(body.get("stato")));
        RichiestaCatering aggiornata = richiestaCateringService.salva(richiesta);
        return ResponseEntity.ok(aggiornata);
    }
}