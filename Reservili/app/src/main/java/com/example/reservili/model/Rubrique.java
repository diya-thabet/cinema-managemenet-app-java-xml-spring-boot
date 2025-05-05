package com.example.reservili.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class Rubrique implements Parcelable {
    String nomart;
    String pnomart;
    String h_debutR;
    String dureeR;
    String typeR;


    public static final Creator<Rubrique> CREATOR = new Creator<Rubrique>() {
        @Override
        public Rubrique createFromParcel(Parcel in) {
            return new Rubrique(in);
        }

        @Override
        public Rubrique[] newArray(int size) {
            return new Rubrique[size];
        }
    };

    public Rubrique() {
    }

    public Rubrique(String dureeR, String h_debutR, String nomart, String pnomart, String typeR) {
        this.dureeR = dureeR;
        this.h_debutR = h_debutR;
        this.nomart = nomart;
        this.pnomart = pnomart;
        this.typeR = typeR;
    }

    public Rubrique(Parcel in) {
    }

    public String getDureeR() {
        return dureeR;
    }

    public void setDureeR(String dureeR) {
        this.dureeR = dureeR;
    }

    public String getH_debutR() {
        return h_debutR;
    }

    public void setH_debutR(String h_debutR) {
        this.h_debutR = h_debutR;
    }

    public String getNomart() {
        return nomart;
    }

    public void setNomart(String nomart) {
        this.nomart = nomart;
    }

    public String getPnomart() {
        return pnomart;
    }

    public void setPnomart(String pnomart) {
        this.pnomart = pnomart;
    }

    public String getTypeR() {
        return typeR;
    }

    public void setTypeR(String typeR) {
        this.typeR = typeR;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {

    }
}