package com.example.s328084s333761mappe2;

public class Møte {

    public Long _ID;
    public String Type;
    public String Sted;
    public String Tidspunkt;

    public Møte(String Type, String Sted, String Tidspunkt) {
        this.Type = Type;
        this.Sted = Sted;
        this.Tidspunkt = Tidspunkt;
    }

    public Møte(Long _ID, String Type, String Sted, String Tidspunkt) {
        this._ID = _ID;
        this.Type = Type;
        this.Sted = Sted;
        this.Tidspunkt = Tidspunkt;
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

    public String getTidspunkt() {
        return Tidspunkt;
    }

    public void setTidspunkt(String tidspunkt) {
        Tidspunkt = tidspunkt;
    }
}
