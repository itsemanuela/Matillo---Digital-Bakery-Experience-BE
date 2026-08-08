package emanuela.carrubba.matillo_bakery.config;

import emanuela.carrubba.matillo_bakery.security.JwtAuthFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;

    public SecurityConfig(JwtAuthFilter jwtAuthFilter) {
        this.jwtAuthFilter = jwtAuthFilter;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        // TODO: aggiungere qui anche i domini reali quando fai il deploy
        configuration.setAllowedOrigins(List.of(
                "http://localhost:5173",
                "http://127.0.0.1:5173"
        ));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(csrf -> csrf.disable())

                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        // Pubblici: chiunque può leggere il catalogo, registrarsi, fare login
                        .requestMatchers(HttpMethod.GET, "/api/prodotti/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/auth/login").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/utenti").permitAll()
                        // Checkout ospite: la creazione di un ordine non richiede login.
                        .requestMatchers(HttpMethod.POST, "/api/ordini").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/chat").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/laboratori/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/laboratori/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/laboratori/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/laboratori/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/prenotazioni").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PATCH, "/api/prenotazioni/**").authenticated()
                        .requestMatchers(HttpMethod.PUT, "/api/prenotazioni/**").authenticated()

                        // Solo ADMIN può modificare il catalogo
                        .requestMatchers(HttpMethod.POST, "/api/prodotti/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/prodotti/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PATCH, "/api/prodotti/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/prodotti/**").hasRole("ADMIN")

                        // Solo ADMIN può vedere TUTTI gli ordini, cercare gli ordini

                        .requestMatchers(HttpMethod.GET, "/api/ordini").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/ordini/utente/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PATCH, "/api/ordini/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/ordini/**").hasRole("ADMIN")

                        // Tutto il resto richiede solo di essere autenticati
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}