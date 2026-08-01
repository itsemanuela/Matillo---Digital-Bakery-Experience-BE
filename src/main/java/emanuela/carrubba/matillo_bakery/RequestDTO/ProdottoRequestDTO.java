package emanuela.carrubba.matillo_bakery.RequestDTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record ProdottoRequestDTO(
        @NotBlank(message = "Il nome del prodotto è obbligatorio")
        String nome,

        @NotNull(message = "Il prezzo è obbligatorio")
        @Positive(message = "Il prezzo deve essere maggiore di zero")
        BigDecimal prezzo,

        String descrizione,

        Boolean disponibile
) {
}
