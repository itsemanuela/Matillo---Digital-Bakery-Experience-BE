package emanuela.carrubba.matillo_bakery.Tools;

import kong.unirest.core.JsonNode;
import emanuela.carrubba.matillo_bakery.entities.User;
import kong.unirest.core.HttpResponse;
import kong.unirest.core.Unirest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class EmailSender {
    private final String domainName;
    private final String apiKey;

    public EmailSender(@Value("${mailgun.domain}") String domainName, @Value("${mailgun.api.key}") String apiKey) {
        this.domainName = domainName;
        this.apiKey = apiKey;
    }

    public void sendCustomRegistrationEmail(User recipient, String messaggioPersonalizzato) {
        HttpResponse<JsonNode> response = Unirest.post("https://api.mailgun.net/v3/" + this.domainName + "/messages")
                .basicAuth("api", this.apiKey)
                .field("from", "admin@" + this.domainName)
                .field("to", recipient.getEmail())
                .field("subject", "Benvenuto sulla piattaforma!")
                .field("text", messaggioPersonalizzato)
                .asJson();

        logRisposta("registrazione", response);
    }

    // metodo per inviare email custom esclusiva admin
    public void sendAdminCustomEmail(String recipientEmail, String subject, String text) {
        HttpResponse<JsonNode> response = Unirest.post("https://api.mailgun.net/v3/" + this.domainName + "/messages")
                .basicAuth("api", this.apiKey)
                .field("from", "admin@" + this.domainName)
                .field("to", recipientEmail)
                .field("subject", subject)
                .field("text", text)
                .asJson();

        logRisposta("admin custom", response);
    }


    public void sendOrderConfirmationEmail(String recipientEmail, String nomeCliente, UUID idOrdine, String prodottiHtml, double totale) {

        String htmlEmail = """
            <!DOCTYPE html>
            <html>
            <head>
              <meta charset="utf-8">
              <title>Conferma Ordine - Antico Forno Matillo</title>
            </head>
            <body style="margin: 0; padding: 0; background-color: #f4f1ea; font-family: 'Helvetica Neue', Helvetica, Arial, sans-serif;">
              <table border="0" cellpadding="0" cellspacing="0" width="100%%" style="background-color: #f4f1ea; padding: 40px 0;">
                <tr>
                  <td align="center">
                    <table border="0" cellpadding="0" cellspacing="0" width="600" style="background-color: #ffffff; border-radius: 16px; overflow: hidden; box-shadow: 0 4px 15px rgba(0,0,0,0.05);">
                      
                      <!-- Header -->
                      <tr>
                        <td style="background-color: #221915; padding: 35px; text-align: center;">
                          <span style="color: #EED972; font-size: 11px; letter-spacing: 3px; text-transform: uppercase; font-weight: bold; display: block; margin-bottom: 8px;">Antico Forno Matillo • Dal 1943</span>
                          <h1 style="color: #ffffff; margin: 0; font-size: 22px; font-family: serif;">Grazie per il tuo ordine, %s!</h1>
                        </td>
                      </tr>

                      <!-- Contenuto -->
                      <tr>
                        <td style="padding: 40px 30px;">
                          <p style="color: #555; font-size: 15px; line-height: 1.6; margin-top: 0;">
                            Il tuo ordine <strong>#%s</strong> è stato registrato con successo. Stiamo già preparando i prodotti con cura e ingredienti freschi.
                          </p>

                          <h3 style="color: #221915; border-bottom: 2px solid #EED972; padding-bottom: 8px; margin-top: 30px; font-size: 16px;">Riepilogo Ordine</h3>
                          
                          <table border="0" cellpadding="0" cellspacing="0" width="100%%" style="margin-top: 10px;">
                            %s
                            <tr>
                              <td style="padding: 15px 10px; font-weight: bold; color: #221915; font-size: 16px;">Totale</td>
                              <td style="padding: 15px 10px; text-align: right; font-weight: bold; color: #221915; font-size: 18px;">€ %.2f</td>
                            </tr>
                          </table>

                          <div style="background-color: #fcfbfa; border-left: 4px solid #EED972; padding: 15px; margin-top: 30px; border-radius: 4px;">
                            <p style="margin: 0; color: #666; font-size: 13px; line-height: 1.4;">
                              Ti avviseremo appena l'ordine sarà pronto. Per qualsiasi richiesta, rispondi pure a questa email.
                            </p>
                          </div>
                        </td>
                      </tr>

                      <!-- Footer -->
                      <tr>
                        <td style="background-color: #f9f6f0; padding: 20px; text-align: center; color: #888; font-size: 12px;">
                          <p style="margin: 0;">Antico Forno Matillo • Tutti i diritti riservati</p>
                        </td>
                      </tr>

                    </table>
                  </td>
                </tr>
              </table>
            </body>
            </html>
            """.formatted(nomeCliente, idOrdine.toString(), prodottiHtml, totale);

        HttpResponse<JsonNode> response = Unirest.post("https://api.mailgun.net/v3/" + this.domainName + "/messages")
                .basicAuth("api", this.apiKey)
                .field("from", "admin@" + this.domainName)
                .field("to", recipientEmail)
                .field("subject", "Conferma Ordine #" + idOrdine + " - Antico Forno Matillo")
                .field("html", htmlEmail)
                .asJson();

        logRisposta("conferma ordine #" + idOrdine, response);
    }


    private void logRisposta(String contesto, HttpResponse<JsonNode> response) {
        if (response.getStatus() >= 200 && response.getStatus() < 300) {
            System.out.println("[EmailSender] Email (" + contesto + ") inviata correttamente. Risposta: " + response.getBody());
        } else {
            System.err.println("[EmailSender] ERRORE invio email (" + contesto + ") — status " + response.getStatus() + ": " + response.getBody());
        }
    }
}