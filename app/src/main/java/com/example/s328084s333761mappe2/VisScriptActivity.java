package com.example.s328084s333761mappe2;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

public class VisScriptActivity extends AppCompatActivity {
    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent i = this.getIntent();
        String bnavn= i.getExtras().getString("scriptnavn");
        VisScriptFragment visscript=new VisScriptFragment();
        visscript.init(bnavn);
        FragmentManager fragmentManager;
        fragmentManager= getSupportFragmentManager();
        fragmentManager.beginTransaction().add(android.R.id.content,visscript).commit();
    }
}