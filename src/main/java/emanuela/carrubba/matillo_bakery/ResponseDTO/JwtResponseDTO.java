package emanuela.carrubba.matillo_bakery.ResponseDTO;

public record JwtResponseDTO    (
        String token,
        String tipo,
        String email,
        String ruolo
) {

    public JwtResponseDTO(String token, String email, String ruolo) {
        this(token, "Bearer", email, ruolo);
    }
}