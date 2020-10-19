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

public class MøteActivity extends AppCompatActivity implements DatePickerFragment.OnDialogDismissListener, TimePickerFragment.OnDialogDismissListener {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mote_layout);

        setTitle(R.string.leggTilMøte);

        typeinn = findViewById(R.id.typeinn);
        stedinn = findViewById(R.id.stedinn);
        datoBoks = findViewById(R.id.datoBoks);
        tidBoks = findViewById(R.id.tidBoks);
        deltakerListe = findViewById(R.id.leggTilDeltakerListe);
        deltakerListe.setItemsCanFocus(false);
        db = new DBHandler(this);
        db.getWritableDatabase();

        final ArrayList<String> list = db.kontaktListe();
        final ArrayAdapter<String> adapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_multiple_choice, list);
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
    SharedPreferences prefs;

    @Override
    protected void onDestroy() {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(getString(R.string.velgDato),"");
        editor.putString(getString(R.string.velgTidspunkt),"");
        editor.apply();
        super.onDestroy();
    }

    public void leggtil(View v) {
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
            String[] splittet = tid.split(":");
            if(Integer.parseInt(splittet[0]) < 10 && Integer.parseInt(splittet[1]) < 10) {
                tid = "0" + splittet[0] +":0" + splittet[1];
            }
            else if(Integer.parseInt(splittet[0]) < 10) {
                tid = "0" + splittet[0] +":" + splittet[1];
            }
            else if(Integer.parseInt(splittet[1]) < 10) {
                tid = splittet[0] +":0" + splittet[1];
            }
            Møte møte = new Møte(type, sted, dato, tid);
            db.leggTilMøte(møte);

            List<Møte> møter = db.finnAlleMøter();
            Møte lagretMøte = db.finnMøte(møte.getType(),møte.getSted(),møte.getDato(),møte.getTidspunkt());
            for (String streng : valgteDeltakere) {
                Kontakt kontakt = db.finnKontakt(streng);

                MøteDeltakelse møteDeltakelse = new MøteDeltakelse(lagretMøte.get_ID(),kontakt.get_ID());
                Log.d("navn",møteDeltakelse.getDeltaker_ID().toString());
                db.leggTilMøteDeltakelse(møteDeltakelse);
            }

            finish();
        }

    }


    public void visTidspunkt(View v) {
        DialogFragment newFragment = new TimePickerFragment();
        newFragment.show(getSupportFragmentManager(), "timePicker");
    }

    public void visDato(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    public void slett(View v) {
        Long møteid= (Long.parseLong(idinn.getText().toString()));
        db.slettMøte(møteid);
    }

    public void oppdater(View v) {
        Møte møte= new Møte();
        møte.setType(typeinn.getText().toString());
        møte.setSted(stedinn.getText().toString());
        møte.setTidspunkt(tidinn.getText().toString());
        møte.set_ID(Long.parseLong(idinn.getText().toString()));
        db.oppdaterMøte(møte);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.motemeny, menu);
        return true;
    }


    @Override
    public void onDialogDismissListener() {
        String tidspunkt = prefs.getString(getString(R.string.velgTidspunkt),"");
        if(!tidspunkt.equals("")) {
            String[] splittet = tidspunkt.split(":");
            if (Integer.parseInt(splittet[0]) < 10 && Integer.parseInt(splittet[1]) < 10) {
                tidspunkt = "0" + splittet[0] + ":0" + splittet[1];
            } else if (Integer.parseInt(splittet[0]) < 10) {
                tidspunkt = "0" + splittet[0] + ":" + splittet[1];
            } else if (Integer.parseInt(splittet[1]) < 10) {
                tidspunkt = splittet[0] + ":0" + splittet[1];
            }
        }
        tidBoks.setText(tidspunkt);
        datoBoks.setText(prefs.getString(getString(R.string.velgDato),""));
    }
}
