package com.example.reservili.ui.signup;

public class SignupRequest {
    private String prenom;
    private String nom;
    private String email;
    private String tel;
    private String password;

    public SignupRequest(String email, String nom, String password, String prenom, String tel) {
        this.email    = email;
        this.nom      = nom;
        this.password = password;
        this.prenom   = prenom;
        this.tel      = tel;
    }

    public SignupRequest() {
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }
}
