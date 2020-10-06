package com.example.s328084s333761mappe2;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toolbar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class MøteActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mote_layout);
        Toolbar myToolbar = (Toolbar)findViewById(R.id.moteToolbar);

        myToolbar.inflateMenu(R.menu.motemeny);
        setActionBar(myToolbar);

        typeinn = (EditText) findViewById(R.id.typeinn);
        stedinn = (EditText) findViewById(R.id.stedinn);
        tidinn = (EditText) findViewById(R.id.tidinn);
        idinn = (EditText) findViewById(R.id.idinn);
        utskrift = (TextView) findViewById(R.id.utskrift);

        db = new DBHandler(this);
        db.getWritableDatabase();
    }

    EditText typeinn;
    EditText stedinn;
    EditText idinn;
    EditText tidinn;
    TextView utskrift;
    DBHandler db;

    public void leggtil(View v) {
        Møte møte = new Møte(typeinn.getText().toString(),stedinn.getText().toString(),tidinn.getText().toString());
        //Fikse møtedeltakelse
        db.leggTilMøte(møte);
        Log.d("Legg inn: ", "legger til møter");
    }

    public void visalle(View v) {
        String tekst = "";
        List<Møte> møter = db.finnAlleMøter();

        for (Møte møte : møter) {
            tekst = tekst + "Id: " + møte.get_ID() + ", Type: " +
                    møte.getType() + " , Sted: " + møte.getSted() + ", Tid: " + møte.getTidspunkt();
            Log.d("Møte: ", tekst);
        }
        utskrift.setText(tekst);
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
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.kontakter:
                Intent ikontakter = new Intent(this,KontaktActivity.class);
                startActivity(ikontakter);
                finish();
                break;
            case R.id.preferanser:
                Intent ipreferanser = new Intent(this,SettPreferanser.class);
                startActivity(ipreferanser);
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }
}
