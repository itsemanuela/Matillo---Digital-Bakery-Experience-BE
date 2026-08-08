package emanuela.carrubba.matillo_bakery.services;

import emanuela.carrubba.matillo_bakery.entities.StatoPrenotazione;
import emanuela.carrubba.matillo_bakery.entities.Laboratorio;
import emanuela.carrubba.matillo_bakery.entities.Prenotazione;
import emanuela.carrubba.matillo_bakery.entities.User;
import emanuela.carrubba.matillo_bakery.exceptions.NotFoundException;
import emanuela.carrubba.matillo_bakery.repository.PrenotazioneRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class PrenotazioneService {

    private final PrenotazioneRepository prenotazioneRepository;

    public PrenotazioneService(PrenotazioneRepository prenotazioneRepository) {
        this.prenotazioneRepository = prenotazioneRepository;
    }

    public List<Prenotazione> trovaTutte() {
        return prenotazioneRepository.findAll();
    }

    public Prenotazione trovaPerId(UUID uuid) {
        return prenotazioneRepository.findById(uuid)
                .orElseThrow(() -> new NotFoundException("Prenotazione con id " + uuid + " non trovata"));
    }

    public List<Prenotazione> trovaPerUtente(User utente) {
        return prenotazioneRepository.findByUtente(utente);
    }

    public Prenotazione salva(Prenotazione prenotazione) {
        return prenotazioneRepository.save(prenotazione);
    }

    public boolean esistePrenotazioneAttiva(Laboratorio laboratorio, User utente) {
        return prenotazioneRepository.findByLaboratorioAndUtente(laboratorio, utente).stream()
                .anyMatch(p -> p.getStato() != StatoPrenotazione.CANCELLATA);
    }

    public boolean esistePrenotazioneAttiva(Laboratorio laboratorio, String emailCliente) {
        return prenotazioneRepository.findByLaboratorioAndEmailCliente(laboratorio, emailCliente).stream()
                .anyMatch(p -> p.getStato() != StatoPrenotazione.CANCELLATA);
    }
    public List<Prenotazione> trovaPerEmailCliente(String emailCliente) {
        return prenotazioneRepository.findByEmailCliente(emailCliente);
    }
}