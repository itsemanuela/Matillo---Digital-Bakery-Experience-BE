package emanuela.carrubba.matillo_bakery.RequestDTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDateTime;

public record LaboratorioRequestDTO(
        @NotBlank(message = "Il nome è obbligatorio")
        String nome,

        @NotBlank(message = "La descrizione è obbligatoria")
        String descrizione,

        String procedimento,

        @NotNull(message = "Data e ora sono obbligatorie")
        LocalDateTime dataOra,

        @NotNull(message = "I posti totali sono obbligatori")
        @Positive(message = "I posti totali devono essere maggiori di zero")
        Integer postiTotali,

        @NotNull(message = "Il prezzo è obbligatorio")
        @Positive(message = "Il prezzo deve essere maggiore di zero")
        Double prezzo
) {
}