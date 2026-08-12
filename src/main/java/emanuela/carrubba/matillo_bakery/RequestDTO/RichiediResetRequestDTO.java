package emanuela.carrubba.matillo_bakery.RequestDTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record RichiediResetRequestDTO(
        @NotBlank @Email String email
) {
}