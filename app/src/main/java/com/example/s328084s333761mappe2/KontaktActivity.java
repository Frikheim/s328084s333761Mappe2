package com.example.s328084s333761mappe2;

import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class KontaktActivity extends AppCompatActivity {
    EditText navninn;
    EditText telefoninn;
    EditText idinn;
    TextView utskrift;
    DBHandler db;



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
}
