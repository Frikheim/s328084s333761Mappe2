package com.example.s328084s333761mappe2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

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
    }
}