package com.example.s328084s333761mappe2;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class DBHandler extends SQLiteOpenHelper {
    static String TABLE_KONTAKTER = "Kontakter";
    static String TABLE_MØTER = "Møter";
    static String TABLE_MØTE_DELTAKELSE = "MøteDeltakelse";
    static String KEY_ID_KONTAKT = "_ID";
    static String KEY_NAME = "Navn";
    static String KEY_PH_NO = "Telefon";
    static String KEY_ID_MØTE = "_ID";
    static String KEY_TYPE = "Type";
    static String KEY_STED = "Sted";
    static String KEY_TID = "Tidspunkt";
    static String KEY_ID_MØTEDELTAKELSE = "_ID";
    static String KEY_ID_DELTAKER = "_ID";
    static String KEY_ID_MØTE_ID = "_ID";
    static int DATABASE_VERSION = 1;
    static String DATABASE_NAME = "MøteDatabase";
    public DBHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String LAG_TABELL_KONTAKTER = "CREATE TABLE " + TABLE_KONTAKTER + "(" + KEY_ID_KONTAKT +
                " INTEGER PRIMARY KEY," + KEY_NAME + " TEXT," + KEY_PH_NO +
                " TEXT" + ")";
        Log.d("SQL", LAG_TABELL_KONTAKTER);
        db.execSQL(LAG_TABELL_KONTAKTER);
        String LAG_TABELL_MØTER = "CREATE TABLE " + TABLE_MØTER + "(" + KEY_ID_MØTE +
                " INTEGER PRIMARY KEY," + KEY_TYPE + " TEXT," + KEY_STED +
                " TEXT," + KEY_TID + " TEXT" + ")";
        Log.d("SQL", LAG_TABELL_MØTER);
        db.execSQL(LAG_TABELL_MØTER);
        String LAG_TABELL_MØTEDELTAKELSE = "CREATE TABLE " + TABLE_MØTE_DELTAKELSE + "(" + KEY_ID_MØTEDELTAKELSE +
                " INTEGER PRIMARY KEY, " + KEY_ID_MØTE_ID + "INTEGER, FOREIGN KEY (" + KEY_ID_MØTE_ID + ") REFERENCES "+TABLE_MØTER+"("+KEY_ID_MØTE+"), "
                + KEY_ID_DELTAKER +" FOREIGN KEY (" + KEY_ID_DELTAKER + ") REFERENCES "+TABLE_KONTAKTER+"("+KEY_ID_KONTAKT+"));";

        Log.d("SQL", LAG_TABELL_MØTEDELTAKELSE);
        db.execSQL(LAG_TABELL_MØTEDELTAKELSE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_KONTAKTER);
        onCreate(db);
    }

    public void leggTilMøte (Møte møte) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_TYPE, møte.getType());
        values.put(KEY_STED, møte.getSted());
        values.put(KEY_TID, møte.getTidspunkt());
        db.insert(TABLE_MØTER,null,values);
        db.close();
    }

    public void leggTilMøteDeltakelse (MøteDeltakelse møteDeltakelse) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_ID_MØTE, møteDeltakelse.getMøte_ID());
        values.put(KEY_ID_KONTAKT, møteDeltakelse.getDeltaker_ID());
        db.insert(TABLE_MØTER,null,values);
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

    public List<Møte> finnAlleMøter() {
        List<Møte> møteListe = new ArrayList<Møte>();
        String selectQuery = "SELECT * FROM " + TABLE_MØTER;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery,null);
        if (cursor.moveToFirst()) {
            do {
                Møte møte = new Møte();
                møte.set_ID(cursor.getLong(0));
                møte.setType(cursor.getString(1));
                møte.setSted(cursor.getString(2));
                møte.setTidspunkt(cursor.getString(3));
                møteListe.add(møte);
            } while (cursor.moveToNext());
            cursor.close();
            db.close();
        }
        return møteListe;
    }



    public void slettKontakt(Long inn_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_KONTAKTER, KEY_ID_KONTAKT + " =? ", new String[]{Long.toString(inn_id)});
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

    public int finnAntallKontakter() {
        String sql= "SELECT * FROM " + TABLE_KONTAKTER;
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
}
