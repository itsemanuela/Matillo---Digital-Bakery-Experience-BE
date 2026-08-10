package emanuela.carrubba.matillo_bakery.repositories;

import emanuela.carrubba.matillo_bakery.entities.FotoGalleria;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface FotoGalleriaRepository extends JpaRepository<FotoGalleria, UUID> {}