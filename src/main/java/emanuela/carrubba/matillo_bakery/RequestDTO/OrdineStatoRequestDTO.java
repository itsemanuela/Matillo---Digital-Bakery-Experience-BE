package emanuela.carrubba.matillo_bakery.RequestDTO;

import emanuela.carrubba.matillo_bakery.StatoOrdine;
import jakarta.validation.constraints.NotNull;

public record OrdineStatoRequestDTO(
        @NotNull(message = "Lo stato è obbligatorio")
        StatoOrdine stato
) {
}
