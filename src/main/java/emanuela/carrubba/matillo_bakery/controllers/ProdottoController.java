package emanuela.carrubba.matillo_bakery.controllers;

import emanuela.carrubba.matillo_bakery.entities.Categoria;
import emanuela.carrubba.matillo_bakery.entities.Prodotto;
import emanuela.carrubba.matillo_bakery.services.FileStorageService;
import emanuela.carrubba.matillo_bakery.services.ProdottoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/prodotti")
// TODO: quando l'autenticazione sarà pronta, proteggere POST/PUT/DELETE
public class ProdottoController {

    private final ProdottoService prodottoService;
    private final FileStorageService fileStorageService;

    public ProdottoController(ProdottoService prodottoService, FileStorageService fileStorageService) {
        this.prodottoService = prodottoService;
        this.fileStorageService = fileStorageService;
    }

    // GET /api/prodotti — lista completa, usata dallo Shop pubblico
    @GetMapping
    public ResponseEntity<List<Prodotto>> getAllProdotti() {
        return ResponseEntity.ok(prodottoService.findAll());
    }

    // GET /api/prodotti/disponibili — solo i prodotti disponibili
    @GetMapping("/disponibili")
    public ResponseEntity<List<Prodotto>> getProdottiDisponibili() {
        return ResponseEntity.ok(prodottoService.trovaDisponibili());
    }

    // GET /api/prodotti/categoria/{categoria} — filtro per categoria
    @GetMapping("/categoria/{categoria}")
    public ResponseEntity<List<Prodotto>> getProdottiByCategoria(@PathVariable Categoria categoria) {
        return ResponseEntity.ok(prodottoService.findByCategoria(categoria));
    }

    // GET /api/prodotti/{id} — dettaglio singolo prodotto
    @GetMapping("/{id}")
    public ResponseEntity<Prodotto> getProdottoById(@PathVariable UUID id) {
        Prodotto prodotto = prodottoService.trovaPerId(id);
        return ResponseEntity.ok(prodotto);
    }

    // POST /api/prodotti — crea un nuovo prodotto (senza immagine, solo dati testuali)
    @PostMapping
    public ResponseEntity<Prodotto> createProdotto(@RequestBody Prodotto prodotto) {
        Prodotto salvato = prodottoService.salvaProdotto(prodotto);
        return ResponseEntity.status(HttpStatus.CREATED).body(salvato);
    }

    // POST /api/prodotti/{id}/immagine — carica/sostituisce l'immagine di un prodotto esistente
    @PostMapping("/{id}/immagine")
    public ResponseEntity<Prodotto> uploadImmagine(
            @PathVariable UUID id,
            @RequestParam("file") MultipartFile file) {

        Prodotto prodotto = prodottoService.trovaPerId(id);
        String imageUrl = fileStorageService.storeFile(file);
        prodotto.setImmagine(imageUrl);
        Prodotto aggiornato = prodottoService.salvaProdotto(prodotto);
        return ResponseEntity.ok(aggiornato);
    }

    // PUT /api/prodotti/{id} — aggiorna i dati di un prodotto esistente
    @PutMapping("/{id}")
    public ResponseEntity<Prodotto> updateProdotto(
            @PathVariable UUID id,
            @RequestBody Prodotto prodottoAggiornato) {

        Prodotto salvato = prodottoService.aggiornaProdotto(id, prodottoAggiornato);
        return ResponseEntity.ok(salvato);
    }

    // DELETE /api/prodotti/{id} — rimuove un prodotto
    // lo recupero prima con trovaPerId.
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProdotto(@PathVariable UUID id) {
        Prodotto prodotto = prodottoService.trovaPerId(id);
        prodottoService.eliminaProdotto(prodotto);
        return ResponseEntity.noContent().build();
    }
}