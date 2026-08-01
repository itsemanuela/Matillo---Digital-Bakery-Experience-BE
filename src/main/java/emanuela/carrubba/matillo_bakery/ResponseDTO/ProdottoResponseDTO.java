package emanuela.carrubba.matillo_bakery.ResponseDTO;

import java.math.BigDecimal;

public record ProdottoResponseDTO(
        Long id,
        String nome,
        BigDecimal prezzo,
        String descrizione,
        String immagineUrl,
        Boolean disponibile
) {
}
