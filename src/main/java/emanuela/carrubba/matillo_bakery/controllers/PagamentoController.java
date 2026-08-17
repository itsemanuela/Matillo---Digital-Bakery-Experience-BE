package emanuela.carrubba.matillo_bakery.controllers;

import com.stripe.Stripe;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.exception.StripeException;
import com.stripe.model.Event;
import com.stripe.model.checkout.Session;
import com.stripe.net.Webhook;
import com.stripe.param.checkout.SessionCreateParams;
import emanuela.carrubba.matillo_bakery.entities.DettaglioOrdine;
import emanuela.carrubba.matillo_bakery.entities.Ordine;
import emanuela.carrubba.matillo_bakery.StatoOrdine;
import emanuela.carrubba.matillo_bakery.services.OrdineService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/pagamenti")
public class PagamentoController {

    private final OrdineService ordineService;

    @Value("${stripe.secret.key}")
    private String stripeSecretKey;

    @Value("${stripe.webhook.secret}")
    private String webhookSecret;

    @Value("${app.frontend.url:http://localhost:5173}")
    private String frontendUrl;

    public PagamentoController(OrdineService ordineService) {
        this.ordineService = ordineService;
    }

    @PostConstruct
    public void init() {
        Stripe.apiKey = stripeSecretKey;
        System.out.println("[PagamentoController] Stripe key caricata — lunghezza: "
                + (stripeSecretKey == null ? "null" : stripeSecretKey.length())
                + ", inizia con: " + (stripeSecretKey == null ? "null" : stripeSecretKey.substring(0, Math.min(12, stripeSecretKey.length())))
                + ", finisce con: " + (stripeSecretKey == null ? "null" : stripeSecretKey.substring(Math.max(0, stripeSecretKey.length() - 6))));
    }


    @PostMapping("/crea-sessione/{ordineId}")
    public ResponseEntity<Map<String, String>> creaSessione(@PathVariable UUID ordineId) throws StripeException {
        Ordine ordine = ordineService.trovaPerId(ordineId);

        SessionCreateParams.Builder paramsBuilder = SessionCreateParams.builder()
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setSuccessUrl(frontendUrl + "/checkout/successo?session_id={CHECKOUT_SESSION_ID}")
                .setCancelUrl(frontendUrl + "/checkout")
                .putMetadata("ordineId", ordine.getUuid().toString());

        for (DettaglioOrdine dettaglio : ordine.getDettagli()) {
            long prezzoInCentesimi = Math.round(dettaglio.getPrezzoUnitario() * 100);

            paramsBuilder.addLineItem(
                    SessionCreateParams.LineItem.builder()
                            .setQuantity((long) dettaglio.getQuantita())
                            .setPriceData(
                                    SessionCreateParams.LineItem.PriceData.builder()
                                            .setCurrency("eur")
                                            .setUnitAmount(prezzoInCentesimi)
                                            .setProductData(
                                                    SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                                            .setName(dettaglio.getProdotto().getNome())
                                                            .build()
                                            )
                                            .build()
                            )
                            .build()
            );
        }

        Session session = Session.create(paramsBuilder.build());

        Map<String, String> risposta = new HashMap<>();
        risposta.put("url", session.getUrl());
        return ResponseEntity.ok(risposta);
    }

    @PostMapping("/webhook")
    public ResponseEntity<String> webhook(@RequestBody String payload,
                                          @RequestHeader("Stripe-Signature") String signature) {
        Event event;
        try {
            event = Webhook.constructEvent(payload, signature, webhookSecret);
        } catch (SignatureVerificationException e) {
            return ResponseEntity.badRequest().body("Firma non valida");
        }

        if ("checkout.session.completed".equals(event.getType())) {
            event.getDataObjectDeserializer().getObject().ifPresent(obj -> {
                Session session = (Session) obj;
                String ordineIdStr = session.getMetadata().get("ordineId");
                if (ordineIdStr != null) {
                    Ordine ordine = ordineService.trovaPerId(UUID.fromString(ordineIdStr));
                    ordine.setStato(StatoOrdine.PAGATO);
                    ordineService.salva(ordine);
                }
            });
        }

        return ResponseEntity.ok("ok");
    }
}