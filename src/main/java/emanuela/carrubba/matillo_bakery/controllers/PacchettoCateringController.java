package emanuela.carrubba.matillo_bakery.controllers;

import emanuela.carrubba.matillo_bakery.RequestDTO.PacchettoCateringRequestDTO;
import emanuela.carrubba.matillo_bakery.entities.PacchettoCatering;
import emanuela.carrubba.matillo_bakery.services.FileStorageService;
import emanuela.carrubba.matillo_bakery.services.PacchettoCateringService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/catering")
public class PacchettoCateringController {

    private final PacchettoCateringService pacchettoCateringService;
    private final FileStorageService fileStorageService;

    public PacchettoCateringController(PacchettoCateringService pacchettoCateringService,
                                       FileStorageService fileStorageService) {
        this.pacchettoCateringService = pacchettoCateringService;
        this.fileStorageService = fileStorageService;
    }

    @GetMapping
    public ResponseEntity<List<PacchettoCatering>> getAllPacchetti() {
        return ResponseEntity.ok(pacchettoCateringService.trovaTutti());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PacchettoCatering> getPacchettoById(@PathVariable UUID id) {
        return ResponseEntity.ok(pacchettoCateringService.trovaPerId(id));
    }

    @PostMapping
    public ResponseEntity<PacchettoCatering> createPacchetto(@Valid @RequestBody PacchettoCateringRequestDTO dto) {
        PacchettoCatering pacchetto = new PacchettoCatering(
                dto.nome(), dto.descrizione(), dto.prezzoPersona(), dto.numeroMinimoPersone()
        );
        pacchetto.setIncluso(dto.incluso());
        PacchettoCatering salvato = pacchettoCateringService.salva(pacchetto);
        return ResponseEntity.status(HttpStatus.CREATED).body(salvato);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PacchettoCatering> updatePacchetto(
            @PathVariable UUID id, @Valid @RequestBody PacchettoCateringRequestDTO dto) {

        PacchettoCatering pacchetto = pacchettoCateringService.trovaPerId(id);
        pacchetto.setNome(dto.nome());
        pacchetto.setDescrizione(dto.descrizione());
        pacchetto.setPrezzoPersona(dto.prezzoPersona());
        pacchetto.setNumeroMinimoPersone(dto.numeroMinimoPersone());
        pacchetto.setIncluso(dto.incluso());

        PacchettoCatering salvato = pacchettoCateringService.salva(pacchetto);
        return ResponseEntity.ok(salvato);
    }

    @PostMapping("/{id}/immagine")
    public ResponseEntity<PacchettoCatering> uploadImmagine(
            @PathVariable UUID id, @RequestParam("file") MultipartFile file) {

        PacchettoCatering pacchetto = pacchettoCateringService.trovaPerId(id);
        String imageUrl = fileStorageService.storeFile(file);
        pacchetto.setImmagine(imageUrl);
        PacchettoCatering aggiornato = pacchettoCateringService.salva(pacchetto);
        return ResponseEntity.ok(aggiornato);
    }

    @PostMapping("/{id}/galleria")
    public ResponseEntity<PacchettoCatering> uploadGalleria(
            @PathVariable UUID id, @RequestParam("files") List<MultipartFile> files) {

        PacchettoCatering pacchetto = pacchettoCateringService.trovaPerId(id);
        List<String> urls = new ArrayList<>();
        for (MultipartFile file : files) {
            urls.add(fileStorageService.storeFile(file));
        }
        pacchetto.setGalleria(String.join(",", urls));
        PacchettoCatering aggiornato = pacchettoCateringService.salva(pacchetto);
        return ResponseEntity.ok(aggiornato);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePacchetto(@PathVariable UUID id) {
        pacchettoCateringService.elimina(id);
        return ResponseEntity.noContent().build();
    }
}