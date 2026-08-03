

package emanuela.carrubba.matillo_bakery.entities.runners;

import emanuela.carrubba.matillo_bakery.entities.Categoria;
import emanuela.carrubba.matillo_bakery.entities.Prodotto;
import emanuela.carrubba.matillo_bakery.services.ProdottoService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;


@Component
public class salvaProdottoRunner implements CommandLineRunner {

    private final ProdottoService prodottoService;
    public salvaProdottoRunner(ProdottoService prodottoService) {
        this.prodottoService = prodottoService;
    }


    @Override
    public void run(String... args) throws Exception {

        //salvo un prodotto
        Prodotto nuovoProdotto = new Prodotto(
                "Croissant alla crema",
                2.50,
                100,
                "Fragrante croissant sfogliato ripieno di crema pasticcera",
                Categoria.DOLCI
        );
       Prodotto prodottoSalvato= prodottoService.salvaProdotto(nuovoProdotto);
        System.out.println("Prodotto salvato con successo! ID:" + prodottoSalvato.getUuid() + " " +  prodottoSalvato.getNome());
    }
}
