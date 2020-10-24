package com.example.s328084s333761mappe2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class EndreMøteActivity extends AppCompatActivity implements DatePickerFragment.OnDialogDismissListener, TimePickerFragment.OnDialogDismissListener {
    EditText typeinn;
    EditText stedinn;
    TextView datoBoks;
    TextView tidBoks;
    ListView deltakerListe;
    List<String> valgteDeltakere = new ArrayList<>();

    DBHandler db;
    String dato;
    String tid;
    Møte møte;
    SharedPreferences prefs;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.endre_mote_layout);
        setTitle(R.string.endreMøte);

        //Får inn ID til møte som skal endres
        Intent i = this.getIntent();
        String motedId= i.getExtras().getString("endremoteid");
        db = new DBHandler(this);
        db.getWritableDatabase();
        møte = db.finnMøte(Integer.parseInt(motedId));
        //Gir feltene de originale verdiene
        typeinn = findViewById(R.id.typeinn);
        typeinn.setText(møte.getType());
        stedinn = findViewById(R.id.stedinn);
        stedinn.setText(møte.getSted());
        datoBoks = findViewById(R.id.datoBoks);
        datoBoks.setText(møte.getDato());
        tidBoks = findViewById(R.id.tidBoks);
        tidBoks.setText(møte.getTidspunkt());

        deltakerListe = findViewById(R.id.leggTilDeltakerListe);
        deltakerListe.setItemsCanFocus(false);

        //Legger til alle kontakter i deltakerlisten
        final ArrayList<String> list = db.kontaktListe();
        Collections.sort(list); //Sorterer deltakerlisten alfabetisk
        final ArrayAdapter<String> adapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_multiple_choice, list);
        deltakerListe.setAdapter(adapter);
        deltakerListe.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String item = (String) deltakerListe.getItemAtPosition(i);
                if(valgteDeltakere.contains(item)) { //Sjekker om deltakeren brukeren trykker på allerde er trykket på
                    valgteDeltakere.remove(item);    //og dermed skal fjernes fra valgteDeltakere-listen
                }
                else {
                    valgteDeltakere.add(item);
                }
            }
        });

        prefs = PreferenceManager.getDefaultSharedPreferences(this);
    }



    @Override
    protected void onDestroy() {
        //Når brukeren går ut av endresiden skal velgDato- og velgTidspunkt-variablene nullstilles
        //slik at disse ikke blir satt når en bruker skal endre et annet møte
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(getString(R.string.velgDato),"");
        editor.putString(getString(R.string.velgTidspunkt),"");
        editor.apply();
        super.onDestroy();
    }

    public void endre() {
        //Henter inn dato, tid, type og sted brukeren har lagt inn
        dato = prefs.getString(getString(R.string.velgDato),"");
        tid = prefs.getString(getString(R.string.velgTidspunkt),"");
        String type = typeinn.getText().toString();
        String sted = stedinn.getText().toString();

        //Hvis noen av feltene ikke er fylt ut får brukeren tilbakemelding om at alle feltene må fylles ut
        if(dato == "" || tid == "" || type == "" || sted == "" || valgteDeltakere.isEmpty()) {
            Toast.makeText(getApplicationContext(),R.string.møteIkkeFyltUt, Toast.LENGTH_SHORT).show();
        }
        else {
            SharedPreferences.Editor editor = prefs.edit();
            //Legger inn dato- og tid-variablene brukeren har valgt inn i SharedPreferences
            editor.putString(getString(R.string.velgDato),"");
            editor.putString(getString(R.string.velgTidspunkt),"");
            editor.apply();

            //Formaterer tid og dato på formen mm:tt og dd.mm.yyyy henholdsvis
            String[] splittetTid = tid.split(":");
            if(Integer.parseInt(splittetTid[0]) < 10 && Integer.parseInt(splittetTid[1]) < 10) {
                tid = "0" + splittetTid[0] +":0" + splittetTid[1];
            }
            else if(Integer.parseInt(splittetTid[0]) < 10) {
                tid = "0" + splittetTid[0] +":" + splittetTid[1];
            }
            else if(Integer.parseInt(splittetTid[1]) < 10) {
                tid = splittetTid[0] +":0" + splittetTid[1];
            }

            String[] splittetDato = dato.split("\\.");
            if(Integer.parseInt(splittetDato[0]) < 10 && Integer.parseInt(splittetDato[1]) < 10) {
                dato = "0" + splittetDato[0] + ".0" + splittetDato[1] + "." + splittetDato[2];
            }
            else if(Integer.parseInt(splittetDato[0]) < 10) {
                dato = "0" + splittetDato[0] +"." + splittetDato[1] + "." + splittetDato[2];
            }
            else if(Integer.parseInt(splittetDato[1]) < 10) {
                dato = splittetDato[0] + ".0" + splittetDato[1] + "." + splittetDato[2];
            }

            //Oppretter et endret møte-objekt og oppdaterer dette i databasen
            Møte endreMøte = new Møte(møte.get_ID(),type, sted, dato, tid);
            db.oppdaterMøte(endreMøte);

            //Finner de tidligere møtedeltakerne i møtet og sletter møtedeltakelsen i databasen
            List<MøteDeltakelse> gamleDeltakere = db.finnMøteDeltakelseIMøte(møte.get_ID());
            for (MøteDeltakelse deltaker : gamleDeltakere) {
                db.slettMøteDeltakelse(deltaker.get_ID());
            }

            //Legger inn de nye møtedeltakelsene i databasen
            for (String streng : valgteDeltakere) {
                Kontakt kontakt = db.finnKontakt(streng);
                MøteDeltakelse møteDeltakelse = new MøteDeltakelse(endreMøte.get_ID(),kontakt.get_ID());
                db.leggTilMøteDeltakelse(møteDeltakelse);
            }
            finish();
        }

    }

    //Åpner TimePicker-fragmentet
    public void visTidspunkt(View v) {
        DialogFragment newFragment = new TimePickerFragment();
        newFragment.show(getSupportFragmentManager(), "timePicker");
    }

    //Åpner DatePicker-fragmentet
    public void visDato(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }


    @Override
    public void onDialogDismissListener() {
        //Henter inn tid og dato fra SharedPreferences
        String tidspunkt = prefs.getString(getString(R.string.velgTidspunkt),"");
        String datoboks = prefs.getString(getString(R.string.velgDato),"");
        if(!tidspunkt.equals("")) {
            //Formaterer tid på formen mm:tt
            String[] splittetTid = tidspunkt.split(":");
            if (Integer.parseInt(splittetTid[0]) < 10 && Integer.parseInt(splittetTid[1]) < 10) {
                tidspunkt = "0" + splittetTid[0] + ":0" + splittetTid[1];
            } else if (Integer.parseInt(splittetTid[0]) < 10) {
                tidspunkt = "0" + splittetTid[0] + ":" + splittetTid[1];
            } else if (Integer.parseInt(splittetTid[1]) < 10) {
                tidspunkt = splittetTid[0] + ":0" + splittetTid[1];
            }
            //Setter den valgte tiden inn i et TextView
            tidBoks.setText(tidspunkt);
        }
        if(!datoboks.equals("")) {
            //Formaterer dato på formen dd.mm.yyyy
            String[] splittetDato = datoboks.split("\\.");
            if (Integer.parseInt(splittetDato[0]) < 10 && Integer.parseInt(splittetDato[1]) < 10) {
                datoboks = "0" + splittetDato[0] + ".0" + splittetDato[1] + "." + splittetDato[2];
            } else if (Integer.parseInt(splittetDato[0]) < 10) {
                datoboks = "0" + splittetDato[0] + "." + splittetDato[1] + "." + splittetDato[2];
            } else if (Integer.parseInt(splittetDato[1]) < 10) {
                datoboks = splittetDato[0] + ".0" + splittetDato[1] + "." + splittetDato[2];
            }
            //Setter den valgte datoen inn i et TextView
            datoBoks.setText(datoboks);
        }
    }

    //Legger til ikon for lagre i actionbaren
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.endremotemeny, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.endreMøteAction) {
            //Når det trykkes på lagre-ikonet skal endre-funksjonen kjøres
            endre();
        } else {
            return super.onOptionsItemSelected(item);
        }
        return true;
    }
}