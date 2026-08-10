package emanuela.carrubba.matillo_bakery.entities;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "foto_galleria")
public class FotoGalleria {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID uuid;

    private String titolo;

    @Column(columnDefinition = "TEXT")
    private String galleria;

    public FotoGalleria() {}

    public FotoGalleria(String titolo) {
        this.titolo = titolo;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public String getTitolo() {
        return titolo;
    }

    public void setTitolo(String titolo) {
        this.titolo = titolo;
    }

    public String getGalleria() {
        return galleria;
    }

    public void setGalleria(String galleria) {
        this.galleria = galleria;
    }

    @Override
    public String toString() {
        return "FotoGalleria{" +
                "uuid=" + uuid +
                ", titolo='" + titolo + '\'' +
                ", galleria='" + galleria + '\'' +
                '}';
    }
}