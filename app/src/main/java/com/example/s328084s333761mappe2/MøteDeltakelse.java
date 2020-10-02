package com.example.s328084s333761mappe2;

public class MøteDeltakelse {
    public Long _ID;
    public Long Møte_ID;
    public Long[] Deltaker_IDListe;

    public MøteDeltakelse(Long Møte_ID) {
        this.Møte_ID = Møte_ID;
    }

    public MøteDeltakelse(Long Møte_ID, Long[] Liste) {
        this.Møte_ID = Møte_ID;
        this.Deltaker_IDListe = Liste;
    }

    public MøteDeltakelse(Long _ID, Long Møte_ID, Long[] Liste) {
        this._ID = _ID;
        this.Møte_ID = Møte_ID;
        this.Deltaker_IDListe = Liste;
    }

    public MøteDeltakelse(){}

    public Long get_ID() {
        return _ID;
    }

    public void set_ID(Long _ID) {
        this._ID = _ID;
    }

    public Long getMøte_ID() {
        return Møte_ID;
    }

    public void setMøte_ID(Long møte_ID) {
        Møte_ID = møte_ID;
    }

    public Long[] getDeltaker_IDListe() {
        return Deltaker_IDListe;
    }

    public void setDeltaker_IDListe(Long[] deltaker_IDListe) {
        Deltaker_IDListe = deltaker_IDListe;
    }
}
