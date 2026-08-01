package emanuela.carrubba.matillo_bakery.ResponseDTO;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record LoginResponseDTO(
        Long id,
        LocalDateTime dataOrdine,
        String stato,
        String indirizzoSpedizione,
        String note,
        BigDecimal totale,
        List<DettaglioOrdineResponseDTO> dettagli
) {
}
