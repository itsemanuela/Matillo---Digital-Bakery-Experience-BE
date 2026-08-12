package emanuela.carrubba.matillo_bakery.repositories;

import emanuela.carrubba.matillo_bakery.entities.Ordine;
import emanuela.carrubba.matillo_bakery.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface OrdineRepository extends JpaRepository<Ordine, UUID> {
    List<Ordine> findByUtente(User user);

    Optional<Ordine> findByUuidAndEmailCliente(UUID uuid, String emailCliente);

    Optional<Ordine> findByUuidAndUtente_Email(UUID uuid, String email);

    List<Ordine> findByUtente_EmailOrderByDataOrdineDesc(String email);
}