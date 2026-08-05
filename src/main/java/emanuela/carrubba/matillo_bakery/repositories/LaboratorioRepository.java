package emanuela.carrubba.matillo_bakery.repositories;

import emanuela.carrubba.matillo_bakery.entities.Laboratorio;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface LaboratorioRepository extends JpaRepository<Laboratorio, UUID> {
}