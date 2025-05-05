package com.example.demo.service;

import com.example.demo.model.Spectacle;
import com.example.demo.model.SpectacleDetails;
import com.example.demo.repository.SpectacleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class SpectacleService {

    @Autowired
    private SpectacleRepository spectacleRepository;

    public List<Spectacle> findSpectacles(String titre, String date, String heure, String lieu, String ville) {
        // This method seems unused or incorrectly implemented; consider updating or removing
        return spectacleRepository.findSpectacles();
    }

    public List<Spectacle> searchSpectacles(String titre, String lieu, String ville, String dateS, String hDebut) {
        LocalDate parsedDateS = null;
        Float parsedHDebut = null;

        // Parse dateS if provided
        if (dateS != null && !dateS.isEmpty()) {
            try {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
                parsedDateS = LocalDate.parse(dateS, formatter);
            } catch (Exception e) {
                parsedDateS = null; // Invalid date format; treat as null to allow query to proceed
            }
        }

        // Parse hDebut if provided
        if (hDebut != null && !hDebut.isEmpty()) {
            try {
                parsedHDebut = Float.parseFloat(hDebut);
            } catch (NumberFormatException e) {
                parsedHDebut = null; // Invalid float format; treat as null to allow query to proceed
            }
        }

        return spectacleRepository.findSpectaclesByParams(titre, lieu, ville, parsedDateS, parsedHDebut);
    }

    public SpectacleDetails getSpectacleById(Long id) {
        Spectacle spectacle = spectacleRepository.findById(id.intValue())
                .orElseThrow(() -> new RuntimeException("Spectacle non trouvé"));
        SpectacleDetails details = spectacleRepository.findSpectacleDetailsById(id.intValue());
        details.setDescription(spectacle.getDescription());
        details.setDureeS(spectacle.getDureeS());
        details.setAdresseLieu(spectacle.getLieu().getAdresse());
        return details;
    }
}