package emanuela.carrubba.matillo_bakery.RequestDTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record LoginRequestDTO(
        @NotBlank(message = "L'email è obbligatoria")
        @Email(message = "Inserisci un indirizzo email valido")
        String email,

        @NotBlank(message = "La password è obbligatoria")
        String password
) {
}
