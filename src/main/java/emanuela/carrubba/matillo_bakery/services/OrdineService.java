package emanuela.carrubba.matillo_bakery.services;

import emanuela.carrubba.matillo_bakery.StatoOrdine;
import emanuela.carrubba.matillo_bakery.entities.Ordine;
import emanuela.carrubba.matillo_bakery.entities.User;
import emanuela.carrubba.matillo_bakery.StatoOrdine;
import emanuela.carrubba.matillo_bakery.exceptions.NotFoundException;
import emanuela.carrubba.matillo_bakery.repositories.OrdineRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class OrdineService {

    private final OrdineRepository ordineRepository;

    public OrdineService(OrdineRepository ordineRepository) {
        this.ordineRepository = ordineRepository;
    }

    public List<Ordine> trovaTutti() {
        return ordineRepository.findAll();
    }

    public Ordine trovaPerId(UUID uuid) {
        return ordineRepository.findById(uuid)
                .orElseThrow(() -> new NotFoundException("Ordine non trovato con id: " + uuid));
    }

    public List<Ordine> trovaPerUtente(User utente) {
        return ordineRepository.findByUtente(utente);
    }

    public Ordine trovaPerUuidEEmailOspite(UUID uuid, String emailCliente) {
        return ordineRepository.findByUuidAndEmailCliente(uuid, emailCliente)
                .orElseThrow(() -> new NotFoundException("Ordine non trovato"));
    }

    public Ordine trovaPerUuidEUtente(UUID uuid, String emailUtente) {
        return ordineRepository.findByUuidAndUtente_Email(uuid, emailUtente)
                .orElseThrow(() -> new NotFoundException("Ordine non trovato"));
    }

    public List<Ordine> trovaPerEmailUtente(String emailUtente) {
        return ordineRepository.findByUtente_EmailOrderByDataOrdineDesc(emailUtente);
    }

    public Ordine salva(Ordine ordine) {
        return ordineRepository.save(ordine);
    }

    // Soft delete
    public void elimina(UUID uuid) {
        Ordine ordine = trovaPerId(uuid);
        ordine.setStato(StatoOrdine.CANCELLATO);
        ordineRepository.save(ordine);
    }
}