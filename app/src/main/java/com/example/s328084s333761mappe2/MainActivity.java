package com.example.s328084s333761mappe2;

import androidx.annotation.NonNull;
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
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity implements ListeFragment.MøteEndret, KontaktListeFragment.KontaktEndret {

    DBHandler db;
    SharedPreferences.OnSharedPreferenceChangeListener onSharedPreferenceChangeListener;

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

        //Åpner listefragmentet
        Fragment startFragment = new ListeFragment();
        openFragment(startFragment);
        setTitle(getString(R.string.møter));

        navView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                //Sjekker hvilket av ikonene på navigasjonsmenyen som er valgt og åpner det
                //aktuelle fragmentet
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
        SharedPreferences.Editor editor = prefs.edit();
        //Nullstiller valgt dato og tid i SharedPreferences
        editor.putString(getString(R.string.velgDato),"");
        editor.putString(getString(R.string.velgTidspunkt),"");

        //Kjører startService når klokkeslettet for varsel og SMS endres slik at pendingintentet oppdateres med den nye tiden
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
        //Hvis ikke rettigheter for å sende SMS er satt på telefonen får bruker spørsmål om å sette disse rettighetene
        if(!(MY_PERMISSIONS_REQUEST_SEND_SMS == PackageManager.PERMISSION_GRANTED && MY_PHONE_STATE_PERMISSION ==PackageManager.PERMISSION_GRANTED)){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS,Manifest.permission.READ_PHONE_STATE}, 0);
        }

        //Sender signal til broadcastreciever
        startService();
    }

    //Metode for å åpne fragmenter
    void openFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    //Sender signal til broadcastreciever
    public void startService() {
        Intent intent = new Intent();
        intent.setAction(getString(R.string.broadcast));
        sendBroadcast(intent);
    }

    //Metode for å stoppe periodisk service og stoppe varsler
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

    //Metode som trigger når bruker trykker på pluss-knappen øverst i enten listen over møter eller
    //listen over kontakter
    public void leggTil(View v) {
        BottomNavigationView navView = findViewById(R.id.nav_view);
        int id = navView.getSelectedItemId();

        //Må sjekke hvilket fragment brukeren befinner seg på, og åpne LeggTilMøte eller LeggTilKontakt
        //basert på dette
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