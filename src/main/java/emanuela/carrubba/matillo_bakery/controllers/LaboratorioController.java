package emanuela.carrubba.matillo_bakery.controllers;

import emanuela.carrubba.matillo_bakery.RequestDTO.LaboratorioRequestDTO;
import emanuela.carrubba.matillo_bakery.entities.Laboratorio;
import emanuela.carrubba.matillo_bakery.services.FileStorageService;
import emanuela.carrubba.matillo_bakery.services.LaboratorioService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/laboratori")
public class LaboratorioController {

    private final LaboratorioService laboratorioService;
    private final FileStorageService fileStorageService;

    public LaboratorioController(LaboratorioService laboratorioService, FileStorageService fileStorageService) {
        this.laboratorioService = laboratorioService;
        this.fileStorageService = fileStorageService;
    }

    @GetMapping
    public ResponseEntity<List<Laboratorio>> getAllLaboratori() {
        return ResponseEntity.ok(laboratorioService.trovaTutti());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Laboratorio> getLaboratorioById(@PathVariable UUID id) {
        return ResponseEntity.ok(laboratorioService.trovaPerId(id));
    }

    @PostMapping
    public ResponseEntity<Laboratorio> createLaboratorio(@Valid @RequestBody LaboratorioRequestDTO dto) {
        Laboratorio laboratorio = new Laboratorio(
                dto.nome(), dto.descrizione(), dto.dataOra(), dto.postiTotali(), dto.prezzo()
        );
        laboratorio.setProcedimento(dto.procedimento());
        Laboratorio salvato = laboratorioService.salva(laboratorio);
        return ResponseEntity.status(HttpStatus.CREATED).body(salvato);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Laboratorio> updateLaboratorio(
            @PathVariable UUID id, @Valid @RequestBody LaboratorioRequestDTO dto) {

        Laboratorio laboratorio = laboratorioService.trovaPerId(id);
        int postiPrenotati = laboratorio.getPostiTotali() - laboratorio.getPostiDisponibili();

        laboratorio.setNome(dto.nome());
        laboratorio.setDescrizione(dto.descrizione());
        laboratorio.setProcedimento(dto.procedimento());
        laboratorio.setDataOra(dto.dataOra());
        laboratorio.setPostiTotali(dto.postiTotali());
        laboratorio.setPostiDisponibili(dto.postiTotali() - postiPrenotati);
        laboratorio.setPrezzo(dto.prezzo());

        Laboratorio salvato = laboratorioService.salva(laboratorio);
        return ResponseEntity.ok(salvato);
    }

    @PostMapping("/{id}/immagine")
    public ResponseEntity<Laboratorio> uploadImmagine(
            @PathVariable UUID id, @RequestParam("file") MultipartFile file) {

        Laboratorio laboratorio = laboratorioService.trovaPerId(id);
        String imageUrl = fileStorageService.storeFile(file);
        laboratorio.setImmagine(imageUrl);
        Laboratorio aggiornato = laboratorioService.salva(laboratorio);
        return ResponseEntity.ok(aggiornato);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLaboratorio(@PathVariable UUID id) {
        laboratorioService.elimina(id);
        return ResponseEntity.noContent().build();
    }
}