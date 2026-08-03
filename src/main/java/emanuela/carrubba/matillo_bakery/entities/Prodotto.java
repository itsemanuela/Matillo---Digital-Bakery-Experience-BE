package emanuela.carrubba.matillo_bakery.entities;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "prodotto")
public class Prodotto {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID uuid;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false)
    private double prezzo;

    @Column(nullable = false)
    private int quantità;

    @Column(nullable = false)
    private String descrizione;

    @Enumerated(EnumType.STRING)
    private Categoria categoria;

    private String immagine;

    @Column(nullable = false)
    private boolean disponibile = true;

    @Column(nullable = false)
    private boolean bestseller = false;

    public Prodotto(String nome, double prezzo, int quantità, String descrizione, Categoria categoria) {
        this.nome = nome;
        this.prezzo = prezzo;
        this.quantità = quantità;
        this.descrizione = descrizione;
        this.categoria = categoria;
    }

    // Costruttore vuoto
    public Prodotto() {}

    // Getters e Setters
    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public double getPrezzo() {
        return prezzo;
    }

    public void setPrezzo(double prezzo) {
        this.prezzo = prezzo;
    }

    public int getQuantità() {
        return quantità;
    }

    public void setQuantità(int quantità) {
        this.quantità = quantità;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    public String getImmagine() {
        return immagine;
    }

    public void setImmagine(String immagine) {
        this.immagine = immagine;
    }

    public boolean isDisponibile() {
        return disponibile;
    }

    public void setDisponibile(boolean disponibile) {
        this.disponibile = disponibile;
    }

    public boolean isBestseller() {
        return bestseller;
    }

    public void setBestseller(boolean bestseller) {
        this.bestseller = bestseller;
    }

    @Override
    public String toString() {
        return "Prodotto{" +
                "uuid=" + uuid +
                ", nome='" + nome + '\'' +
                ", prezzo=" + prezzo +
                ", quantità=" + quantità +
                ", descrizione='" + descrizione + '\'' +
                ", categoria=" + categoria +
                ", immagine='" + immagine + '\'' +
                ", disponibile=" + disponibile +
                ", bestseller=" + bestseller +
                '}';
    }
}