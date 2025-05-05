package com.example.demo.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "billet")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Billet {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_billet")
    @SequenceGenerator(name = "seq_billet", sequenceName = "seq_billet", allocationSize = 1)
    @Column(name = "idbillet")
    private Integer idBillet;

    @ManyToOne
    @JoinColumn(name = "idres", nullable = false)
    private Reservation reservation;

    @Column(name = "numplace")
    private Integer numPlace;

}