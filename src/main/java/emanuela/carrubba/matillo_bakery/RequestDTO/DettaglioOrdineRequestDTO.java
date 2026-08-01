package emanuela.carrubba.matillo_bakery.RequestDTO;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.UUID;

public record DettaglioOrdineRequestDTO(
        @NotNull(message = "L'ID del prodotto è obbligatorio")
        UUID idProdotto,

        @NotNull(message = "La quantità è obbligatoria")
        @Positive(message = "La quantità deve essere almeno 1")
        Integer quantita
) {
}
