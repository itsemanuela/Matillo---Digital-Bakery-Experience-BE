package emanuela.carrubba.matillo_bakery.RequestDTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UtenteRequestDTO (
        @NotBlank(message = "Il nome è obbligatorio")
        String nome,

        @NotBlank(message = "Il cognome è obbligatorio")
        String cognome,

        @NotBlank(message = "L'email è obbligatoria")
        @Email(message = "Formato email non valido")
        String email,

        @NotBlank(message = "La password è obbligatoria")
        @Size(min = 6, message = "La password deve essere di almeno 6 caratteri")
        String password,

        @NotBlank(message = "Il campo telefono è vuoto!")
        String telefono,
        @NotBlank(message = "L'indirizzo per la spedizione è obbligatorio!")
        String indirizzo,

        @NotBlank(message= "Il cap è obbligatorio!")
        String cap,

        @NotBlank(message = "La città è obbligatoria!")
        String città
) {}
