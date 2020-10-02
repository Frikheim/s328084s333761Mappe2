package com.example.s328084s333761mappe2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    EditText navninn;
    EditText telefoninn;
    EditText idinn;
    TextView utskrift;
    DBHandler db;

    //Åpner preferanser ativity
    public void visPreferanser(View v) {
        Intent ipreferanser = new Intent(this,SettPreferanser.class);
        startActivity(ipreferanser);
    }

    public void leggtil(View v) {
        Kontakt kontakt = new Kontakt(navninn.getText().toString(),telefoninn.getText().toString());
        db.leggTilKontakt(kontakt);
        Log.d("Legg inn: ", "legger til kontakter");
    }

    public void visalle(View v) {
        String tekst = "";
        List<Kontakt> kontakter = db.finnAlleKontakter();

        for (Kontakt kontakt : kontakter) {
            tekst = tekst + "Id: " + kontakt.get_ID() + ",Navn: " +
                    kontakt.getNavn() + " ,Telefon: " + kontakt.getTelefon();
            Log.d("Navn: ", tekst);
        }
        utskrift.setText(tekst);
    }

    public void slett(View v) {
        Long kontaktid= (Long.parseLong(idinn.getText().toString()));
        db.slettKontakt(kontaktid);
    }

    public void oppdater(View v) {
        Kontakt kontakt= new Kontakt();
        kontakt.setNavn(navninn.getText().toString());
        kontakt.setTelefon(telefoninn.getText().toString());
        kontakt.set_ID(Long.parseLong(idinn.getText().toString()));
        db.oppdaterKontakt(kontakt);
    }

    public void linkEndret(String link) {
        if(findViewById(R.id.scriptfragment) != null) {
            VisScriptFragment visscript= (VisScriptFragment) getSupportFragmentManager().findFragmentById(R.id.scriptfragment);
            if(visscript== null) {
                visscript= new VisScriptFragment();
                visscript.init(link);
                FragmentManager fm = getSupportFragmentManager();
                FragmentTransaction trans = fm.beginTransaction();
                trans.replace(R.id.listefragment, visscript);
                trans.commit();
            }
            else{
                visscript.updateUrl(link);
            }
        } else {
            Intent i = new Intent(this, VisScriptActivity.class);
            i.putExtra("scriptnavn", link);
            startActivity(i);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        navninn = (EditText) findViewById(R.id.navn);
        telefoninn = (EditText) findViewById(R.id.telefon);
        idinn = (EditText) findViewById(R.id.min_id);
        utskrift = (TextView) findViewById(R.id.utskrift);
        db = new DBHandler(this);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        //bruk shared pref til å velge mellom leggtil og endre kontakt
    }
}