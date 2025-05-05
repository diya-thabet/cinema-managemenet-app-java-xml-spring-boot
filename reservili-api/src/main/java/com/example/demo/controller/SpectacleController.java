package com.example.demo.controller;

import com.example.demo.model.Spectacle;
import com.example.demo.model.SpectacleDetails;
import com.example.demo.service.SpectacleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/spectacles")
@CrossOrigin(origins = "*")
public class SpectacleController {

    @Autowired
    private SpectacleService spectacleService;

    @GetMapping
    public List<Spectacle> getSpectacles(
            @RequestParam(required = false) String titre,
            @RequestParam(required = false) String date,
            @RequestParam(required = false) String heure,
            @RequestParam(required = false) String lieu,
            @RequestParam(required = false) String ville) {
        return spectacleService.findSpectacles(titre, date, heure, lieu, ville);
    }

    @GetMapping("/search")
    public List<Spectacle> searchSpectacles(
            @RequestParam(required = false) String titre,
            @RequestParam(required = false) String lieu,
            @RequestParam(required = false) String ville,
            @RequestParam(required = false) String dateS,
            @RequestParam(required = false) String h_debut) {
        return spectacleService.searchSpectacles(titre, lieu, ville, dateS, h_debut);
    }

    @GetMapping("/{id}")
    public SpectacleDetails getSpectacle(@PathVariable Long id) {
        return spectacleService.getSpectacleById(id);
    }
}