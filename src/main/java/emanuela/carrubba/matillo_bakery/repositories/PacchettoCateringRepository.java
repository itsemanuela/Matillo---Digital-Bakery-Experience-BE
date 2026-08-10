package emanuela.carrubba.matillo_bakery.repositories;

import emanuela.carrubba.matillo_bakery.entities.PacchettoCatering;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PacchettoCateringRepository extends JpaRepository<PacchettoCatering, UUID> {
}