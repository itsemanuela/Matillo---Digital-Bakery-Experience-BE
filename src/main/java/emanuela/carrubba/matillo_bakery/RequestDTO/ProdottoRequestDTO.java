package emanuela.carrubba.matillo_bakery.RequestDTO;

import emanuela.carrubba.matillo_bakery.entities.Categoria;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;

public record ProdottoRequestDTO(
        @NotBlank(message = "Il nome del prodotto è obbligatorio")
        String nome,

        @NotNull(message = "Il prezzo è obbligatorio")
        @Positive(message = "Il prezzo deve essere maggiore di zero")
        BigDecimal prezzo,

        @NotNull(message = "La quantità è obbligatoria")
        @PositiveOrZero(message = "La quantità non può essere negativa")
        Integer quantità,

        String descrizione,

        @NotNull(message = "La categoria è obbligatoria")
        Categoria categoria,

        Boolean disponibile
) {
}
