package com.example.s328084s333761mappe2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

public class MainActivity extends AppCompatActivity implements ListeFragment.UrlEndret{

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
        db = new DBHandler(this);
        db.getWritableDatabase();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        //bruk shared pref til å velge mellom leggtil og endre kontakt

        Toolbar myToolbar = (Toolbar)findViewById(R.id.minToolbar);

        myToolbar.inflateMenu(R.menu.hovedmeny);
        setActionBar(myToolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.hovedmeny, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.kontakter:
                Intent ikontakter = new Intent(this,KontaktActivity.class);
                startActivity(ikontakter);
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