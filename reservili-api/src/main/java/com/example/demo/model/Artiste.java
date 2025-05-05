package com.example.demo.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "artiste")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Artiste {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_joker")
    @SequenceGenerator(name = "seq_joker", sequenceName = "seq_joker", allocationSize = 1)
    @Column(name = "idart")
    private Integer idArt;

    @Column(name = "nomart", nullable = false, length = 30)
    private String nomArt;

    @Column(name = "prenomart", nullable = false, length = 30)
    private String prenomArt;

    @Column(name = "specialite", nullable = false, length = 10)
    private String specialite;

    @Column(name = "description", length = 500)
    private String description;

    @Column(name = "imageurl", length = 500)
    private String imageUrl;

}