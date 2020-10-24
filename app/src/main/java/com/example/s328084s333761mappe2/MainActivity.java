package com.example.s328084s333761mappe2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toolbar;


import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity implements ListeFragment.MøteEndret, KontaktListeFragment.KontaktEndret {

    DBHandler db;
    SharedPreferences.OnSharedPreferenceChangeListener onSharedPreferenceChangeListener;
    public Toolbar toolbarvismote;
    public Toolbar toolbarendremote;
    public Toolbar toolbarleggtilmote;
    public Toolbar toolbarendrekontakt;
    public Toolbar toolbarleggtilkontakt;
    public ActionBar actionBarvismote;
    public ActionBar actionBarendremote;
    public ActionBar actionBarleggtilmote;
    public ActionBar actionBarendrekontakt;
    public ActionBar actionleggtilkontakt;
    public void idEndret(String innhold) {

        Intent i = new Intent(this, VisMoteActivity.class);
        i.putExtra("moteid", innhold);
        startActivity(i);

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
        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        //bruk shared pref til å velge mellom leggtil og endre kontakt
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(getString(R.string.velgDato),"");
        editor.putString(getString(R.string.velgTidspunkt),"");

        //kjører startService når klokkeslettet for varsel og sms endres slik at pendingintentet oppdateres med det nye tiden
        onSharedPreferenceChangeListener = new SharedPreferences.OnSharedPreferenceChangeListener() {
            @Override
            public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
                if(key.equals(getString(R.string.varsel_nøkkel))) {
                    if(prefs.getBoolean(getString(R.string.varsel_nøkkel),false)) {
                        startService();
                    }
                    else {
                        stoppPeriodisk();
                    }
                }
                if (key.equals(getString(R.string.klokkeslett_nøkkel))) {
                    if(prefs.getBoolean(getString(R.string.varsel_nøkkel),false)) {
                        startService();
                    }
                }
            }
        };


        int MY_PHONE_STATE_PERMISSION;
        int MY_PERMISSIONS_REQUEST_SEND_SMS;
        MY_PERMISSIONS_REQUEST_SEND_SMS = ActivityCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS);
        MY_PHONE_STATE_PERMISSION = ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE);
        if(!(MY_PERMISSIONS_REQUEST_SEND_SMS == PackageManager.PERMISSION_GRANTED&& MY_PHONE_STATE_PERMISSION ==PackageManager.PERMISSION_GRANTED)){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS,Manifest.permission.READ_PHONE_STATE}, 0);
        }

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

        Intent i = new Intent(this, VisKontaktActivity.class);
        i.putExtra("kontaktnavn", innhold);
        startActivity(i);

    }

    public void leggTil(View v) {
        BottomNavigationView navView = findViewById(R.id.nav_view);
        int id = navView.getSelectedItemId();

        if(id == R.id.navigation_moter) {
            Intent imote = new Intent(this, LeggTilMøteActivity.class);
            startActivity(imote);
        }
        else if(id == R.id.navigation_kontakter) {
            Intent ikontakt = new Intent(this,LeggTilKontaktActivity.class);
            startActivity(ikontakt);
        }
    }


}