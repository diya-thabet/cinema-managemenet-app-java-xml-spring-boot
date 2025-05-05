package com.example.demo.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

@Entity
@Table(name = "spectacle")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Spectacle {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_joker")
	@SequenceGenerator(name = "seq_joker", sequenceName = "seq_joker", allocationSize = 1)
	@Column(name = "idspec")
	private Integer idSpec;

	@Column(name = "titre", nullable = false, length = 40)
	private String titre;

	@Column(name = "dates", nullable = false)
	private LocalDate dateS;

	@Column(name = "h_debut", nullable = false)
	private Float hDebut;

	@Column(name = "durees", nullable = false)
	private Float dureeS;

	@Column(name = "nbrspectateur", nullable = false)
	private Integer nbrSpectateur;

	@ManyToOne
	@JoinColumn(name = "idlieu", nullable = false)
	private Lieu lieu;

	@Column(name = "annulee", nullable = false, length = 3)
	private String annulee;

	@Column(name = "imageurl", length = 500)
	private String imageUrl;

	@Column(name = "description", length = 500)
	private String description;

}