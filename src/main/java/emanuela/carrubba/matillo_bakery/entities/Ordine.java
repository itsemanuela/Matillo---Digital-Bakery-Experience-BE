package emanuela.carrubba.matillo_bakery.entities;

import emanuela.carrubba.matillo_bakery.StatoOrdine;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "ordini")
public class Ordine {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID uuid;

    // Utente che ha effettuato l'ordine — NULLABLE: un ordine da ospite
    // (senza login) non ha nessun utente collegato.
    @ManyToOne
    @JoinColumn(name = "utente_uuid", nullable = true)
    private User utente;

    // Dati di contatto per gli ordini da OSPITE (utente == null).

    private String nomeCliente;
    private String cognomeCliente;
    private String emailCliente;
    private String telefonoCliente;

    @Column(name = "indirizzo_spedizione", nullable = false)
    private String indirizzoSpedizione;

    private String note;

    @Column(nullable = false)
    private double totale;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatoOrdine stato = StatoOrdine.IN_ELABORAZIONE;

    @Column(name = "data_ordine", nullable = false, updatable = false)
    private LocalDateTime dataOrdine = LocalDateTime.now();

    // Relazione uno-a-molti con i dettagli dell'ordine
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "ordine_uuid")
    private List<DettaglioOrdine> dettagli;


    public Ordine() {}

    public Ordine(User utente, String indirizzoSpedizione, String note, double totale, List<DettaglioOrdine> dettagli) {
        this.utente = utente;
        this.indirizzoSpedizione = indirizzoSpedizione;
        this.note = note;
        this.totale = totale;
        this.dettagli = dettagli;
    }

    // Costruttore per ordine da OSPITE (utente = null, dati di contatto valorizzati a mano)
    public Ordine(String nomeCliente, String cognomeCliente, String emailCliente, String telefonoCliente,
                  String indirizzoSpedizione, String note, double totale, List<DettaglioOrdine> dettagli) {
        this.nomeCliente = nomeCliente;
        this.cognomeCliente = cognomeCliente;
        this.emailCliente = emailCliente;
        this.telefonoCliente = telefonoCliente;
        this.indirizzoSpedizione = indirizzoSpedizione;
        this.note = note;
        this.totale = totale;
        this.dettagli = dettagli;
    }

    // Getters e Setters
    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
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

    public String getIndirizzoSpedizione() {
        return indirizzoSpedizione;
    }

    public void setIndirizzoSpedizione(String indirizzoSpedizione) {
        this.indirizzoSpedizione = indirizzoSpedizione;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public double getTotale() {
        return totale;
    }

    public void setTotale(double totale) {
        this.totale = totale;
    }

    public StatoOrdine getStato() {
        return stato;
    }

    public void setStato(StatoOrdine stato) {
        this.stato = stato;
    }

    public LocalDateTime getDataOrdine() {
        return dataOrdine;
    }

    public void setDataOrdine(LocalDateTime dataOrdine) {
        this.dataOrdine = dataOrdine;
    }

    public List<DettaglioOrdine> getDettagli() {
        return dettagli;
    }

    public void setDettagli(List<DettaglioOrdine> dettagli) {
        this.dettagli = dettagli;
    }

    @Override
    public String toString() {
        return "Ordine{" +
                "uuid=" + uuid +
                ", utente=" + utente +
                ", nomeCliente='" + nomeCliente + '\'' +
                ", emailCliente='" + emailCliente + '\'' +
                ", indirizzoSpedizione='" + indirizzoSpedizione + '\'' +
                ", note='" + note + '\'' +
                ", totale=" + totale +
                ", stato=" + stato +
                ", dataOrdine=" + dataOrdine +
                ", dettagli=" + dettagli +
                '}';
    }
}