package com.example.demo.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "client")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Data
@Setter
@ToString
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_joker")
    @SequenceGenerator(name = "seq_joker", sequenceName = "seq_joker", allocationSize = 1)
    @Column(name = "idclt")
    private Integer idClt;

    @Column(name = "nomclt", nullable = false, length = 10)
    private String nomClt;

    @Column(name = "prenomclt", nullable = false, length = 10)
    private String prenomClt;

    @Column(name = "tel", length = 8)
    private String tel;

    @Column(name = "email", nullable = false, length = 50)
    private String email;

    @Column(name = "motp", nullable = false, length = 100)
    private String motP;

}