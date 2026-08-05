package emanuela.carrubba.matillo_bakery.entities;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "laboratorio")
public class Laboratorio {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID uuid;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false, length = 1000)
    private String descrizione;

    @Column(columnDefinition = "TEXT")
    private String procedimento;

    @Column(name = "data_ora", nullable = false)
    private LocalDateTime dataOra;

    @Column(name = "posti_totali", nullable = false)
    private int postiTotali;

    @Column(name = "posti_disponibili", nullable = false)
    private int postiDisponibili;

    @Column(nullable = false)
    private double prezzo;

    private String immagine;

    public Laboratorio() {}

    public Laboratorio(String nome, String descrizione, LocalDateTime dataOra, int postiTotali, double prezzo) {
        this.nome = nome;
        this.descrizione = descrizione;
        this.dataOra = dataOra;
        this.postiTotali = postiTotali;
        this.postiDisponibili = postiTotali;
        this.prezzo = prezzo;
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

    public String getProcedimento() {
        return procedimento;
    }

    public void setProcedimento(String procedimento) {
        this.procedimento = procedimento;
    }

    public LocalDateTime getDataOra() {
        return dataOra;
    }

    public void setDataOra(LocalDateTime dataOra) {
        this.dataOra = dataOra;
    }

    public int getPostiTotali() {
        return postiTotali;
    }

    public void setPostiTotali(int postiTotali) {
        this.postiTotali = postiTotali;
    }

    public int getPostiDisponibili() {
        return postiDisponibili;
    }

    public void setPostiDisponibili(int postiDisponibili) {
        this.postiDisponibili = postiDisponibili;
    }

    public double getPrezzo() {
        return prezzo;
    }

    public void setPrezzo(double prezzo) {
        this.prezzo = prezzo;
    }

    public String getImmagine() {
        return immagine;
    }

    public void setImmagine(String immagine) {
        this.immagine = immagine;
    }
}