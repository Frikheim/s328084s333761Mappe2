package com.example.s328084s333761mappe2;

public class MøteDeltakelse {
    public Long _ID;
    public Long Møte_ID;
    public Long Deltaker_ID;

    public MøteDeltakelse(Long Møte_ID) {
        this.Møte_ID = Møte_ID;
    }

    public MøteDeltakelse(Long Møte_ID, Long Deltaker_ID) {
        this.Møte_ID = Møte_ID;
        this.Deltaker_ID = Deltaker_ID;
    }

    public MøteDeltakelse(Long _ID, Long Møte_ID, Long Deltaker_ID) {
        this._ID = _ID;
        this.Møte_ID = Møte_ID;
        this.Deltaker_ID = Deltaker_ID;
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

    public Long getDeltaker_ID() {
        return Deltaker_ID;
    }

    public void setDeltaker_ID(Long deltaker_ID) {
        Deltaker_ID = deltaker_ID;
    }
}
