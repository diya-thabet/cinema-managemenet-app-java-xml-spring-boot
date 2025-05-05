package com.example.demo.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class ReservationRequest {
	 
    private int idClt;
    private int idSpec; 
    private String categorieTckt;
    private int qte; 
    private int idDateLieu;
    private String paymentMethod;
    private float prixpaye;
    private String nomClt;
    private String prenomClt;
    private String tel;
    private String email;
}
