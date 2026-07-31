package emanuela.carrubba.matillo_bakery.entities;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "dettagli_ordine")
public class DettaglioOrdine {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID uuid;

    // Riferimento al prodotto acquistato
    @ManyToOne
    @JoinColumn(name = "prodotto_uuid", nullable = false)
    private Prodotto prodotto;

    @Column(nullable = false)
    private int quantita;

    @Column(nullable = false)
    private double prezzoUnitario; // Prezzo al momento dell'ordine

    public DettaglioOrdine() {}

    public DettaglioOrdine(Prodotto prodotto, int quantita, double prezzoUnitario) {
        this.prodotto = prodotto;
        this.quantita = quantita;
        this.prezzoUnitario = prezzoUnitario;
    }

    // Getters e Setters
    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public Prodotto getProdotto() {
        return prodotto;
    }

    public void setProdotto(Prodotto prodotto) {
        this.prodotto = prodotto;
    }

    public int getQuantita() {
        return quantita;
    }

    public void setQuantita(int quantita) {
        this.quantita = quantita;
    }

    public double getPrezzoUnitario() {
        return prezzoUnitario;
    }

    public void setPrezzoUnitario(double prezzoUnitario) {
        this.prezzoUnitario = prezzoUnitario;
    }
}
