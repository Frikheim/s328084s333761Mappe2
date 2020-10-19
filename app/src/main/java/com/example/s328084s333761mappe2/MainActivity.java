package com.example.s328084s333761mappe2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toolbar;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity implements ListeFragment.MøteEndret, KontaktListeFragment.KontaktEndret {

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
        final BottomNavigationView navView = findViewById(R.id.nav_view);

        Fragment startFragment = new ListeFragment();
        openFragment(startFragment);
        setTitle(getString(R.string.møter));



        navView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()){

                    case R.id.navigation_moter:
                        Fragment moterFragment = new ListeFragment();
                        openFragment(moterFragment);
                        setTitle(getString(R.string.møter));
                        return true;

                    case R.id.navigation_kontakter:
                        Fragment kontakterFragment = new KontaktListeFragment();
                        openFragment(kontakterFragment);
                        setTitle(getString(R.string.kontakter));
                        return true;

                    case R.id.navigation_preferanser:
                        Fragment preferanserFragment = new PreferanserFragment();
                        openFragment(preferanserFragment);
                        setTitle(getString(R.string.preferanser));
                        return true;
                }


                return false;
            }
        });


        db = new DBHandler(this);
        db.getWritableDatabase();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        //bruk shared pref til å velge mellom leggtil og endre kontakt


        startService();
    }
    void openFragment(Fragment fragment){
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
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

    public void leggTil(View v) {
        BottomNavigationView navView = findViewById(R.id.nav_view);
        int id = navView.getSelectedItemId();

        if(id == R.id.navigation_moter) {
            Intent imote = new Intent(this,MøteActivity.class);
            startActivity(imote);
        }
        else if(id == R.id.navigation_kontakter) {
            Intent ikontakt = new Intent(this,LeggTilKontaktActivity.class);
            startActivity(ikontakt);
        }
    }
    /*@Override
    protected void onPostResume() {
        super.onPostResume();
        ListeFragment liste = (ListeFragment) getSupportFragmentManager()
                .findFragmentById(R.id.listefragment);
        liste.oppdater();
    }

     */



}