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
        configuration.setAllowedOrigins(List.of(
                "http://localhost:5173",
                "http://127.0.0.1:5173",
                "https://matillo-bakery.netlify.app"
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
                        .requestMatchers(HttpMethod.GET, "/api/prodotti", "/api/prodotti/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/auth/login").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/auth/richiedi-reset").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/auth/reset-password").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/utenti").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/utenti/esiste").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/ordini").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/ordini/stato").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/pagamenti/crea-sessione/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/pagamenti/webhook").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/chat").permitAll()

                        .requestMatchers(HttpMethod.GET, "/api/laboratori", "/api/laboratori/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/laboratori/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/laboratori/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/laboratori/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/prenotazioni").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PATCH, "/api/prenotazioni/**").authenticated()
                        .requestMatchers(HttpMethod.PUT, "/api/prenotazioni/**").authenticated()

                        .requestMatchers(HttpMethod.GET, "/api/catering", "/api/catering/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/richieste-catering").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/catering/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/catering/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/catering/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/richieste-catering").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PATCH, "/api/richieste-catering/**").hasRole("ADMIN")

                        .requestMatchers(HttpMethod.GET, "/api/galleria-eventi", "/api/galleria-eventi/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/galleria-eventi/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/galleria-eventi/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/galleria-eventi/**").hasRole("ADMIN")

                        .requestMatchers(HttpMethod.POST, "/api/prodotti/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/prodotti/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PATCH, "/api/prodotti/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/prodotti/**").hasRole("ADMIN")

                        .requestMatchers(HttpMethod.GET, "/api/ordini").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/ordini/utente/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PATCH, "/api/ordini/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/ordini/**").authenticated()

                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}