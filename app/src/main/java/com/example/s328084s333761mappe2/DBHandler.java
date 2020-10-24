package com.example.s328084s333761mappe2;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
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
        //Oppretter tabellene i databasen
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

    //Dersom databaseversjonen endrer seg kjøres onUpgrade og vi sletter gamle tabeller og oppretter nye i onCreate
    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MOTE_DELTAKELSE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_KONTAKTER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MOTER);

        onCreate(db);
    }

    //ContentProvider

    //Metode for ContentProvider til å hente én kontakt
    public Cursor contentProviderHentEn(Uri uri, String[] projection, String[] selectionArgs, String sortOrder) {
        Cursor cur;
        SQLiteDatabase db = this.getWritableDatabase();
        cur= db.query(TABLE_KONTAKTER, projection, KEY_ID_KONTAKT + "=" + uri.getPathSegments().get(1), selectionArgs, null, null, sortOrder);
        return cur;
    }

    //Metode for ContentProvider til å hente alle kontakter
    public Cursor contentProviderHentAlle() {
        Cursor cur;
        SQLiteDatabase db = this.getWritableDatabase();
        cur= db.query(TABLE_KONTAKTER, null, null, null, null, null, null);
        return cur;
    }

    //Metode for ContentProvider til å legge til en kontakt
    public Cursor contentProviderLeggTil(ContentValues values) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.insert(TABLE_KONTAKTER, null, values);
        Cursor c = db.query(TABLE_KONTAKTER, null, null, null, null, null, null);
        return c;
    }

    //Metode som finner tar inn en liste med MøteDeltakelse og returnerer en liste med navnene til alle deltakerne
    public ArrayList<String> deltakerListe(List<MøteDeltakelse> deltakere) {
        ArrayList<String> deltakerListe = new ArrayList<>();

        for (MøteDeltakelse møteDeltakelse: deltakere ) {
            Kontakt kontakt = finnKontakt(møteDeltakelse.getDeltaker_ID().intValue());
            deltakerListe.add(kontakt.getNavn());
        }
        return deltakerListe;
    }

    //Metode som finner alle kontakter og returnerer en liste med alle kontakter
    public ArrayList<String> kontaktListe() {
        ArrayList<String> kontaktListe = new ArrayList<String>();
        List<Kontakt> kontakter = finnAlleKontakter();
        for (Kontakt kontakt : kontakter) {
            kontaktListe.add(kontakt.getNavn());
        }
        return kontaktListe;
    }

    //Metode som legger til et møte i databasen
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

    //Metode som legger til en møtedeltakelse i databasen
    public void leggTilMøteDeltakelse (MøteDeltakelse møteDeltakelse) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_ID_MOTE_ID, møteDeltakelse.getMøte_ID());
        values.put(KEY_ID_DELTAKER, møteDeltakelse.getDeltaker_ID());
        db.insert(TABLE_MOTE_DELTAKELSE,null,values);
        db.close();
    }

    //Metode som legger til en kontakt i databasen
    public void leggTilKontakt(Kontakt kontakt) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, kontakt.getNavn());
        values.put(KEY_PH_NO, kontakt.getTelefon());
        db.insert(TABLE_KONTAKTER,null,values);
        db.close();
    }

    //Metode som finner alle kontakter i databasen og returnerer en liste med kontakt-objekter
    public ArrayList<Kontakt> finnAlleKontakter() {
        ArrayList<Kontakt> kontaktListe = new ArrayList<Kontakt>();
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

    //Metode som finner alle møter i databasen og returnerer en liste med møte-objekter
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

    //Metode som tar inn en møte-ID, finner alle møtedeltakelsene i møtet og returnerer
    //en liste med møtedeltakelse-objekter
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

    //Metode for å slette en kontakt fra databasen
    public void slettKontakt(Long inn_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_KONTAKTER, KEY_ID_KONTAKT + " =? ", new String[]{Long.toString(inn_id)});
        db.close();
    }

    //Metode for å slette et møte fra databasen
    public void slettMøte(Long inn_id) {
        List<MøteDeltakelse> deltakere = finnMøteDeltakelseIMøte(inn_id);
        for (MøteDeltakelse deltaker: deltakere) {
            slettMøteDeltakelse(deltaker.get_ID());
        }
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_MOTER, KEY_ID_MOTE + " =? ", new String[]{Long.toString(inn_id)});
        db.close();
    }

    //Metode for å slette en møtedeltakelse fra databasen
    public void slettMøteDeltakelse(Long inn_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_MOTE_DELTAKELSE, KEY_ID_MOTEDELTAKELSE + " =? ", new String[]{Long.toString(inn_id)});
        db.close();
    }

    //Metode som tar inn et endret kontakt-objekt og oppdaterer kontakt-objektet i databasen
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

    //Metode som tar inn et endret møte-objekt og oppdaterer møte-objektet i databasen
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

    //Metode som tar inn en ID og finner et evt kontakt-objekt i databasen som har denne ID-en
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

    //Metode som tar inn et navn og finner et evt kontakt-objekt i databasen som har dette navnet
    //(dette medfører at en bruker ikke kan legge inn to kontakter med identisk navn)
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

    //Metode som tar inn en ID og finner et evt møte-objekt i databasen som har denne ID-en
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

    //Metode som tar inn type, sted, dato og tidspunkt og finner et evt møte-objekt
    //i databasen som har disse verdiene
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

    //Metode som tar inn en dato, finner alle møter som finner sted denne datoen og
    //returnerer en liste med møte-objekter
    public ArrayList<Møte> finnAlleMøterIDag(String dato) {
        ArrayList<Møte> møteListe = new ArrayList<Møte>();
        ArrayList<Møte> alleMøter = sorterMøter(finnAlleMøter());
        for (Møte møte : alleMøter) {
            if(møte.getDato().equals(dato)) {
                møteListe.add(møte);
            }
        }
        return møteListe;
    }

    //Metode som tar inn en liste med møte-objekter og sorterer disse på dato og tid
    public ArrayList<Møte> sorterMøter(ArrayList<Møte> møter) {
        ArrayList<Møte> sortertListe = new ArrayList(); //oppretter en ny liste med møte-objekter
        sortertListe.add(møter.get(0)); //legger til det første møtet i innparameter-listen i det sorterte arrayet
        boolean leter; //boolean som sier om vi leter etter riktig plass i sortertListe eller om vi har funnet riktig plass
        for (int i = 1; i < møter.size(); i++) {
            leter = true;
            String dato = møter.get(i).getDato();
            //Splitter opp datoen for å lage egne int-variabler for dag, måned og år som vi kan sammenligne
            String[] splittet = dato.split("\\.");
            int dag = Integer.parseInt(splittet[0]);
            int måned = Integer.parseInt(splittet[1]);
            int år = Integer.parseInt(splittet[2]);
            int plassering = 0; //plasseringen til det originale møtet i den sorterte listen
            while(leter) {
                String datoSortert = sortertListe.get(plassering).getDato();
                //Splitter opp datoen for å lage egne int-variabler for dag, måned og år som vi kan sammenligne
                String[] splittetSortert = datoSortert.split("\\.");
                int dagSortert = Integer.parseInt(splittetSortert[0]);
                int månedSortert = Integer.parseInt(splittetSortert[1]);
                int årSortert = Integer.parseInt(splittetSortert[2]);
                if(årSortert == år) { //Hvis begge møtene er i samme år må vi se videre på måneden
                    if(månedSortert == måned) { //Hvis begge møtene er i samme måned må vi se videre må dagen
                        if(dagSortert == dag) { //Hvis begge møtene er på samme dag må vi se videre på tiden
                            String tid = møter.get(i).getTidspunkt();
                            //Splitter opp tiden for å lage egne int-variabler for time og minutter som vi kan sammenligne
                            String tidSortert = sortertListe.get(plassering).getTidspunkt();
                            String[] splittetTid = tid.split(":");
                            String[] splittetTidSortert = tidSortert.split(":");
                            int time = Integer.parseInt(splittetTid[0]);
                            int timeSortert = Integer.parseInt(splittetTidSortert[0]);
                            int minutt = Integer.parseInt(splittetTid[1]);
                            int minuttSortert = Integer.parseInt(splittetTidSortert[1]);

                            if(timeSortert == time) { //Hvis begge møtene finner sted i samme time må vi se videre på minuttene
                                if(minuttSortert == minutt) {
                                    //Hvis begge møtene finner sted på nøyaktig samtidig legger vi til originalmøtet bakerst i den sorterte listen
                                    sortertListe.add(møter.get(i));
                                    leter = false;
                                }
                                else if(minuttSortert < minutt) {
                                    //Hvis originalmøtet sin minutt-verdi er høyere enn møtet vi sammenligner med i den sorterte listen
                                    //så finner originalmøtet sted etter møtet vi sammenligner med, og originalmøtet skal derfor legges
                                    // inn bak møtet i den sorterte listen. Øker derfor plassering-variabelen med en
                                    plassering++;
                                    if(plassering == sortertListe.size()) {
                                        //Hvis plassering er like stor som antall elementer i den sorterte listen
                                        //betyr det at vi er på slutten av den sorterte listen og det er ikke flere
                                        //møter å sammenligne med. Legger derfor originalmøtet bakerst i den sorterte listen
                                        sortertListe.add(møter.get(i));
                                        leter = false;
                                    }
                                }
                                else {
                                    //Originalmøtet sin minutt-verdi er lavere enn møtet vi sammenligner med i den sorterte listen
                                    //som betyr at originalmøtet finner sted før møtet vi sammenligner med. Originalmøtet legges derfor
                                    //inn på plassen foran møtet vi sammenligner med
                                    sortertListe.add(plassering,møter.get(i));
                                    leter = false;
                                }
                            }
                            else if(timeSortert < time) {
                                //Hvis originalmøtet sin time-verdi er høyere enn møtet vi sammenligner med i den sorterte listen
                                //så finner originalmøtet sted etter møtet vi sammenligner med, og originalmøtet skal derfor legges
                                // inn bak møtet i den sorterte listen. Øker derfor plassering-variabelen med en
                                plassering++;
                                if(plassering == sortertListe.size()) {
                                    sortertListe.add(møter.get(i));
                                    leter = false;
                                }
                            }
                            else {
                                //Originalmøtet sin time-verdi er lavere enn møtet vi sammenligner med i den sorterte listen
                                //som betyr at originalmøtet finner sted før møtet vi sammenligner med. Originalmøtet legges derfor
                                //inn på plassen foran møtet vi sammenligner med
                                sortertListe.add(plassering,møter.get(i));
                                leter = false;
                            }
                        }
                        else if(dagSortert < dag) {
                            //Hvis originalmøtet sin dag-verdi er høyere enn møtet vi sammenligner med i den sorterte listen
                            //så finner originalmøtet sted etter møtet vi sammenligner med, og originalmøtet skal derfor legges
                            // inn bak møtet i den sorterte listen. Øker derfor plassering-variabelen med en
                            plassering++;
                            if(plassering == sortertListe.size()) {
                                sortertListe.add(møter.get(i));
                                leter = false;
                            }
                        }
                        else {
                            //Originalmøtet sin dag-verdi er lavere enn møtet vi sammenligner med i den sorterte listen
                            //som betyr at originalmøtet finner sted før møtet vi sammenligner med. Originalmøtet legges derfor
                            //inn på plassen foran møtet vi sammenligner med
                            sortertListe.add(plassering,møter.get(i));
                            leter = false;
                        }
                    }
                    else if(månedSortert < måned) {
                        //Hvis originalmøtet sin måned-verdi er høyere enn møtet vi sammenligner med i den sorterte listen
                        //så finner originalmøtet sted etter møtet vi sammenligner med, og originalmøtet skal derfor legges
                        // inn bak møtet i den sorterte listen. Øker derfor plassering-variabelen med en
                        plassering++;
                        if(plassering == sortertListe.size()) {
                            sortertListe.add(møter.get(i));
                            leter = false;
                        }
                    }
                    else {
                        //Originalmøtet sin måned-verdi er lavere enn møtet vi sammenligner med i den sorterte listen
                        //som betyr at originalmøtet finner sted før møtet vi sammenligner med. Originalmøtet legges derfor
                        //inn på plassen foran møtet vi sammenligner med
                        sortertListe.add(plassering,møter.get(i));
                        leter = false;
                    }
                }
                else if(årSortert < år) {
                    //Hvis originalmøtet sin år-verdi er høyere enn møtet vi sammenligner med i den sorterte listen
                    //så finner originalmøtet sted etter møtet vi sammenligner med, og originalmøtet skal derfor legges
                    // inn bak møtet i den sorterte listen. Øker derfor plassering-variabelen med en
                    plassering++;
                    if(plassering == sortertListe.size()) {
                        sortertListe.add(møter.get(i));
                        leter = false;
                    }
                }
                else {
                    //Originalmøtet sin år-verdi er lavere enn møtet vi sammenligner med i den sorterte listen
                    //som betyr at originalmøtet finner sted før møtet vi sammenligner med. Originalmøtet legges derfor
                    //inn på plassen foran møtet vi sammenligner med
                    sortertListe.add(plassering,møter.get(i));
                    leter = false;
                }
            }
        }
        return sortertListe;
    }
}
