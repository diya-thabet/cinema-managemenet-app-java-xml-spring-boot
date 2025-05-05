package com.example.reservili.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.time.LocalDate;

public class Crenaux implements Parcelable {
       Long id;
        String datecrn;
        String nomlieu;
        String adrss;
        String ville;
        int capact;
        String suppr;
        private float prixbalcon;
        private float prixorchestre;
        private float prixgalerie;
        private int reservedQty;

    public float getPrixbalcon() {
        return prixbalcon;
    }

    public void setPrixbalcon(float prixbalcon) {
        this.prixbalcon = prixbalcon;
    }

    public float getPrixgalerie() {
        return prixgalerie;
    }

    public void setPrixgalerie(float prixgalerie) {
        this.prixgalerie = prixgalerie;
    }

    public float getPrixorchestre() {
        return prixorchestre;
    }

    public void setPrixorchestre(float prixorchestre) {
        this.prixorchestre = prixorchestre;
    }

    public Crenaux(String adrss, int capact, String datecrn, Long id, String nomlieu, float prixbalcon, float prixgalerie, float prixorchestre, int reservedQty, String suppr, String ville) {
        this.adrss = adrss;
        this.capact = capact;
        this.datecrn = datecrn;
        this.id = id;
        this.nomlieu = nomlieu;
        this.prixbalcon = prixbalcon;
        this.prixgalerie = prixgalerie;
        this.prixorchestre = prixorchestre;
        this.reservedQty = reservedQty;
        this.suppr = suppr;
        this.ville = ville;
    }

    public int getReservedQty() {
        return reservedQty;
    }

    public void setReservedQty(int reservedQty) {
        this.reservedQty = reservedQty;
    }

    public static final Creator<Crenaux> CREATOR = new Creator<Crenaux>() {
        @Override
        public Crenaux createFromParcel(Parcel in) {
            return new Crenaux(in);
        }

        @Override
        public Crenaux[] newArray(int size) {
            return new Crenaux[size];
        }
    };


    public Crenaux() {
    }




    public Crenaux(Parcel in) {
    }

    public String getAdrss() {
        return adrss;
    }

    public void setAdrss(String adrss) {
        this.adrss = adrss;
    }

    public int getCapact() {
        return capact;
    }

    public void setCapact(int capact) {
        this.capact = capact;
    }

    public String getDatecrn() {
        return datecrn;
    }

    public void setDatecrn(String datecrn) {
        this.datecrn = datecrn;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomlieu() {
        return nomlieu;
    }

    public void setNomlieu(String nomlieu) {
        this.nomlieu = nomlieu;
    }

    public String getSuppr() {
        return suppr;
    }

    public void setSuppr(String suppr) {
        this.suppr = suppr;
    }

    public String getVille() {
        return ville;
    }

    public void setVille(String ville) {
        this.ville = ville;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {

        dest.writeLong(id);
        dest.writeString(datecrn);
        dest.writeString(nomlieu);
        dest.writeString(adrss);
        dest.writeString(ville);
        dest.writeInt(capact);
        dest.writeString(suppr);
        dest.writeFloat(prixbalcon);
        dest.writeFloat(prixorchestre);
        dest.writeFloat(prixgalerie);
        dest.writeInt(reservedQty);

    }








}
