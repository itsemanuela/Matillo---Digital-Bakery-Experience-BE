package emanuela.carrubba.matillo_bakery.repositories;

import emanuela.carrubba.matillo_bakery.entities.Ordine;
import emanuela.carrubba.matillo_bakery.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface OrdineRepository extends JpaRepository<Ordine, UUID>{
    List<Ordine> findByUtente(User user);
}
