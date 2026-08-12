package emanuela.carrubba.matillo_bakery.ResponseDTO;

import emanuela.carrubba.matillo_bakery.StatoOrdine;

import java.time.LocalDateTime;
import java.util.UUID;

public class OrdineStatoResponseDTO {

    private UUID uuid;
    private StatoOrdine stato;
    private LocalDateTime dataOrdine;

    public OrdineStatoResponseDTO(UUID uuid, StatoOrdine stato, LocalDateTime dataOrdine) {
        this.uuid = uuid;
        this.stato = stato;
        this.dataOrdine = dataOrdine;
    }

    public UUID getUuid() {
        return uuid;
    }

    public StatoOrdine getStato() {
        return stato;
    }

    public LocalDateTime getDataOrdine() {
        return dataOrdine;
    }
}