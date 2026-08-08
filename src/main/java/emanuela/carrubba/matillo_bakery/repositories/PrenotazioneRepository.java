package emanuela.carrubba.matillo_bakery.repository;

import emanuela.carrubba.matillo_bakery.entities.Laboratorio;
import emanuela.carrubba.matillo_bakery.entities.Prenotazione;
import emanuela.carrubba.matillo_bakery.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface PrenotazioneRepository extends JpaRepository<Prenotazione, UUID> {
    List<Prenotazione> findByUtente(User utente);
    List<Prenotazione> findByLaboratorio(Laboratorio laboratorio);
    List<Prenotazione> findByLaboratorioAndUtente(Laboratorio laboratorio, User utente);
    List<Prenotazione> findByLaboratorioAndEmailCliente(Laboratorio laboratorio, String emailCliente);
    List<Prenotazione> findByEmailCliente(String emailCliente);
}