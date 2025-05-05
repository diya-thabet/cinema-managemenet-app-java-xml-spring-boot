package com.example.demo.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

@Entity
@Table(name = "date_lieu")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Crenaux {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_date_lieu")
	@SequenceGenerator(name = "seq_date_lieu", sequenceName = "seq_date_lieu", allocationSize = 1)
	@Column(name = "iddatelieu")
	private Integer idDateLieu;

	@ManyToOne
	@JoinColumn(name = "idspec", nullable = false)
	private Spectacle spectacle;

	@Column(name = "datelieu", nullable = false)
	private LocalDate dateLieu;

	@ManyToOne
	@JoinColumn(name = "idlieu", nullable = false)
	private Lieu lieu;

	@Column(name = "prixbalcon", nullable = false)
	private Float prixBalcon;

	@Column(name = "prixgalerie", nullable = false)
	private Float prixGalerie;

	@Column(name = "prixorchestre", nullable = false)
	private Float prixOrchestre;

}