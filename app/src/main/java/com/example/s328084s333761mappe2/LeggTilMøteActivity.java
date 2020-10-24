package com.example.s328084s333761mappe2;

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

public class LeggTilMøteActivity extends AppCompatActivity implements DatePickerFragment.OnDialogDismissListener, TimePickerFragment.OnDialogDismissListener {
    EditText typeinn;
    EditText stedinn;
    TextView datoBoks;
    TextView tidBoks;
    ListView deltakerListe;
    List<String> valgteDeltakere = new ArrayList<>();

    DBHandler db;
    String dato;
    String tid;
    SharedPreferences prefs;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mote_layout);

        setTitle(R.string.leggTilMøte);

        //Finner Viewene vi trenger i layouten
        typeinn = findViewById(R.id.typeinn);
        stedinn = findViewById(R.id.stedinn);
        datoBoks = findViewById(R.id.datoBoks);
        tidBoks = findViewById(R.id.tidBoks);
        deltakerListe = findViewById(R.id.leggTilDeltakerListe);
        deltakerListe.setItemsCanFocus(false);
        db = new DBHandler(this);
        db.getWritableDatabase();

        //Oppretter en liste med navnene til alle kontaktene i databasen
        final ArrayList<String> list = db.kontaktListe();
        Collections.sort(list); //Sorterer kontaktene alfabetisk
        final ArrayAdapter<String> adapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_multiple_choice, list);
        deltakerListe.setAdapter(adapter);
        deltakerListe.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String item = (String) deltakerListe.getItemAtPosition(i);
                if(valgteDeltakere.contains(item)) {//Sjekker om deltakeren brukeren trykker på allerde er trykket på
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

    public void leggtil() {
        //Henter inn dato, tid, type og sted som strenger
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

            //Oppretter et nytt møte-objekt og legger inn dette i databasen
            Møte møte = new Møte(type, sted, dato, tid);
            db.leggTilMøte(møte);

            //Finner møtet vi nettopp lagret i databasen
            Møte lagretMøte = db.finnMøte(møte.getType(),møte.getSted(),møte.getDato(),møte.getTidspunkt());
            //Går gjennom valgteDeltakere-listen og finner en og en kontakt og legger inn denne møtedeltakelsen
            //i databasen
            for (String streng : valgteDeltakere) {
                Kontakt kontakt = db.finnKontakt(streng);
                MøteDeltakelse møteDeltakelse = new MøteDeltakelse(lagretMøte.get_ID(),kontakt.get_ID());
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
        }
        //Legger inn valgt tid og dato i TextViews
        tidBoks.setText(tidspunkt);
        datoBoks.setText(datoboks);
    }

    //Legger til ikon for lagre i actionbaren
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.leggtilmeny, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.leggTilAction) {
            //Når brukeren trykker på lagre-ikonet kjøres leggtil-funksjonen
            leggtil();
        } else {
            return super.onOptionsItemSelected(item);
        }
        return true;
    }
}
