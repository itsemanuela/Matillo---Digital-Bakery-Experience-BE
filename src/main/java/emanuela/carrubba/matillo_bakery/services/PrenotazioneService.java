package emanuela.carrubba.matillo_bakery.services;

import emanuela.carrubba.matillo_bakery.entities.StatoPrenotazione;
import emanuela.carrubba.matillo_bakery.entities.Laboratorio;
import emanuela.carrubba.matillo_bakery.entities.Prenotazione;
import emanuela.carrubba.matillo_bakery.entities.User;
import emanuela.carrubba.matillo_bakery.exceptions.NotFoundException;
import emanuela.carrubba.matillo_bakery.exceptions.QuantitaNonDisponibileException;
import emanuela.carrubba.matillo_bakery.repositories.PrenotazioneRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class PrenotazioneService {

    private final PrenotazioneRepository prenotazioneRepository;
    private final LaboratorioService laboratorioService;

    public PrenotazioneService(PrenotazioneRepository prenotazioneRepository, LaboratorioService laboratorioService) {
        this.prenotazioneRepository = prenotazioneRepository;
        this.laboratorioService = laboratorioService;
    }

    public List<Prenotazione> trovaTutte() {
        return prenotazioneRepository.findAll();
    }
    public List<Prenotazione> trovaPerLaboratorio(UUID laboratorioUuid) {
        return prenotazioneRepository.findByLaboratorioUuid(laboratorioUuid);
    }

    public List<Prenotazione> trovaPerLaboratorioEStato(UUID laboratorioUuid, StatoPrenotazione stato) {
        return prenotazioneRepository.findByLaboratorioUuidAndStato(laboratorioUuid, stato);
    }

    public List<Prenotazione> trovaPerStato(StatoPrenotazione stato) {
        return prenotazioneRepository.findByStato(stato);
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

    public Prenotazione modificaPrenotazione(UUID id, User utenteLoggato, Prenotazione datiAggiornati) {
        Prenotazione prenotazioneEsistente = prenotazioneRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Prenotazione con id " + id + " non trovata"));

        if (!prenotazioneEsistente.getUtente().getUuid().equals(utenteLoggato.getUuid())) {
            throw new RuntimeException("Non sei autorizzato a modificare questa prenotazione");
        }

        Laboratorio laboratorio = prenotazioneEsistente.getLaboratorio();
        int vecchiPosti = prenotazioneEsistente.getNumeroPersone();
        int nuoviPosti = datiAggiornati.getNumeroPersone();


        int postiTotaliDisponibiliLab = laboratorio.getPostiDisponibili() + vecchiPosti;

        if (nuoviPosti > postiTotaliDisponibiliLab) {
            throw new QuantitaNonDisponibileException(
                    "Non puoi richiedere " + nuoviPosti + " posti. La capienza massima disponibile per questo laboratorio è " + postiTotaliDisponibiliLab + "."
            );
        }


        int differenzaPosti = nuoviPosti - vecchiPosti;
        laboratorio.setPostiDisponibili(laboratorio.getPostiDisponibili() - differenzaPosti);

        laboratorioService.salva(laboratorio);
        prenotazioneEsistente.setNumeroPersone(nuoviPosti);

        return prenotazioneRepository.save(prenotazioneEsistente);
    }
}