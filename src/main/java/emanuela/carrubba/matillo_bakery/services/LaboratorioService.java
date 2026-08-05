package emanuela.carrubba.matillo_bakery.services;

import emanuela.carrubba.matillo_bakery.entities.Laboratorio;
import emanuela.carrubba.matillo_bakery.exceptions.NotFoundException;
import emanuela.carrubba.matillo_bakery.repositories.LaboratorioRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class LaboratorioService {

    private final LaboratorioRepository laboratorioRepository;

    public LaboratorioService(LaboratorioRepository laboratorioRepository) {
        this.laboratorioRepository = laboratorioRepository;
    }

    public List<Laboratorio> trovaTutti() {
        return laboratorioRepository.findAll();
    }

    public Laboratorio trovaPerId(UUID uuid) {
        return laboratorioRepository.findById(uuid)
                .orElseThrow(() -> new NotFoundException("Laboratorio con id " + uuid + " non trovato"));
    }

    public Laboratorio salva(Laboratorio laboratorio) {
        return laboratorioRepository.save(laboratorio);
    }

    public void elimina(UUID uuid) {
        laboratorioRepository.deleteById(uuid);
    }
}