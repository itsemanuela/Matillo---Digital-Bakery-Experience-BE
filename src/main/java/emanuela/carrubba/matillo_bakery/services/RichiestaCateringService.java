package emanuela.carrubba.matillo_bakery.services;

import emanuela.carrubba.matillo_bakery.entities.RichiestaCatering;
import emanuela.carrubba.matillo_bakery.entities.User;
import emanuela.carrubba.matillo_bakery.exceptions.NotFoundException;
import emanuela.carrubba.matillo_bakery.repositories.RichiestaCateringRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class RichiestaCateringService {

    private final RichiestaCateringRepository richiestaCateringRepository;

    public RichiestaCateringService(RichiestaCateringRepository richiestaCateringRepository) {
        this.richiestaCateringRepository = richiestaCateringRepository;
    }

    public List<RichiestaCatering> trovaTutte() {
        return richiestaCateringRepository.findAll();
    }

    public RichiestaCatering trovaPerId(UUID uuid) {
        return richiestaCateringRepository.findById(uuid)
                .orElseThrow(() -> new NotFoundException("Richiesta catering con id " + uuid + " non trovata"));
    }

    public List<RichiestaCatering> trovaPerUtente(User utente) {
        return richiestaCateringRepository.findByUtente(utente);
    }

    public RichiestaCatering salva(RichiestaCatering richiesta) {
        return richiestaCateringRepository.save(richiesta);
    }
}