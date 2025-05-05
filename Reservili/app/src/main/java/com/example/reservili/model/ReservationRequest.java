package com.example.reservili.model;

public class ReservationRequest {

    // -------------------------
    // Cas connecté
    // -------------------------
    private int    idClt;        // >0 si utilisateur déjà connecté

    // -------------------------
    // Cas invité
    // -------------------------
    private String nomClt;
    private String prenomClt;
    private String tel;
    private String email;

    // -------------------------
    // Toujours présent
    // -------------------------
    private int    idSpec;       // id du spectacle
    private long   idDateLieu;   // créneau choisi
    private String categorieTckt;
    private int    qte;
    private float  prixpaye;
    private String paymentMethod;

    public ReservationRequest() {}

    /**
     * Constructeur pour utilisateur connecté
     */
    public ReservationRequest(int idClt,
                              int idSpec,
                              long idDateLieu,
                              String categorieTckt,
                              int qte,
                              float prixpaye,
                              String paymentMethod,
                              String email,
                              String last,
                              String first) {
        this.idClt          = idClt;
        this.idSpec         = idSpec;
        this.idDateLieu     = idDateLieu;
        this.categorieTckt  = categorieTckt;
        this.qte            = qte;
        this.prixpaye       = prixpaye;
        this.paymentMethod  = paymentMethod;
        this.email=email;
        this.nomClt=first;
        this.prenomClt=last;

    }

    /**
     * Constructeur pour invité (on crée un client)
     */
    public ReservationRequest(String nomClt,
                              String prenomClt,
                              String tel,
                              String email,
                              int idSpec,
                              long idDateLieu,
                              String categorieTckt,
                              int qte,
                              float prixpaye,
                              String paymentMethod) {
        this.nomClt         = nomClt;
        this.prenomClt      = prenomClt;
        this.tel            = tel;
        this.email          = email;
        this.idSpec         = idSpec;
        this.idDateLieu     = idDateLieu;
        this.categorieTckt  = categorieTckt;
        this.qte            = qte;
        this.prixpaye       = prixpaye;
        this.paymentMethod  = paymentMethod;
    }

    // --- getters & setters ---
    public int getIdClt()               { return idClt; }
    public void setIdClt(int idClt)     { this.idClt = idClt; }

    public String getNomClt()           { return nomClt; }
    public void setNomClt(String n)     { this.nomClt = n; }

    public String getPrenomClt()        { return prenomClt; }
    public void setPrenomClt(String p)  { this.prenomClt = p; }

    public String getTel()              { return tel; }
    public void setTel(String t)        { this.tel = t; }

    public String getEmail()            { return email; }
    public void setEmail(String e)      { this.email = e; }

    public int getIdSpec()              { return idSpec; }
    public void setIdSpec(int s)        { this.idSpec = s; }

    public long getIdDateLieu()         { return idDateLieu; }
    public void setIdDateLieu(long d)   { this.idDateLieu = d; }

    public String getCategorieTckt()         { return categorieTckt; }
    public void setCategorieTckt(String c)   { this.categorieTckt = c; }

    public int getQte()                   { return qte; }
    public void setQte(int q)             { this.qte = q; }

    public float getPrixpaye()            { return prixpaye; }
    public void setPrixpaye(float p)      { this.prixpaye = p; }

    public String getPaymentMethod()           { return paymentMethod; }
    public void setPaymentMethod(String pm)    { this.paymentMethod = pm; }
}
