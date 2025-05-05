package com.example.reservili.model;


import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class SpectacleSummary implements Parcelable {
    private int idSpec;
    private String titre;
    private String dateS;
    private String imageUrl;

    private boolean annulee;
    private String h_debut;
    private String ville;
    private String nomLieu;
    private int nbrSpect;
    private int capacite;

    public int getCapacite() {
        return capacite;
    }

    public void setCapacite(int capacite) {
        this.capacite = capacite;
    }

    public int getNbrSpect() {
        return nbrSpect;
    }

    public void setNbrSpect(int nbrSpect) {
        this.nbrSpect = nbrSpect;
    }




    public String getH_debut() {
        return h_debut;
    }

    public void setH_debut(String h_debut) {
        this.h_debut = h_debut;
    }

    public String getNomLieu() {
        return nomLieu;
    }

    public void setNomLieu(String nomLieu) {
        this.nomLieu = nomLieu;
    }

    public String getVille() {
        return ville;
    }

    public void setVille(String ville) {
        this.ville = ville;
    }



    public SpectacleSummary(int idSpec,String titre,String dateS,String imageUrl){
        this.idSpec=idSpec;
        this.titre=titre;
        this.dateS=dateS;
        this.imageUrl=imageUrl;
    }
    // Getters
    public int getIdSpec() {
        return idSpec;
    }

    public String getTitre() {
        return titre;
    }

    public String getDateS() {
        return dateS;
    }

    public String getImageUrl() {
        return imageUrl;
    }



    public boolean isAnnulee() {
        return annulee;
    }

    // Setters
    public void setIdSpec(int idSpec) {
        this.idSpec = idSpec;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public void setDateS(String dateS) {
        this.dateS = dateS;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }



    public void setAnnulee(boolean annulee) {
        this.annulee = annulee;
    }

    // Parcelable implementation (optional if you're passing it between activities/fragments)
    protected SpectacleSummary(Parcel in) {
        idSpec = in.readInt();
        nbrSpect=in.readInt();
        capacite=in.readInt();
        titre = in.readString();
        dateS = in.readString();
        imageUrl = in.readString();

        annulee = in.readByte() != 0;
    }

    public static final Creator<SpectacleSummary> CREATOR = new Creator<SpectacleSummary>() {
        @Override
        public SpectacleSummary createFromParcel(Parcel in) {
            return new SpectacleSummary(in);
        }

        @Override
        public SpectacleSummary[] newArray(int size) {
            return new SpectacleSummary[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(idSpec);
        parcel.writeString(titre);
        parcel.writeString(dateS);
        parcel.writeString(imageUrl);
        parcel.writeInt(nbrSpect);
        parcel.writeInt(capacite);
        parcel.writeByte((byte) (annulee ? 1 : 0));
    }


    public SpectacleSummary() {}
}
