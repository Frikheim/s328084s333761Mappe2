package com.example.s328084s333761mappe2;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class VisMoteActivity extends AppCompatActivity {
    String id;
    Møte møte;
    TextView type ;
    TextView sted ;
    TextView tidspunkt ;
    TextView dato;
    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vismote_layout);
        Intent i = this.getIntent();
        id= i.getExtras().getString("moteid");
        setTitle(R.string.møter);


        DBHandler db = new DBHandler(this);
        db.getWritableDatabase();
        //finner møte og setter info
        møte = db.finnMøte(Integer.parseInt(id));
        type = findViewById(R.id.visType);
        sted = findViewById(R.id.visSted);
        tidspunkt = findViewById(R.id.visTidspunkt);
        dato = findViewById(R.id.visDato);
        type.setText(møte.getType());
        sted.setText(møte.getSted());
        tidspunkt.setText(møte.getTidspunkt());
        dato.setText(møte.getDato());

        ListView liste = findViewById(R.id.deltakerListeView);
        List<MøteDeltakelse> møteDeltakelse = db.finnMøteDeltakelseIMøte(møte.get_ID());

        final ArrayList<String> list = db.deltakerListe(møteDeltakelse);
        Collections.sort(list);
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, list);
        liste.setAdapter(adapter);


    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.vismotemeny, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.endreMøteAction:
                Intent endreMote = new Intent(this,EndreMøteActivity.class);
                endreMote.putExtra("endremoteid", id);
                startActivity(endreMote);

                finish();
                break;
            case R.id.slettMøteAction:
                DBHandler db = new DBHandler(this);
                db.getWritableDatabase();
                db.slettMøte(Long.parseLong(id));

                finish();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }
}