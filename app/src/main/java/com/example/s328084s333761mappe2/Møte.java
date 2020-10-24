package com.example.s328084s333761mappe2;

import androidx.annotation.NonNull;

public class Møte {
    public Long _ID;
    public String Type;
    public String Sted;
    public String Dato;
    public String Tidspunkt;

    public Møte(String Type, String Sted, String Dato, String Tidspunkt) {
        this.Type = Type;
        this.Sted = Sted;
        this.Dato = Dato;
        this.Tidspunkt = Tidspunkt;
    }

    public Møte(Long _ID, String Type, String Sted, String Dato, String Tidspunkt) {
        this._ID = _ID;
        this.Type = Type;
        this.Sted = Sted;
        this.Dato = Dato;
        this.Tidspunkt = Tidspunkt;
    }

    @NonNull
    @Override
    public String toString() {
        String streng = get_ID() + ": " + getType() + " Dato: " + getDato() + " Kl: " + getTidspunkt() + " " + getSted();
        return streng;
    }

    public Møte(){}

    public Long get_ID() {
        return _ID;
    }

    public void set_ID(Long _ID) {
        this._ID = _ID;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    public String getSted() {
        return Sted;
    }

    public void setSted(String sted) {
        Sted = sted;
    }

    public String getDato() {
        return Dato;
    }

    public void setDato(String dato) {
        Dato = dato;
    }

    public String getTidspunkt() {
        return Tidspunkt;
    }

    public void setTidspunkt(String tidspunkt) {
        Tidspunkt = tidspunkt;
    }
}
