package com.example.demo.service;

import com.example.demo.model.Crenaux;
import com.example.demo.model.ReservationRequest;
import com.example.demo.repository.ClientRepository;
import com.example.demo.repository.CrenauxRepository;
import com.example.demo.repository.ReservationRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReservationService {

	@Autowired
	private ReservationRepository reservationRepository;

	@Autowired
	private ClientRepository clientRepository;

	@Autowired
	private CrenauxRepository crenauxRepository;

	@Autowired
	private EmailService emailService;

	@Transactional
	public void createReservation(ReservationRequest request) {
		reservationRepository.createReservation(request, clientRepository, crenauxRepository);

		// Spectacle is now fetched in ReservationRepository
		emailService.sendReservationConfirmation(
				request.getEmail(),
				request,
				crenauxRepository.findById(request.getIdDateLieu())
						.map(Crenaux::getSpectacle)
						.orElseThrow(() -> new RuntimeException("Spectacle non trouvé"))
		);
	}

	public List<Crenaux> findByClientId(int clientId) {
		return reservationRepository.findCrenauxByClientId(clientId);
	}
}