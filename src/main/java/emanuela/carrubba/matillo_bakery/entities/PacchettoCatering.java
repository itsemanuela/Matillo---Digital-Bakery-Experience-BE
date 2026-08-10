package emanuela.carrubba.matillo_bakery.entities;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "pacchetto_catering")
public class PacchettoCatering {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID uuid;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String descrizione;

    @Column(name = "prezzo_persona", nullable = false)
    private double prezzoPersona;

    @Column(name = "numero_minimo_persone", nullable = false)
    private int numeroMinimoPersone;

    @Column(columnDefinition = "TEXT")
    private String incluso;

    private String immagine;

    @Column(columnDefinition = "TEXT")
    private String galleria;

    public PacchettoCatering() {}

    public PacchettoCatering(String nome, String descrizione, double prezzoPersona, int numeroMinimoPersone) {
        this.nome = nome;
        this.descrizione = descrizione;
        this.prezzoPersona = prezzoPersona;
        this.numeroMinimoPersone = numeroMinimoPersone;
    }

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

    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public double getPrezzoPersona() {
        return prezzoPersona;
    }

    public void setPrezzoPersona(double prezzoPersona) {
        this.prezzoPersona = prezzoPersona;
    }

    public int getNumeroMinimoPersone() {
        return numeroMinimoPersone;
    }

    public void setNumeroMinimoPersone(int numeroMinimoPersone) {
        this.numeroMinimoPersone = numeroMinimoPersone;
    }

    public String getIncluso() {
        return incluso;
    }

    public void setIncluso(String incluso) {
        this.incluso = incluso;
    }

    public String getImmagine() {
        return immagine;
    }

    public void setImmagine(String immagine) {
        this.immagine = immagine;
    }

    public String getGalleria() {
        return galleria;
    }

    public void setGalleria(String galleria) {
        this.galleria = galleria;
    }

    @Override
    public String toString() {
        return "PacchettoCatering{" +
                "uuid=" + uuid +
                ", nome='" + nome + '\'' +
                ", descrizione='" + descrizione + '\'' +
                ", prezzoPersona=" + prezzoPersona +
                ", numeroMinimoPersone=" + numeroMinimoPersone +
                ", incluso='" + incluso + '\'' +
                ", immagine='" + immagine + '\'' +
                ", galleria='" + galleria + '\'' +
                '}';
    }
}