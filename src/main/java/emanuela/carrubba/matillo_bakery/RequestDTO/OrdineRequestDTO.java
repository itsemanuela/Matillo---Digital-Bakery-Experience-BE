package emanuela.carrubba.matillo_bakery.RequestDTO;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public record OrdineRequestDTO(
        @NotBlank(message = "L'indirizzo di spedizione è obbligatorio")
        String indirizzoSpedizione,

        String note,

        @NotEmpty(message = "L'ordine deve contenere almeno un prodotto")
        @Valid
        List<DettaglioOrdineRequestDTO> dettagli
) {
}
