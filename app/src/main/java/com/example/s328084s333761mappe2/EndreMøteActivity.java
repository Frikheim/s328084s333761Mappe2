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
import java.util.List;

public class EndreMøteActivity extends AppCompatActivity implements DatePickerFragment.OnDialogDismissListener, TimePickerFragment.OnDialogDismissListener {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.endre_mote_layout);

        //får inn id til møte som skal endres
        Intent i = this.getIntent();
        Long motedId= i.getExtras().getLong("endremoteid");
        db = new DBHandler(this);
        db.getWritableDatabase();
        møte = db.finnMøte(motedId.intValue());
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

    public void endre(View v) {
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
        tidBoks.setText(prefs.getString(getString(R.string.velgTidspunkt),""));
        datoBoks.setText(prefs.getString(getString(R.string.velgDato),""));
    }
}