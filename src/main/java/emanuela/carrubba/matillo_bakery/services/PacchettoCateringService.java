package emanuela.carrubba.matillo_bakery.services;

import emanuela.carrubba.matillo_bakery.entities.PacchettoCatering;
import emanuela.carrubba.matillo_bakery.exceptions.NotFoundException;
import emanuela.carrubba.matillo_bakery.repositories.PacchettoCateringRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class PacchettoCateringService {

    private final PacchettoCateringRepository pacchettoCateringRepository;

    public PacchettoCateringService(PacchettoCateringRepository pacchettoCateringRepository) {
        this.pacchettoCateringRepository = pacchettoCateringRepository;
    }

    public List<PacchettoCatering> trovaTutti() {
        return pacchettoCateringRepository.findAll();
    }

    public PacchettoCatering trovaPerId(UUID uuid) {
        return pacchettoCateringRepository.findById(uuid)
                .orElseThrow(() -> new NotFoundException("Pacchetto catering con id " + uuid + " non trovato"));
    }

    public PacchettoCatering salva(PacchettoCatering pacchetto) {
        return pacchettoCateringRepository.save(pacchetto);
    }

    public void elimina(UUID uuid) {
        pacchettoCateringRepository.deleteById(uuid);
    }
}