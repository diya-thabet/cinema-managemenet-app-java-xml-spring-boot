package com.example.demo.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "reservation")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_reservation")
    @SequenceGenerator(name = "seq_reservation", sequenceName = "seq_reservation", allocationSize = 1)
    @Column(name = "idres")
    private Integer idRes;

    @ManyToOne
    @JoinColumn(name = "idclt", nullable = false)
    private Client client;

    @ManyToOne
    @JoinColumn(name = "idspec", nullable = false)
    private Spectacle spectacle;

    @Column(name = "categorietckt", nullable = false, length = 10)
    private String categorieTckt;

    @Column(name = "qte", nullable = false)
    private Integer qte;

    @ManyToOne
    @JoinColumn(name = "iddatelieu")
    private Crenaux dateLieu;

    @Column(name = "paymentmethod", nullable = false, length = 20)
    private String paymentMethod;

    @Column(name = "prixpaye", nullable = false)
    private Float prixPaye;

}