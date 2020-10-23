package com.example.s328084s333761mappe2;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

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
        navn= i.getExtras().getString("kontaktnavn");

        db = new DBHandler(this);
        db.getWritableDatabase();

        kontakt = db.finnKontakt(navn);
        EditText visNavn = findViewById(R.id.visNavn);
        EditText visTelefon = findViewById(R.id.visTelefon);
        visNavn.setText(kontakt.getNavn());
        visTelefon.setText(kontakt.getTelefon());
    }

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
                EditText visNavn = findViewById(R.id.visNavn);
                EditText visTelefon = findViewById(R.id.visTelefon);

                String navn = visNavn.getText().toString();
                String tlf = visTelefon.getText().toString();

                if(navn.equals("") || tlf.equals("")) {
                    Toast.makeText(getApplicationContext(),R.string.møteIkkeFyltUt, Toast.LENGTH_SHORT).show();
                }
                else {
                    //endre kontakt
                    Kontakt endretKontakt = new Kontakt(kontakt.get_ID(), navn, tlf);
                    db.oppdaterKontakt(endretKontakt);

                    finish();
                }
                break;
            case R.id.slettKontaktAction:
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
