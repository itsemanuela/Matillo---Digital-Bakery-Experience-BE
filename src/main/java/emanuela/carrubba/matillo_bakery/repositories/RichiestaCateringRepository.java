package emanuela.carrubba.matillo_bakery.repositories;

import emanuela.carrubba.matillo_bakery.entities.RichiestaCatering;
import emanuela.carrubba.matillo_bakery.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface RichiestaCateringRepository extends JpaRepository<RichiestaCatering, UUID> {
    List<RichiestaCatering> findByUtente(User utente);
}