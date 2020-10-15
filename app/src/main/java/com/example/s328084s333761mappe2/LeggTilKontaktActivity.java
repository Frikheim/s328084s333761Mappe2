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
        Toolbar myToolbar = (Toolbar)findViewById(R.id.kontaktToolbar);

        myToolbar.inflateMenu(R.menu.kontaktmeny);
        setActionBar(myToolbar);

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
            case R.id.møter:
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
