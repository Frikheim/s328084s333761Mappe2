package com.example.s328084s333761mappe2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

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
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toolbar;

public class MainActivity extends AppCompatActivity implements ListeFragment.MøteEndret {

    DBHandler db;

    public void idEndret(String innhold) {
        String[] splittet = innhold.split(":");
        String id = splittet[0];
        if(findViewById(R.id.scriptfragment) != null) {
            VisMoteFragment visscript= (VisMoteFragment) getSupportFragmentManager().findFragmentById(R.id.scriptfragment);
            if(visscript== null) {
                visscript= new VisMoteFragment();
                visscript.init(id);
                FragmentManager fm = getSupportFragmentManager();
                FragmentTransaction trans = fm.beginTransaction();
                trans.replace(R.id.listefragment, visscript);
                trans.commit();
            }
            else{
                visscript.updateUrl(id);
            }
        } else {
            Intent i = new Intent(this, VisMoteActivity.class);
            i.putExtra("moteid", id);
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

        startService();
    }

    public void startService() {
        Intent intent = new Intent();
        intent.setAction(getString(R.string.broadcast));
        sendBroadcast(intent);
    }

    public void stoppPeriodisk() {
        Intent i = new Intent(this, MinService.class);
        PendingIntent pintent = PendingIntent.getService(this, 0, i, 0);
        AlarmManager alarm = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        if (alarm!= null) {
            alarm.cancel(pintent);
        }
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
        ListeFragment liste = (ListeFragment) getSupportFragmentManager()
                .findFragmentById(R.id.listefragment);
        liste.oppdater();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.kontakter:
                Intent ikontakter = new Intent(this, KontaktActivity.class);
                startActivity(ikontakter);
                break;
            case R.id.preferanser:
                Intent ipreferanser = new Intent(this,SettPreferanser.class);
                startActivity(ipreferanser);
                break;
            case R.id.leggTilMote:
                Intent inyttMote = new Intent(this,MøteActivity.class);
                startActivity(inyttMote);
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

}