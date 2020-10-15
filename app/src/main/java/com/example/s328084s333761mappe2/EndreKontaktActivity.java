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

public class EndreKontaktActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.endre_kontakt_layout);
        Toolbar myToolbar = (Toolbar)findViewById(R.id.moteToolbar);

        myToolbar.inflateMenu(R.menu.kontaktmeny);
        setActionBar(myToolbar);

        //får inn id til møte som skal endres
        Intent i = this.getIntent();
        String endrekontaktnavn = i.getExtras().getString("endrekontaktnavn");
        db = new DBHandler(this);
        db.getWritableDatabase();
        kontakt = db.finnKontakt(endrekontaktnavn);
        //gir feltene de originale verdiene
        navninn = findViewById(R.id.navninn);
        navninn.setText(kontakt.getNavn());
        id = kontakt.get_ID();
        tlfinn = findViewById(R.id.telefoninn);
        tlfinn.setText(kontakt.getTelefon());
    }

    EditText navninn;
    EditText tlfinn;
    Long id;

    DBHandler db;
    String dato;
    String tid;
    Kontakt kontakt;

    public void endre(View v) {
        String navn = navninn.getText().toString();
        String tlf = tlfinn.getText().toString();

        if(navn == "" || tlf == "") {
            Toast.makeText(getApplicationContext(),R.string.møteIkkeFyltUt, Toast.LENGTH_SHORT).show();
        }
        else {
            //endre kontakt
            Kontakt endretKontakt = new Kontakt(kontakt.get_ID(),navn,tlf);
            db.oppdaterKontakt(endretKontakt);
            finish();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.kontaktmeny, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.kontakter:
                Intent ikontakter = new Intent(this, LeggTilKontaktActivity.class);
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