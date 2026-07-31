package emanuela.carrubba.matillo_bakery.services;

import emanuela.carrubba.matillo_bakery.entities.Categoria;
import emanuela.carrubba.matillo_bakery.entities.Prodotto;
import emanuela.carrubba.matillo_bakery.exceptions.NotFoundException;
import emanuela.carrubba.matillo_bakery.repositories.ProdottoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ProdottoService {

    private final ProdottoRepository prodottoRepository;
    public ProdottoService(ProdottoRepository prodottoRepository) {
        this.prodottoRepository = prodottoRepository;
    }

    public List<Prodotto> findAll(){
        return prodottoRepository.findAll();
    }

    public List<Prodotto> trovaDisponibili(){
        return prodottoRepository.finByDisponibileTrue();
    }
    public List<Prodotto> findByCategoria(Categoria categoria){
        return prodottoRepository.findByCategoria(categoria);
    }
    public Prodotto trovaPerId(UUID uuid){
        return prodottoRepository.findById(uuid).orElseThrow(()-> new NotFoundException("Il prodotto con id" + uuid + "non è presente nei nostri database"));
    }

    public Prodotto salvaProdotto(Prodotto prodotto){
        return prodottoRepository.save(prodotto);
    }
    public void eliminaProdotto(Prodotto prodotto){
        prodottoRepository.delete(prodotto);
    }
public Prodotto aggiornaProdotto(UUID uuid, Prodotto prodottoAggiornato){
        Prodotto prodottoEsistente= trovaPerId(uuid);
    prodottoEsistente.setNome(prodottoAggiornato.getNome());
    prodottoEsistente.setPrezzo(prodottoAggiornato.getPrezzo());
    prodottoEsistente.setQuantità(prodottoAggiornato.getQuantità());
    prodottoEsistente.setDescrizione(prodottoAggiornato.getDescrizione());
    prodottoEsistente.setCategoria(prodottoAggiornato.getCategoria());
    prodottoEsistente.setImmagine(prodottoAggiornato.getImmagine());
    prodottoEsistente.setDisponibile(prodottoAggiornato.isDisponibile());
    return prodottoRepository.save(prodottoEsistente);
}
}
