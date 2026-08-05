package emanuela.carrubba.matillo_bakery.entities;


import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.UUID;
@Entity
@Table(name = "prenotazione")
public class Prenotazione {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID uuid;

    @ManyToOne
    @JoinColumn(name = "laboratorio_uuid", nullable = false)
    private Laboratorio laboratorio;

    @ManyToOne
    @JoinColumn(name = "utente_uuid", nullable = true)
    private User utente;

    private String nomeCliente;
    private String cognomeCliente;
    private String emailCliente;
    private String telefonoCliente;

    @Column(nullable = false)
    private int numeroPersone;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatoPrenotazione stato = StatoPrenotazione.CONFERMATA;

    @Column(name = "data_prenotazione", nullable = false, updatable = false)
    private LocalDateTime dataPrenotazione = LocalDateTime.now();

    public Prenotazione() {}

    public Prenotazione(Laboratorio laboratorio, User utente, int numeroPersone) {
        this.laboratorio = laboratorio;
        this.utente = utente;
        this.numeroPersone = numeroPersone;
    }

    public Prenotazione(Laboratorio laboratorio, String nomeCliente, String cognomeCliente,
                        String emailCliente, String telefonoCliente, int numeroPersone) {
        this.laboratorio = laboratorio;
        this.nomeCliente = nomeCliente;
        this.cognomeCliente = cognomeCliente;
        this.emailCliente = emailCliente;
        this.telefonoCliente = telefonoCliente;
        this.numeroPersone = numeroPersone;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public Laboratorio getLaboratorio() {
        return laboratorio;
    }

    public void setLaboratorio(Laboratorio laboratorio) {
        this.laboratorio = laboratorio;
    }

    public User getUtente() {
        return utente;
    }

    public void setUtente(User utente) {
        this.utente = utente;
    }

    public String getNomeCliente() {
        return nomeCliente;
    }

    public void setNomeCliente(String nomeCliente) {
        this.nomeCliente = nomeCliente;
    }

    public String getCognomeCliente() {
        return cognomeCliente;
    }

    public void setCognomeCliente(String cognomeCliente) {
        this.cognomeCliente = cognomeCliente;
    }

    public String getEmailCliente() {
        return emailCliente;
    }

    public void setEmailCliente(String emailCliente) {
        this.emailCliente = emailCliente;
    }

    public String getTelefonoCliente() {
        return telefonoCliente;
    }

    public void setTelefonoCliente(String telefonoCliente) {
        this.telefonoCliente = telefonoCliente;
    }

    public int getNumeroPersone() {
        return numeroPersone;
    }

    public void setNumeroPersone(int numeroPersone) {
        this.numeroPersone = numeroPersone;
    }

    public StatoPrenotazione getStato() {
        return stato;
    }

    public void setStato(StatoPrenotazione stato) {
        this.stato = stato;
    }

    public LocalDateTime getDataPrenotazione() {
        return dataPrenotazione;
    }

    public void setDataPrenotazione(LocalDateTime dataPrenotazione) {
        this.dataPrenotazione = dataPrenotazione;
    }
}