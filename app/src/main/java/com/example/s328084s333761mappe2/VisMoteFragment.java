package com.example.s328084s333761mappe2;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;

public class VisMoteFragment extends Fragment {
    private int id;

    public void init(String id) { this.id = Integer.parseInt(id);
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {super.onActivityCreated(savedInstanceState);
    }

    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View motevindu = inflater.inflate(R.layout.vismote_layout, container, false);

        if( motevindu != null) {
            DBHandler db = new DBHandler(getActivity());
            db.getWritableDatabase();
            //finner møte og setter info
            Møte møte = db.finnMøte(id);
            TextView boks = motevindu.findViewById(R.id.møteView);
            Log.d("boks",møte.toString());
            boks.setText(møte.toString());

            ListView liste = motevindu.findViewById(R.id.deltakerListeView);
            List<MøteDeltakelse> møteDeltakelse = db.finnMøteDeltakelseIMøte(møte.get_ID());

            final ArrayList<String> list = db.deltakerListe(møteDeltakelse);
            Log.d("deltakerliste",list.get(0));
            final ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),android.R.layout.simple_list_item_1, list);
            liste.setAdapter(adapter);

            Button endreMøte = motevindu.findViewById(R.id.endreMøte);
            endreMøte.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick( View view) {
                    View foreldre = (View) view.getParent().getParent();
                    TextView textboks = foreldre.findViewById(R.id.møteView);
                    Log.d("tekstboks","");
                    String boksInnhold = textboks.getText().toString();
                    String[] splittet = boksInnhold.split(":");
                    Long id = Long.parseLong(splittet[0]);
                    Intent endreMote = new Intent(foreldre.getContext(),EndreMøteActivity.class);
                    endreMote.putExtra("endremoteid", id);
                    Log.d("id",id.toString());
                    startActivity(endreMote);

                    getActivity().onBackPressed();
                }
            });

            Button slettMøte = motevindu.findViewById(R.id.slettMøte);
            slettMøte.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick( View view) {
                    View foreldre = (View) view.getParent().getParent();
                    TextView textboks = foreldre.findViewById(R.id.møteView);
                    String boksInnhold = textboks.getText().toString();
                    String[] splittet = boksInnhold.split(":");
                    Long id = Long.parseLong(splittet[0]);
                    DBHandler db = new DBHandler(getActivity());
                    db.getWritableDatabase();
                    db.slettMøte(id);

                    getActivity().onBackPressed();
                }
            });
        }
        return motevindu;
    }

    public void updateUrl(String id) {
        this.id = Integer.parseInt(id);
        DBHandler db = new DBHandler(getActivity());
        db.getWritableDatabase();
        Møte møte = db.finnMøte(this.id);
        TextView boks = getView().findViewById(R.id.møteView);
        boks.setText(møte.toString());
        ListView liste = getView().findViewById(R.id.deltakerListeView);
        List<MøteDeltakelse> møteDeltakelse = db.finnMøteDeltakelseIMøte(møte.get_ID());
        final ArrayList<String> list = db.deltakerListe(møteDeltakelse);
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),android.R.layout.simple_list_item_1, list);
        liste.setAdapter(adapter);
    }

    public void endreMøte(View v) {

    }

    public void slettMøte(View v) {

    }
}