package emanuela.carrubba.matillo_bakery.services;

import emanuela.carrubba.matillo_bakery.entities.FotoGalleria;
import emanuela.carrubba.matillo_bakery.exceptions.NotFoundException;
import emanuela.carrubba.matillo_bakery.repositories.FotoGalleriaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class FotoGalleriaService {

    private final FotoGalleriaRepository fotoGalleriaRepository;

    public FotoGalleriaService(FotoGalleriaRepository fotoGalleriaRepository) {
        this.fotoGalleriaRepository = fotoGalleriaRepository;
    }

    public List<FotoGalleria> trovaTutti() {
        return fotoGalleriaRepository.findAll();
    }

    public FotoGalleria trovaPerId(UUID uuid) {
        return fotoGalleriaRepository.findById(uuid)
                .orElseThrow(() -> new NotFoundException("Foto galleria con id " + uuid + " non trovata"));
    }

    public FotoGalleria salva(FotoGalleria foto) {
        return fotoGalleriaRepository.save(foto);
    }

    public void elimina(UUID uuid) {
        fotoGalleriaRepository.deleteById(uuid);
    }
}