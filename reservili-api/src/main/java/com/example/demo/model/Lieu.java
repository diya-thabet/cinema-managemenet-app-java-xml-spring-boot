package com.example.demo.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "lieu")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Lieu {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_joker")
    @SequenceGenerator(name = "seq_joker", sequenceName = "seq_joker", allocationSize = 1)
    @Column(name = "idlieu")
    private Integer idLieu;

    @Column(name = "nomlieu", nullable = false, length = 30)
    private String nomLieu;

    @Column(name = "adresse", nullable = false, length = 100)
    private String adresse;

    @Column(name = "ville", nullable = false, length = 30)
    private String ville;

    @Column(name = "capacite", nullable = false)
    private Integer capacite;

    @Column(name = "supprimee", nullable = false, length = 10)
    private String supprimee;

}