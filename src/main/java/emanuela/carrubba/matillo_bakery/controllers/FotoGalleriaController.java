package emanuela.carrubba.matillo_bakery.controllers;

import emanuela.carrubba.matillo_bakery.RequestDTO.FotoGalleriaRequestDTO;
import emanuela.carrubba.matillo_bakery.entities.FotoGalleria;
import emanuela.carrubba.matillo_bakery.services.FileStorageService;
import emanuela.carrubba.matillo_bakery.services.FotoGalleriaService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/galleria-eventi")
public class FotoGalleriaController {

    private final FotoGalleriaService fotoGalleriaService;
    private final FileStorageService fileStorageService;

    public FotoGalleriaController(FotoGalleriaService fotoGalleriaService,
                                  FileStorageService fileStorageService) {
        this.fotoGalleriaService = fotoGalleriaService;
        this.fileStorageService = fileStorageService;
    }

    @GetMapping
    public ResponseEntity<List<FotoGalleria>> getAllEventi() {
        return ResponseEntity.ok(fotoGalleriaService.trovaTutti());
    }

    @GetMapping("/{id}")
    public ResponseEntity<FotoGalleria> getEventoById(@PathVariable UUID id) {
        return ResponseEntity.ok(fotoGalleriaService.trovaPerId(id));
    }

    @PostMapping
    public ResponseEntity<FotoGalleria> createEvento(@Valid @RequestBody FotoGalleriaRequestDTO dto) {
        FotoGalleria evento = new FotoGalleria(dto.titolo());
        FotoGalleria salvato = fotoGalleriaService.salva(evento);
        return ResponseEntity.status(HttpStatus.CREATED).body(salvato);
    }

    @PutMapping("/{id}")
    public ResponseEntity<FotoGalleria> updateEvento(
            @PathVariable UUID id, @Valid @RequestBody FotoGalleriaRequestDTO dto) {

        FotoGalleria evento = fotoGalleriaService.trovaPerId(id);
        evento.setTitolo(dto.titolo());

        FotoGalleria salvato = fotoGalleriaService.salva(evento);
        return ResponseEntity.ok(salvato);
    }

    @PostMapping("/{id}/galleria")
    public ResponseEntity<FotoGalleria> uploadGalleria(
            @PathVariable UUID id, @RequestParam("files") List<MultipartFile> files) {

        FotoGalleria evento = fotoGalleriaService.trovaPerId(id);

        List<String> urlEsistenti = evento.getGalleria() == null || evento.getGalleria().isBlank()
                ? new ArrayList<>()
                : new ArrayList<>(Arrays.asList(evento.getGalleria().split(",")));

        List<CompletableFuture<String>> caricamenti = files.stream()
                .map(file -> CompletableFuture.supplyAsync(() -> fileStorageService.storeFile(file)))
                .collect(Collectors.toList());

        List<String> nuoveUrl = caricamenti.stream()
                .map(CompletableFuture::join)
                .collect(Collectors.toList());

        urlEsistenti.addAll(nuoveUrl);

        evento.setGalleria(String.join(",", urlEsistenti));
        FotoGalleria aggiornato = fotoGalleriaService.salva(evento);
        return ResponseEntity.ok(aggiornato);
    }

    @DeleteMapping("/{id}/galleria")
    public ResponseEntity<FotoGalleria> rimuoviFoto(
            @PathVariable UUID id, @RequestParam("url") String url) {

        FotoGalleria evento = fotoGalleriaService.trovaPerId(id);

        if (evento.getGalleria() != null) {
            List<String> urlRimanenti = new ArrayList<>(Arrays.asList(evento.getGalleria().split(",")));
            urlRimanenti.remove(url);
            evento.setGalleria(String.join(",", urlRimanenti));
        }

        FotoGalleria aggiornato = fotoGalleriaService.salva(evento);
        return ResponseEntity.ok(aggiornato);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEvento(@PathVariable UUID id) {
        fotoGalleriaService.elimina(id);
        return ResponseEntity.noContent().build();
    }
}