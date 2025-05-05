package com.example.demo.repository;

import com.example.demo.model.Client;
import com.example.demo.model.EmailAlreadyExistsException;
import com.example.demo.model.LoginResponse;
import com.example.demo.model.SignupRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface AuthRepository extends JpaRepository<Client, Integer> {

    /**
     * Vérifie les identifiants, et renvoie les infos utilisateur si OK,
     * ou null si pas trouvé / mot de passe incorrect.
     */
    default LoginResponse verifyUser(String email, String password) {
        Client client = findByEmailAndMotP(email, password);
        if (client == null) {
            return null;
        }
        return new LoginResponse(
                client.getIdClt(),
                client.getPrenomClt(),
                client.getNomClt(),
                client.getEmail(),
                client.getTel()
        );
    }

    Client findByEmailAndMotP(String email, String password);

    Client findByEmail(String email);

    /**
     * Crée ou met à jour l’utilisateur selon l’email.
     * @return idClt du client.
     * @throws EmailAlreadyExistsException si compte déjà créé avec motP != "0".
     */
    default int createOrUpdateUser(SignupRequest req) {
        Client existingClient = findByEmail(req.getEmail());
        if (existingClient != null) {
            if (existingClient.getMotP() != null && !existingClient.getMotP().equals("0")) {
                throw new EmailAlreadyExistsException("Un compte existe déjà pour cet email");
            }
            // Mise à jour du client existant
            existingClient.setMotP(req.getPassword());
            existingClient.setNomClt(req.getNom());
            existingClient.setPrenomClt(req.getPrenom());
            existingClient.setTel(req.getTel());
            save(existingClient);
            return existingClient.getIdClt();
        } else {
            // Création d’un nouveau client
            Client newClient = new Client();
            newClient.setNomClt(req.getNom());
            newClient.setPrenomClt(req.getPrenom());
            newClient.setEmail(req.getEmail());
            newClient.setTel(req.getTel());
            newClient.setMotP(req.getPassword());
            save(newClient);
            return newClient.getIdClt();
        }
    }
}