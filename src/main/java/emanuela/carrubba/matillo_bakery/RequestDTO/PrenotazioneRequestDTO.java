package emanuela.carrubba.matillo_bakery.RequestDTO;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.UUID;

public record PrenotazioneRequestDTO(
        @NotNull(message = "Il laboratorio è obbligatorio")
        UUID laboratorioId,

        @NotNull(message = "Il numero di persone è obbligatorio")
        @Positive(message = "Il numero di persone deve essere almeno 1")
        Integer numeroPersone,

        String nomeCliente,
        String cognomeCliente,
        String emailCliente,
        String telefonoCliente
) {
}