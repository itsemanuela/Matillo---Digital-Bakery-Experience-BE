package emanuela.carrubba.matillo_bakery.services;

import emanuela.carrubba.matillo_bakery.entities.User;
import emanuela.carrubba.matillo_bakery.exceptions.NotFoundException;
import emanuela.carrubba.matillo_bakery.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository  = userRepository;
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

    public User salvaUtente(User user){
        return userRepository.save(user);
    }
    public void eliminaUtenteById(UUID uuid){
         userRepository.deleteById(uuid);
    }
}
