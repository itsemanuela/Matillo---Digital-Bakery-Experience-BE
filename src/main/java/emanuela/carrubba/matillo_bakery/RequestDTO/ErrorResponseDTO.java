package emanuela.carrubba.matillo_bakery.RequestDTO;

import java.time.LocalDateTime;
import java.util.Map;

public record ErrorResponseDTO(
        LocalDateTime timestamp,
        int status,
        String error,
        String message,
        Map<String, String> validationErrors
) {
    public ErrorResponseDTO(int status, String error, String message) {
        this(LocalDateTime.now(), status, error, message, null);
    }

    public ErrorResponseDTO(int status, String error, Map<String, String> validationErrors) {
        this(LocalDateTime.now(), status, error, "Uno o più campi non sono validi", validationErrors);
    }
}
