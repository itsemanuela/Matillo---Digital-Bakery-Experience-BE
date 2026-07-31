package emanuela.carrubba.matillo_bakery.repositories;

import emanuela.carrubba.matillo_bakery.entities.Categoria;
import emanuela.carrubba.matillo_bakery.entities.Prodotto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ProdottoRepository extends JpaRepository<Prodotto, UUID> {
    List<Prodotto> findByNome(String nome);
    List<Prodotto> findByCategoria(Categoria categoria);
    List<Prodotto> findByDisponibileTrue();
}
