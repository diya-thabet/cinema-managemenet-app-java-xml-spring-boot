package com.example.reservili.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.LinkedList;

public class Spectacle implements Parcelable {
  private String description;

    public Spectacle() {
    }

    private String dureeS;
  private String adresseLieu;
  private ArrayList<Rubrique> rubriques;
    private ArrayList<Crenaux> crenaux;

    public ArrayList<Crenaux> getCrenaux() {
        return crenaux;
    }

    public void setCrenaux(ArrayList<Crenaux> crenaux) {
        this.crenaux = crenaux;
    }

    public Spectacle(String adresseLieu, ArrayList<Rubrique> rubriques, String dureeS, String description, ArrayList<Crenaux> crenaux) {
        this.adresseLieu = adresseLieu;
        this.rubriques = rubriques;
        this.dureeS = dureeS;
        this.description = description;
        this.crenaux = crenaux;
    }

    protected Spectacle(Parcel in) {
        description = in.readString();
        dureeS = in.readString();
        adresseLieu = in.readString();
        rubriques = in.createTypedArrayList(Rubrique.CREATOR);
        crenaux = in.createTypedArrayList(Crenaux.CREATOR);
    }

    public static final Creator<Spectacle> CREATOR = new Creator<Spectacle>() {
        @Override
        public Spectacle createFromParcel(Parcel in) {
            return new Spectacle(in);
        }

        @Override
        public Spectacle[] newArray(int size) {
            return new Spectacle[size];
        }
    };

    public String getAdresseLieu() {
        return adresseLieu;
    }

    public void setAdresseLieu(String adresseLieu) {
        this.adresseLieu = adresseLieu;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDureeS() {
        return dureeS;
    }

    public void setDureeS(String dureeS) {
        this.dureeS = dureeS;
    }

    public ArrayList<Rubrique> getRubriques() {
        return rubriques;
    }

    public void setRubriques(ArrayList<Rubrique> rubriques) {
        this.rubriques = rubriques;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(description);
        dest.writeString(dureeS);
        dest.writeString(adresseLieu);
        dest.writeTypedList(rubriques);
        dest.writeTypedList(crenaux);
    }
}

