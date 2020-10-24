package com.example.s328084s333761mappe2;

public class Kontakt {
    public Long _ID;
    public String Navn;
    public String Telefon;

    public Long get_ID() {
        return _ID;
    }

    public void set_ID(Long _ID) {
        this._ID = _ID;
    }

    public String getNavn() {
        return Navn;
    }

    public void setNavn(String navn) {
        Navn = navn;
    }

    public String getTelefon() {
        return Telefon;
    }

    public void setTelefon(String telefon) {
        Telefon = telefon;
    }

    public Kontakt(String navn, String telefon) {
        Navn = navn;
        Telefon = telefon;
    }

    public Kontakt(Long _ID, String navn, String telefon) {
        this._ID = _ID;
        Navn = navn;
        Telefon = telefon;
    }

    public Kontakt() {
    }
}
