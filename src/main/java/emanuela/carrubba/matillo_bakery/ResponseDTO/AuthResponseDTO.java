package emanuela.carrubba.matillo_bakery.ResponseDTO;

import java.util.UUID;

public record AuthResponseDTO(
        String token,
        UUID uuid,
        String nome,
        String email,
        String ruolo
) {
}
