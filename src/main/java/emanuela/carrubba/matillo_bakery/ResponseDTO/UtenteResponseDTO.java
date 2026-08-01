package emanuela.carrubba.matillo_bakery.ResponseDTO;

public record UtenteResponseDTO(
        Long id,
        String nome,
        String cognome,
        String email,
        String telefono,
        String indirizzo,
        String ruolo
) {
}
