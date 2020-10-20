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



}
