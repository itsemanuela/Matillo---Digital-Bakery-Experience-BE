package emanuela.carrubba.matillo_bakery.ResponseDTO;

import emanuela.carrubba.matillo_bakery.entities.User;

import java.util.UUID;

public record UtenteResponseDTO(
        UUID uuid,
        String nome,
        String cognome,
        String email,
        String telefono,
        String indirizzo,
        String ruolo
) {
    public static UtenteResponseDTO fromEntity(User user) {
        return new UtenteResponseDTO(
                user.getUuid(),
                user.getNome(),
                user.getCognome(),
                user.getEmail(),
                user.getTelefono(),
                user.getIndirizzo(),
                user.getRuolo().name()
        );
    }
}