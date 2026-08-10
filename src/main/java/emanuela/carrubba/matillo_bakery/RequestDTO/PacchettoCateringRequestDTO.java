package emanuela.carrubba.matillo_bakery.RequestDTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record PacchettoCateringRequestDTO(
        @NotBlank(message = "Il nome è obbligatorio")
        String nome,

        @NotBlank(message = "La descrizione è obbligatoria")
        String descrizione,

        @NotNull(message = "Il prezzo a persona è obbligatorio")
        @Positive(message = "Il prezzo deve essere maggiore di zero")
        Double prezzoPersona,

        @NotNull(message = "Il numero minimo di persone è obbligatorio")
        @Positive(message = "Il numero minimo deve essere maggiore di zero")
        Integer numeroMinimoPersone,

        String incluso
) {
}