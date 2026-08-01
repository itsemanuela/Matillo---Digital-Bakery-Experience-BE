package emanuela.carrubba.matillo_bakery.config;

import emanuela.carrubba.matillo_bakery.entities.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;

@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String secret;

    // Durata del token in millisecondi — 24 ore. Regolabile.
    private final long scadenzaMs = 1000 * 60 * 60 * 24;

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(secret.getBytes());
    }

    // Genera un token per l'utente appena autenticato.
    // Include email (subject), ruolo e uuid come claim personalizzati,
    // così il filtro può ricostruire l'identità senza rileggere il DB
    // ad ogni richiesta.
    public String generaToken(User user) {
        return Jwts.builder()
                .subject(user.getEmail())
                .claim("ruolo", user.getRuolo().name())
                .claim("uuid", user.getUuid().toString())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + scadenzaMs))
                .signWith(getSigningKey())
                .compact();
    }

    public String estraiEmail(String token) {
        return estraiClaims(token).getSubject();
    }

    public String estraiRuolo(String token) {
        return estraiClaims(token).get("ruolo", String.class);
    }

    public String estraiUuid(String token) {
        return estraiClaims(token).get("uuid", String.class);
    }

    public boolean isTokenValido(String token) {
        try {
            Claims claims = estraiClaims(token);
            return claims.getExpiration().after(new Date());
        } catch (Exception e) {

            return false;
        }
    }

    private Claims estraiClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}