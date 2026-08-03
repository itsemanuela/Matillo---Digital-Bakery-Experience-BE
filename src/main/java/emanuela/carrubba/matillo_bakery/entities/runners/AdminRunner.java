package emanuela.carrubba.matillo_bakery.entities.runners;

import emanuela.carrubba.matillo_bakery.RuoloUtente;
import emanuela.carrubba.matillo_bakery.entities.User;
import emanuela.carrubba.matillo_bakery.services.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;


@Component
public class AdminRunner implements CommandLineRunner {

    private static final String EMAIL_DA_PROMUOVERE = "emanuela_carrubba_@hotmail.com";

    private final UserService userService;

    public AdminRunner(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void run(String... args) {
        try {
            User utente = userService.trovaPerEmail(EMAIL_DA_PROMUOVERE);

            if (utente.getRuolo() != RuoloUtente.ADMIN) {
                utente.setRuolo(RuoloUtente.ADMIN);
                userService.salvaUtente(utente);
                System.out.println("[AdminRunner] Utente " + EMAIL_DA_PROMUOVERE + " promosso ad ADMIN.");
            } else {
                System.out.println("[AdminRunner] Utente " + EMAIL_DA_PROMUOVERE + " è già ADMIN, nessuna modifica.");
            }
        } catch (Exception e) {
            System.out.println("[AdminRunner] Utente " + EMAIL_DA_PROMUOVERE + " non trovato — verifica che si sia già registrato.");
        }
    }
}