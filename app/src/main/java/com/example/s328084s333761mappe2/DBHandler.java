package com.example.s328084s333761mappe2;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.TimePicker;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class DBHandler extends SQLiteOpenHelper {
    static String TABLE_KONTAKTER = "Kontakter";
    static String TABLE_MOTER = "Moter";
    static String TABLE_MOTE_DELTAKELSE = "MoteDeltakelse";
    static String KEY_ID_KONTAKT = "Kontakt_ID";
    static String KEY_NAME = "Navn";
    static String KEY_PH_NO = "Telefon";
    static String KEY_ID_MOTE = "Mote_ID";
    static String KEY_TYPE = "Type";
    static String KEY_STED = "Sted";
    static String KEY_DATO = "Dato";
    static String KEY_TID = "Tidspunkt";
    static String KEY_ID_MOTEDELTAKELSE = "MoteDeltakelse_ID";
    static String KEY_ID_DELTAKER = "Deltaker_ID";
    static String KEY_ID_MOTE_ID = "Mote_ID_ID";
    static int DATABASE_VERSION = 2;
    static String DATABASE_NAME = "MoteDatabase";
    public DBHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        Log.d("SQL", "konstruktør");
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String LAG_TABELL_KONTAKTER = "CREATE TABLE " + TABLE_KONTAKTER + "(" + KEY_ID_KONTAKT +
                " INTEGER PRIMARY KEY," + KEY_NAME + " TEXT," + KEY_PH_NO +
                " TEXT" + ")";
        Log.d("SQL", LAG_TABELL_KONTAKTER);
        db.execSQL(LAG_TABELL_KONTAKTER);
        String LAG_TABELL_MØTER = "CREATE TABLE " + TABLE_MOTER + "(" + KEY_ID_MOTE +
                " INTEGER PRIMARY KEY," + KEY_TYPE + " TEXT," + KEY_STED + " TEXT," +
                  KEY_DATO + " TEXT," + KEY_TID + " TEXT)";
        Log.d("SQL", LAG_TABELL_MØTER);
        db.execSQL(LAG_TABELL_MØTER);
        String LAG_TABELL_MØTEDELTAKELSE = "CREATE TABLE " + TABLE_MOTE_DELTAKELSE + "(" + KEY_ID_MOTEDELTAKELSE +
                " INTEGER PRIMARY KEY, " + KEY_ID_MOTE_ID + " INTEGER, " + KEY_ID_DELTAKER +
                " INTEGER, FOREIGN KEY (" + KEY_ID_MOTE_ID + ") REFERENCES " + TABLE_MOTER +"("+ KEY_ID_MOTE +")," +
                " FOREIGN KEY (" + KEY_ID_DELTAKER + ") REFERENCES "+TABLE_KONTAKTER+"("+KEY_ID_KONTAKT+"));";

        Log.d("SQL", LAG_TABELL_MØTEDELTAKELSE);
        db.execSQL(LAG_TABELL_MØTEDELTAKELSE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MOTE_DELTAKELSE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_KONTAKTER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MOTER);

        onCreate(db);
    }

    public ArrayList<String> møteListe() {
        ArrayList<String> møteListe = new ArrayList<String>();
        List<Møte> møter = finnAlleMøter();
        for (Møte møte: møter ) {
            møteListe.add(møte.toString());
        }
        return møteListe;
    }

    public ArrayList<String> deltakerListe(List<MøteDeltakelse> deltakere) {
        ArrayList<String> deltakerListe = new ArrayList<>();

        for (MøteDeltakelse møteDeltakelse: deltakere ) {
            Kontakt kontakt = finnKontakt(møteDeltakelse.getDeltaker_ID().intValue());
            deltakerListe.add(kontakt.getNavn());
        }
        return deltakerListe;
    }

    public ArrayList<String> kontaktListe() {
        ArrayList<String> kontaktListe = new ArrayList<String>();
        List<Kontakt> kontakter = finnAlleKontakter();
        for (Kontakt kontakt : kontakter) {
            kontaktListe.add(kontakt.getNavn());
        }
        return kontaktListe;
    }

    public void leggTilMøte (Møte møte) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_TYPE, møte.getType());
        values.put(KEY_STED, møte.getSted());
        //Fiks
        values.put(KEY_DATO,møte.getDato());
        values.put(KEY_TID, møte.getTidspunkt());
        db.insert(TABLE_MOTER,null,values);
        db.close();
    }

    public void leggTilMøteDeltakelse (MøteDeltakelse møteDeltakelse) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_ID_MOTE_ID, møteDeltakelse.getMøte_ID());
        values.put(KEY_ID_DELTAKER, møteDeltakelse.getDeltaker_ID());
        db.insert(TABLE_MOTE_DELTAKELSE,null,values);
        db.close();
    }

    public void leggTilKontakt(Kontakt kontakt) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, kontakt.getNavn());
        values.put(KEY_PH_NO, kontakt.getTelefon());
        db.insert(TABLE_KONTAKTER,null,values);
        db.close();
    }

    public List<Kontakt> finnAlleKontakter() {
        List<Kontakt> kontaktListe = new ArrayList<Kontakt>();
        String selectQuery = "SELECT * FROM " + TABLE_KONTAKTER;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery,null);
        if (cursor.moveToFirst()) {
            do {
                Kontakt kontakt = new Kontakt();
                kontakt.set_ID(cursor.getLong(0));
                kontakt.setNavn(cursor.getString(1));
                kontakt.setTelefon(cursor.getString(2));
                kontaktListe.add(kontakt);
            } while (cursor.moveToNext());
            cursor.close();
            db.close();
        }
        return kontaktListe;
    }

    public ArrayList<Møte> finnAlleMøter() {
        ArrayList<Møte> møteListe = new ArrayList<Møte>();
        String selectQuery = "SELECT * FROM " + TABLE_MOTER;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery,null);
        if (cursor.moveToFirst()) {
            do {
                Møte møte = new Møte();
                møte.set_ID(cursor.getLong(0));
                møte.setType(cursor.getString(1));
                møte.setSted(cursor.getString(2));
                møte.setDato(cursor.getString(3));
                møte.setTidspunkt(cursor.getString(4));
                møteListe.add(møte);
            } while (cursor.moveToNext());
            cursor.close();
            db.close();
        }
        return møteListe;
    }

    public List<MøteDeltakelse> finnAlleMøteDeltakelser() {
        List<MøteDeltakelse> møteDeltakelseListe = new ArrayList<MøteDeltakelse>();
        String selectQuery = "SELECT * FROM " + TABLE_MOTE_DELTAKELSE;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery,null);
        if (cursor.moveToFirst()) {
            do {
                MøteDeltakelse møteDeltakelse = new MøteDeltakelse();
                møteDeltakelse.set_ID(cursor.getLong(0));
                møteDeltakelse.setMøte_ID(cursor.getLong(1));
                møteDeltakelse.setDeltaker_ID(cursor.getLong(2));
                møteDeltakelseListe.add(møteDeltakelse);
            } while (cursor.moveToNext());
            cursor.close();
            db.close();
        }
        return møteDeltakelseListe;
    }

    public List<MøteDeltakelse> finnMøteDeltakelseIMøte(Long møteId) {
        List<MøteDeltakelse> møteDeltakelseListe = new ArrayList<MøteDeltakelse>();
        String selectQuery = "SELECT * FROM " + TABLE_MOTE_DELTAKELSE + " WHERE " + KEY_ID_MOTE_ID + " = " + møteId;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery,null);
        if (cursor.moveToFirst()) {
            do {
                MøteDeltakelse møteDeltakelse = new MøteDeltakelse();
                møteDeltakelse.set_ID(cursor.getLong(0));
                møteDeltakelse.setMøte_ID(cursor.getLong(1));
                møteDeltakelse.setDeltaker_ID(cursor.getLong(2));
                møteDeltakelseListe.add(møteDeltakelse);
            } while (cursor.moveToNext());
            cursor.close();
            db.close();
        }
        return møteDeltakelseListe;
    }



    public void slettKontakt(Long inn_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_KONTAKTER, KEY_ID_KONTAKT + " =? ", new String[]{Long.toString(inn_id)});
        db.close();
    }

    public void slettKontakt(String navn_inn) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_KONTAKTER, KEY_NAME + " =? ", new String[]{navn_inn});
        db.close();
    }

    public void slettMøte(Long inn_id) {
        List<MøteDeltakelse> deltakere = finnMøteDeltakelseIMøte(inn_id);
        for (MøteDeltakelse deltaker: deltakere) {
            slettMøteDeltakelse(deltaker.get_ID());
        }
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_MOTER, KEY_ID_MOTE + " =? ", new String[]{Long.toString(inn_id)});
        db.close();
    }

    public void slettMøteDeltakelse(Long inn_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_MOTE_DELTAKELSE, KEY_ID_MOTEDELTAKELSE + " =? ", new String[]{Long.toString(inn_id)});
        db.close();
    }

    public int oppdaterKontakt(Kontakt kontakt) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values= new ContentValues();
        values.put(KEY_NAME, kontakt.getNavn());
        values.put(KEY_PH_NO, kontakt.getTelefon());
        int endret = db.update(TABLE_KONTAKTER, values, KEY_ID_KONTAKT + "= ?",
                new String[]{String.valueOf(kontakt.get_ID())});
        db.close();
        return endret;
    }

    public int oppdaterMøte(Møte møte) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values= new ContentValues();
        values.put(KEY_TYPE, møte.getType());
        values.put(KEY_STED, møte.getSted());
        values.put(KEY_DATO, møte.getDato());
        values.put(KEY_TID, møte.getTidspunkt());
        int endret = db.update(TABLE_MOTER, values, KEY_ID_MOTE + "= ?",
                new String[]{String.valueOf(møte.get_ID())});
        db.close();
        return endret;
    }

    public int oppdaterMøteDeltakelse(MøteDeltakelse møteDeltakelse) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values= new ContentValues();
        values.put(KEY_ID_MOTE_ID, møteDeltakelse.getMøte_ID());
        values.put(KEY_PH_NO, møteDeltakelse.getDeltaker_ID());
        int endret = db.update(TABLE_MOTE_DELTAKELSE, values, KEY_ID_MOTEDELTAKELSE + "= ?",
                new String[]{String.valueOf(møteDeltakelse.get_ID())});
        db.close();
        return endret;
    }

    public int finnAntallKontakter() {
        String sql= "SELECT * FROM " + TABLE_KONTAKTER;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor= db.rawQuery(sql, null);
        int antall = cursor.getCount();
        cursor.close();
        db.close();
        return antall;
    }

    public int finnAntallMøter() {
        String sql= "SELECT * FROM " + TABLE_MOTER;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor= db.rawQuery(sql, null);
        int antall = cursor.getCount();
        cursor.close();
        db.close();
        return antall;
    }

    public int finnAntallMøteDeltakelse() {
        String sql= "SELECT * FROM " + TABLE_MOTE_DELTAKELSE;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor= db.rawQuery(sql, null);
        int antall = cursor.getCount();
        cursor.close();
        db.close();
        return antall;
    }

    public Kontakt finnKontakt(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor= db.query(TABLE_KONTAKTER, new String[]{KEY_ID_KONTAKT, KEY_NAME, KEY_PH_NO},
                KEY_ID_KONTAKT + "=?", new String[]{String.valueOf(id)},
                null, null, null, null);
        if(cursor!= null) cursor.moveToFirst();
        Kontakt kontakt= new Kontakt(Long.parseLong(cursor.getString(0)), cursor.getString(1),
                cursor.getString(2));
        cursor.close();
        db.close();
        return kontakt;
    }

    public Kontakt finnKontakt(String navn) {
        Log.d("TAG", "Navn:" + navn + ".");
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor= db.query(TABLE_KONTAKTER, new String[]{KEY_ID_KONTAKT, KEY_NAME, KEY_PH_NO},
                KEY_NAME + "=?", new String[]{navn},
                null, null, null, null);
        if(cursor!= null) cursor.moveToFirst();
        Kontakt kontakt= new Kontakt(Long.parseLong(cursor.getString(0)), cursor.getString(1),
                cursor.getString(2));
        cursor.close();
        db.close();
        return kontakt;
    }


    public Møte finnMøte(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor= db.query(TABLE_MOTER, new String[]{KEY_ID_MOTE, KEY_TYPE, KEY_STED, KEY_DATO, KEY_TID},
                KEY_ID_MOTE + "=?", new String[]{String.valueOf(id)},
                null, null, null, null);
        if(cursor!= null) cursor.moveToFirst();
        Møte møte= new Møte(cursor.getLong(0), cursor.getString(1),
                cursor.getString(2), cursor.getString(3), cursor.getString(4));
        cursor.close();
        db.close();
        return møte;
    }

    public Møte finnMøte(String type, String sted, String dato, String tidspunkt) {
        List<Møte> møter = finnAlleMøter();
        Møte møte = null;
        for (Møte møte1 : møter) {
            if(type.equals(møte1.getType()) && sted.equals(møte1.getSted()) &&
                    dato.equals(møte1.getDato()) && tidspunkt.equals(møte1.getTidspunkt())) {
                møte = møte1;
            }
        }
        return møte;
    }

    public MøteDeltakelse finnMøteDeltakelse(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor= db.query(TABLE_MOTE_DELTAKELSE, new String[]{KEY_ID_MOTEDELTAKELSE, KEY_ID_MOTE_ID, KEY_ID_DELTAKER},
                KEY_ID_MOTEDELTAKELSE + "=?", new String[]{String.valueOf(id)},
                null, null, null, null);
        if(cursor!= null) cursor.moveToFirst();
        MøteDeltakelse møteDeltakelse= new MøteDeltakelse(cursor.getLong(0), cursor.getLong(1),
                cursor.getLong(2));
        cursor.close();
        db.close();
        return møteDeltakelse;
    }
}
