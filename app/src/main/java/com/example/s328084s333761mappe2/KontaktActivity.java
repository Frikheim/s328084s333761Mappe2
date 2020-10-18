package com.example.s328084s333761mappe2;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class KontaktActivity extends AppCompatActivity implements KontaktListeFragment.KontaktEndret {

    DBHandler db;

    public void idKontaktEndret(String innhold) {
        if(findViewById(R.id.scriptfragmentkontakt) != null) {
            VisKontaktFragment visscript= (VisKontaktFragment) getSupportFragmentManager().findFragmentById(R.id.scriptfragmentkontakt);
            if(visscript== null) {
                visscript= new VisKontaktFragment();
                visscript.init(innhold);
                FragmentManager fm = getSupportFragmentManager();
                FragmentTransaction trans = fm.beginTransaction();
                trans.replace(R.id.listefragmentkontakt, visscript);
                trans.commit();
            }
            else{
                visscript.updateUrl(innhold);
            }
        } else {
            Intent i = new Intent(this, VisKontaktActivity.class);
            i.putExtra("kontaktnavn", innhold);
            startActivity(i);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.kontakt_activity_layout);
        db = new DBHandler(this);
        db.getWritableDatabase();


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
    protected void onPostResume() {
        super.onPostResume();
        KontaktListeFragment liste = (KontaktListeFragment) getSupportFragmentManager()
                .findFragmentById(R.id.listefragmentkontakt);
        liste.oppdater();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.kontakter:
                Intent ikontakter = new Intent(this, LeggTilKontaktActivity.class);
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
