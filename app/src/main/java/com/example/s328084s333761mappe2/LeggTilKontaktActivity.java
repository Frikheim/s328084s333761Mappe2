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
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class LeggTilKontaktActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.kontakt_layout);

        setTitle(R.string.leggTil);

        navninn = (EditText) findViewById(R.id.navninn);
        telefoninn = (EditText) findViewById(R.id.telefoninn);

        db = new DBHandler(this);
        db.getWritableDatabase();
    }

    EditText navninn;
    EditText telefoninn;
    DBHandler db;

    public void leggtil(View v) {
        Kontakt kontakt = new Kontakt(navninn.getText().toString(),telefoninn.getText().toString());
        db.leggTilKontakt(kontakt);
        Log.d("Legg inn: ", "legger til kontakter");
        finish();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.leggtilmeny, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.leggTilAction:
                EditText navninn = findViewById(R.id.navninn);
                EditText telefoninn = findViewById(R.id.telefoninn);

                String navn = navninn.getText().toString();
                String tlf = telefoninn.getText().toString();

                if(navn.equals("") || tlf.equals("")) {
                    Toast.makeText(getApplicationContext(),R.string.møteIkkeFyltUt, Toast.LENGTH_SHORT).show();
                }
                else {
                    Kontakt kontakt = new Kontakt(navninn.getText().toString(),telefoninn.getText().toString());
                    db.leggTilKontakt(kontakt);
                    Log.d("Legg inn: ", "legger til kontakter");
                    finish();
                }
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

}
