package emanuela.carrubba.matillo_bakery.RequestDTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDate;
import java.util.UUID;

public record RichiestaCateringRequestDTO(
        @NotNull(message = "Il pacchetto è obbligatorio")
        UUID pacchettoId,

        @NotBlank(message = "Il nome è obbligatorio")
        String nomeCliente,

        @NotBlank(message = "Il cognome è obbligatorio")
        String cognomeCliente,

        @NotBlank(message = "L'email è obbligatoria")
        String emailCliente,

        @NotBlank(message = "Il telefono è obbligatorio")
        String telefonoCliente,

        @NotNull(message = "La data dell'evento è obbligatoria")
        LocalDate dataEvento,

        @NotNull(message = "Il numero di persone è obbligatorio")
        @Positive(message = "Il numero di persone deve essere almeno 1")
        Integer numeroPersone,

        String note
) {
}