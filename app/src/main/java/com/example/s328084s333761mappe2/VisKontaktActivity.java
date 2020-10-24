package com.example.s328084s333761mappe2;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class VisKontaktActivity extends AppCompatActivity {
    String navn;
    Kontakt kontakt;
    EditText visNavn;
    EditText visTelefon;
    DBHandler db;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.viskontakt_layout);
        setTitle(R.string.kontakter);
        Intent i = this.getIntent();
        navn = i.getExtras().getString("kontaktnavn");

        db = new DBHandler(this);
        db.getWritableDatabase();
        //Finner kontakten basert på navnet vi får fra intentet over
        kontakt = db.finnKontakt(navn);
        visNavn = findViewById(R.id.visNavn);
        visTelefon = findViewById(R.id.visTelefon);
        //Fyller inn EditTextViewene med informasjonen om kontakten
        visNavn.setText(kontakt.getNavn());
        visTelefon.setText(kontakt.getTelefon());
    }

    public void endreKontakt() {
        visNavn = findViewById(R.id.visNavn);
        visTelefon = findViewById(R.id.visTelefon);
        String navn = visNavn.getText().toString();
        String tlf = visTelefon.getText().toString();

        //Sjekker om alle feltene er fylt ut, hvis ikke får bruker beskjed om dette
        if(navn.equals("") || tlf.equals("")) {
            Toast.makeText(getApplicationContext(),R.string.møteIkkeFyltUt, Toast.LENGTH_SHORT).show();
        }
        else {
            //Oppretter et endret kontakt-objekt og oppdaterer databasen med dette objektet
            Kontakt endretKontakt = new Kontakt(kontakt.get_ID(), navn, tlf);
            db.oppdaterKontakt(endretKontakt);
            finish();
        }
    }

    //Legger til lagre- og slette-ikon i actionbaren
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.endrekontaktmeny, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.endreKontaktAction:
                //Hvis bruker trykker på lagre-ikonet kjøres endreKontakt-funksjonen
                endreKontakt();
                break;
            case R.id.slettKontaktAction:
                //Hvis bruker trykker på slett-ikonet slettes kontakten fra databasen
                DBHandler db = new DBHandler(this);
                db.getWritableDatabase();
                db.slettKontakt(kontakt.get_ID());
                finish();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }
}
