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
import android.widget.Toolbar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class EndreMøteActivity extends AppCompatActivity implements DatePickerFragment.OnDialogDismissListener, TimePickerFragment.OnDialogDismissListener {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.endre_mote_layout);
        setTitle(R.string.endreMøte);

        //får inn id til møte som skal endres
        Intent i = this.getIntent();
        String motedId= i.getExtras().getString("endremoteid");
        db = new DBHandler(this);
        db.getWritableDatabase();
        møte = db.finnMøte(Integer.parseInt(motedId));
        //gir feltene de originale verdiene
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

        final ArrayList<String> list = db.kontaktListe();
        Collections.sort(list);
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_multiple_choice, list);
        deltakerListe.setAdapter(adapter);
        deltakerListe.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String item = (String) deltakerListe.getItemAtPosition(i);
                Log.d("item", item);
                if(valgteDeltakere.contains(item)) {
                    valgteDeltakere.remove(item);
                }
                else {
                    valgteDeltakere.add(item);
                }
            }
        });

        prefs = PreferenceManager.getDefaultSharedPreferences(this);
    }

    EditText typeinn;
    EditText stedinn;
    EditText idinn;
    EditText tidinn;
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
    protected void onDestroy() {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(getString(R.string.velgDato),"");
        editor.putString(getString(R.string.velgTidspunkt),"");
        editor.apply();
        super.onDestroy();
    }

    public void endre() {
        dato = prefs.getString(getString(R.string.velgDato),"");
        tid = prefs.getString(getString(R.string.velgTidspunkt),"");
        String type = typeinn.getText().toString();
        String sted = stedinn.getText().toString();

        if(dato == "" || tid == "" || type == "" || sted == "" || valgteDeltakere.isEmpty()) {
            Toast.makeText(getApplicationContext(),R.string.møteIkkeFyltUt, Toast.LENGTH_SHORT).show();
        }
        else {
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString(getString(R.string.velgDato),"");
            editor.putString(getString(R.string.velgTidspunkt),"");
            editor.apply();
            //endre møte
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
            Møte endreMøte = new Møte(møte.get_ID(),type, sted, dato, tid);
            db.oppdaterMøte(endreMøte);
            List<MøteDeltakelse> gamleDeltakere = db.finnMøteDeltakelseIMøte(møte.get_ID());
            for (MøteDeltakelse deltaker : gamleDeltakere) {
                db.slettMøteDeltakelse(deltaker.get_ID());
            }
            for (String streng : valgteDeltakere) {
                Kontakt kontakt = db.finnKontakt(streng);

                MøteDeltakelse møteDeltakelse = new MøteDeltakelse(endreMøte.get_ID(),kontakt.get_ID());
                Log.d("navn",møteDeltakelse.getDeltaker_ID().toString());
                db.leggTilMøteDeltakelse(møteDeltakelse);
            }
            finish();
        }

        Log.d("Legg inn: ", "legger til møter");
    }


    public void visTidspunkt(View v) {
        DialogFragment newFragment = new TimePickerFragment();
        newFragment.show(getSupportFragmentManager(), "timePicker");
    }

    public void visDato(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }


    @Override
    public void onDialogDismissListener() {
        String tidspunkt = prefs.getString(getString(R.string.velgTidspunkt),"");
        String datoboks = prefs.getString(getString(R.string.velgDato),"");
        if(!tidspunkt.equals("")) {
            String[] splittetTid = tidspunkt.split(":");
            if (Integer.parseInt(splittetTid[0]) < 10 && Integer.parseInt(splittetTid[1]) < 10) {
                tidspunkt = "0" + splittetTid[0] + ":0" + splittetTid[1];
            } else if (Integer.parseInt(splittetTid[0]) < 10) {
                tidspunkt = "0" + splittetTid[0] + ":" + splittetTid[1];
            } else if (Integer.parseInt(splittetTid[1]) < 10) {
                tidspunkt = splittetTid[0] + ":0" + splittetTid[1];
            }
            tidBoks.setText(tidspunkt);
        }
        if(!datoboks.equals("")) {
            String[] splittetDato = datoboks.split("\\.");
            if (Integer.parseInt(splittetDato[0]) < 10 && Integer.parseInt(splittetDato[1]) < 10) {
                datoboks = "0" + splittetDato[0] + ".0" + splittetDato[1] + "." + splittetDato[2];
            } else if (Integer.parseInt(splittetDato[0]) < 10) {
                datoboks = "0" + splittetDato[0] + "." + splittetDato[1] + "." + splittetDato[2];
            } else if (Integer.parseInt(splittetDato[1]) < 10) {
                datoboks = splittetDato[0] + ".0" + splittetDato[1] + "." + splittetDato[2];
            }

            datoBoks.setText(datoboks);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.endremotemeny, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.endreMøteAction:
                endre();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }
}