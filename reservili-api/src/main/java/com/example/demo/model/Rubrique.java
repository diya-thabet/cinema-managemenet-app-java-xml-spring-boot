package com.example.demo.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "rubrique")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Rubrique {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_joker")
    @SequenceGenerator(name = "seq_joker", sequenceName = "seq_joker", allocationSize = 1)
    @Column(name = "idrub")
    private Integer idRub;

    @ManyToOne
    @JoinColumn(name = "idspec", nullable = false)
    private Spectacle spectacle;

    @ManyToOne
    @JoinColumn(name = "idart", nullable = false)
    private Artiste artiste;

    @Column(name = "h_debutr", nullable = false)
    private Float hDebutR;

    @Column(name = "dureerub", nullable = false)
    private Float dureeRub;

    @Column(name = "typer", nullable = false, length = 10)
    private String typeR;

}