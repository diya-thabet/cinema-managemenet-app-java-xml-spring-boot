package com.example.demo.repository;

import com.example.demo.model.Crenaux;
import com.example.demo.model.Rubrique;
import com.example.demo.model.Spectacle;
import com.example.demo.model.SpectacleDetails;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class SpectacleRepositoryImpl implements SpectacleRepositoryCustom {

    @PersistenceContext
    private EntityManager em;

    @Override
    public List<Spectacle> findSpectacles() {
        return em.createQuery("SELECT s FROM Spectacle s", Spectacle.class)
                .getResultList();
    }

    @Override
    public SpectacleDetails findSpectacleDetailsById(Integer id) {
        Spectacle spectacle = em.find(Spectacle.class, id);
        if (spectacle == null) {
            throw new RuntimeException("Spectacle non trouvé");
        }

        List<Crenaux> crenaux = em.createQuery(
                        "SELECT c FROM Crenaux c WHERE c.spectacle.idSpec = :id", Crenaux.class)
                .setParameter("id", id)
                .getResultList();

        List<Rubrique> rubriques = em.createQuery(
                        "SELECT r FROM Rubrique r WHERE r.spectacle.idSpec = :id", Rubrique.class)
                .setParameter("id", id)
                .getResultList();

        return new SpectacleDetails(
                spectacle.getDescription(),
                spectacle.getDureeS(),
                spectacle.getLieu().getAdresse(),
                rubriques,
                crenaux
        );
    }

    @Override
    public Crenaux getCrenaux(Integer idSpec, Integer idCrenaux) {
        Crenaux c = em.find(Crenaux.class, idCrenaux);
        if (c == null || c.getSpectacle() == null || !c.getSpectacle().getIdSpec().equals(idSpec)) {
            throw new RuntimeException("Créneau non trouvé ou ne correspond pas au spectacle");
        }
        return c;
    }
}
