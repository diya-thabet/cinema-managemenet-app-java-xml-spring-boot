package com.example.demo.model;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class SpectacleDetails {

	private String description;

	private Float dureeS;

	private String adresseLieu;

	private List<Rubrique> rubriques;

	private List<Crenaux> crenaux;

}