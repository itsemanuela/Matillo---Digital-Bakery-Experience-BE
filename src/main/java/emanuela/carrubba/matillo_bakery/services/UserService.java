package emanuela.carrubba.matillo_bakery.services;

import emanuela.carrubba.matillo_bakery.entities.User;
import emanuela.carrubba.matillo_bakery.exceptions.NotFoundException;
import emanuela.carrubba.matillo_bakery.repositories.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {

    private static final int DURATA_TOKEN_MINUTI = 30;

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }
    public List<User> trovaTutti(){
        return userRepository.findAll();
    }
    public User trovaPerId(UUID uuid){
        return userRepository.findById(uuid).orElseThrow(()->new NotFoundException("User con id" + uuid + "non è stato trovato!"));
    }
    public User trovaPerEmail(String email){
        return userRepository.findByEmail(email).orElseThrow(()->new NotFoundException("User con email" + email + "non è stato trovato!"));
    }

    public boolean esisteEmail(String email) {
        return userRepository.findByEmail(email).isPresent();
    }

    public User salvaUtente(User user){
        return userRepository.save(user);
    }
    public void eliminaUtenteById(UUID uuid){
        userRepository.deleteById(uuid);
    }

    // Recupero password — genera un token valido DURATA_TOKEN_MINUTI minuti

    public Optional<User> generaTokenReset(String email) {
        Optional<User> utenteOpt = userRepository.findByEmail(email);
        if (utenteOpt.isEmpty()) {
            return Optional.empty();
        }

        User utente = utenteOpt.get();
        utente.setResetToken(UUID.randomUUID().toString());
        utente.setResetTokenScadenza(LocalDateTime.now().plusMinutes(DURATA_TOKEN_MINUTI));
        return Optional.of(userRepository.save(utente));
    }

    // Verifica il token, aggiorna la password, e invalida il token

    public void resettaPassword(String token, String nuovaPasswordInChiaro) {
        User utente = userRepository.findByResetToken(token)
                .orElseThrow(() -> new NotFoundException("Token di reset non valido."));

        if (utente.getResetTokenScadenza() == null
                || utente.getResetTokenScadenza().isBefore(LocalDateTime.now())) {
            throw new NotFoundException("Il link per reimpostare la password è scaduto.");
        }

        utente.setPassword(passwordEncoder.encode(nuovaPasswordInChiaro));
        utente.setResetToken(null);
        utente.setResetTokenScadenza(null);
        userRepository.save(utente);
    }
}