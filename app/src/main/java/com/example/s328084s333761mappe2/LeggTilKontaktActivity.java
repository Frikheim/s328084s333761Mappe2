package com.example.s328084s333761mappe2;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class LeggTilKontaktActivity extends AppCompatActivity {
    EditText navninn;
    EditText telefoninn;
    DBHandler db;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.kontakt_layout);

        setTitle(R.string.leggTilKontakt);

        //Finner EditTextViewene til layouten
        navninn = findViewById(R.id.navninn);
        telefoninn = findViewById(R.id.telefoninn);

        db = new DBHandler(this);
        db.getWritableDatabase();
    }

    public void leggtil() {
        String navn = navninn.getText().toString();
        String tlf = telefoninn.getText().toString();
        if(navn.equals("") || tlf.equals("")) { //Sjekker om alle feltene er fylt inn, hvis ikke får brukeren beskjed om dette
            Toast.makeText(getApplicationContext(),R.string.møteIkkeFyltUt, Toast.LENGTH_SHORT).show();
        }
        else {
            //Oppretter et nytt kontakt-objekt og legger dette til i databasen
            Kontakt kontakt = new Kontakt(navninn.getText().toString(),telefoninn.getText().toString());
            db.leggTilKontakt(kontakt);
            finish();
        }
    }

    //Legger til lagre-ikon i actionbaren
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.leggtilmeny, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.leggTilAction) {
            //Dersom det trykkes på lagre-ikonet kjøres leggtil-funksjonen
            leggtil();
        } else {
            return super.onOptionsItemSelected(item);
        }
        return true;
    }

}
