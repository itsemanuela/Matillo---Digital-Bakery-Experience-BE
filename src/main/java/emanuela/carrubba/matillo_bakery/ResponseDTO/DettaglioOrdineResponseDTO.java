package emanuela.carrubba.matillo_bakery.ResponseDTO;

import java.math.BigDecimal;

public record DettaglioOrdineResponseDTO(
        Long id,
        Long idProdotto,
        String nomeProdotto,
        BigDecimal prezzoUnitario,
        Integer quantita,
        BigDecimal sottototale
) {
}
