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

    // Utente che ha effettuato l'ordine
    @ManyToOne
    @JoinColumn(name = "utente_uuid", nullable = false)
    private User utente;

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

    // Costruttore vuoto
    public Ordine() {}


    public Ordine(User utente, double totale, List<DettaglioOrdine> dettagli) {
        this.utente = utente;
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
}