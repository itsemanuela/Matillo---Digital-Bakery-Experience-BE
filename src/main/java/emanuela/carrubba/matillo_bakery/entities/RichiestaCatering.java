package emanuela.carrubba.matillo_bakery.entities;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "richiesta_catering")
public class RichiestaCatering {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID uuid;

    @ManyToOne
    @JoinColumn(name = "pacchetto_uuid", nullable = false)
    private PacchettoCatering pacchetto;

    @ManyToOne
    @JoinColumn(name = "utente_uuid", nullable = true)
    private User utente;

    @Column(name = "nome_cliente", nullable = false)
    private String nomeCliente;

    @Column(name = "cognome_cliente", nullable = false)
    private String cognomeCliente;

    @Column(name = "email_cliente", nullable = false)
    private String emailCliente;

    @Column(name = "telefono_cliente", nullable = false)
    private String telefonoCliente;

    @Column(name = "data_evento", nullable = false)
    private LocalDate dataEvento;

    @Column(name = "numero_persone", nullable = false)
    private int numeroPersone;

    @Column(columnDefinition = "TEXT")
    private String note;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatoRichiesta stato = StatoRichiesta.IN_ATTESA;

    @Column(name = "data_richiesta", nullable = false, updatable = false)
    private LocalDateTime dataRichiesta = LocalDateTime.now();

    public RichiestaCatering() {}

    public RichiestaCatering(PacchettoCatering pacchetto, User utente, String nomeCliente, String cognomeCliente,
                             String emailCliente, String telefonoCliente, LocalDate dataEvento,
                             int numeroPersone, String note) {
        this.pacchetto = pacchetto;
        this.utente = utente;
        this.nomeCliente = nomeCliente;
        this.cognomeCliente = cognomeCliente;
        this.emailCliente = emailCliente;
        this.telefonoCliente = telefonoCliente;
        this.dataEvento = dataEvento;
        this.numeroPersone = numeroPersone;
        this.note = note;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public PacchettoCatering getPacchetto() {
        return pacchetto;
    }

    public void setPacchetto(PacchettoCatering pacchetto) {
        this.pacchetto = pacchetto;
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

    public LocalDate getDataEvento() {
        return dataEvento;
    }

    public void setDataEvento(LocalDate dataEvento) {
        this.dataEvento = dataEvento;
    }

    public int getNumeroPersone() {
        return numeroPersone;
    }

    public void setNumeroPersone(int numeroPersone) {
        this.numeroPersone = numeroPersone;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public StatoRichiesta getStato() {
        return stato;
    }

    public void setStato(StatoRichiesta stato) {
        this.stato = stato;
    }

    public LocalDateTime getDataRichiesta() {
        return dataRichiesta;
    }

    public void setDataRichiesta(LocalDateTime dataRichiesta) {
        this.dataRichiesta = dataRichiesta;
    }

    @Override
    public String toString() {
        return "RichiestaCatering{" +
                "uuid=" + uuid +
                ", pacchetto=" + pacchetto +
                ", utente=" + utente +
                ", nomeCliente='" + nomeCliente + '\'' +
                ", cognomeCliente='" + cognomeCliente + '\'' +
                ", emailCliente='" + emailCliente + '\'' +
                ", telefonoCliente='" + telefonoCliente + '\'' +
                ", dataEvento=" + dataEvento +
                ", numeroPersone=" + numeroPersone +
                ", note='" + note + '\'' +
                ", stato=" + stato +
                ", dataRichiesta=" + dataRichiesta +
                '}';
    }
}