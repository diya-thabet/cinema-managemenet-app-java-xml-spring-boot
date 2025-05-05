package com.example.demo.repository;

import com.example.demo.model.Client;
import com.example.demo.model.Crenaux;
import com.example.demo.model.Reservation;
import com.example.demo.model.ReservationRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Integer> {

    /**
     * Crée une réservation ; si request.getIdClt()>0 on considère
     * que l'utilisateur est déjà enregistré, sinon on l'insère d'abord.
     */
    default void createReservation(ReservationRequest request, ClientRepository clientRepository, CrenauxRepository crenauxRepository) {
        Integer idClt = request.getIdClt();
        Client client;

        // 1) Si pas d'utilisateur connecté, créer un client anonyme
        if (idClt == null || idClt <= 0) {
            client = new Client();
            client.setNomClt(request.getNomClt());
            client.setPrenomClt(request.getPrenomClt());
            client.setTel(request.getTel());
            client.setEmail(request.getEmail());
            client.setMotP("0");
            clientRepository.save(client);
        } else {
            client = clientRepository.findById(idClt)
                    .orElseThrow(() -> new RuntimeException("Client non trouvé"));
        }

        // 2) Vérifier la capacité résiduelle
        Crenaux crenaux = crenauxRepository.findById(request.getIdDateLieu())
                .orElseThrow(() -> new RuntimeException("Créneau non trouvé"));
        int reservedQty = findReservedQuantityByDateLieuId(request.getIdDateLieu());
        int placesRestantes = crenaux.getLieu().getCapacite() - reservedQty;
        if (request.getQte() > placesRestantes) {
            throw new RuntimeException(
                    "Capacité insuffisante : il ne reste que " + placesRestantes + " places."
            );
        }

        // 3) Créer la réservation
        Reservation reservation = new Reservation();
        reservation.setClient(client);
        reservation.setSpectacle(crenaux.getSpectacle()); // Use Crenaux’s Spectacle
        reservation.setCategorieTckt(request.getCategorieTckt());
        reservation.setQte(request.getQte());
        reservation.setDateLieu(crenaux);
        reservation.setPaymentMethod(request.getPaymentMethod());
        reservation.setPrixPaye(request.getPrixpaye());
        save(reservation);
    }

    @Query("SELECT COALESCE(SUM(r.qte), 0) FROM Reservation r WHERE r.dateLieu.idDateLieu = :idDateLieu")
    int findReservedQuantityByDateLieuId(Integer idDateLieu);

    @Query("SELECT c FROM Reservation r JOIN r.dateLieu c WHERE r.client.idClt = :clientId ORDER BY c.dateLieu")
    List<Crenaux> findCrenauxByClientId(Integer clientId);
}