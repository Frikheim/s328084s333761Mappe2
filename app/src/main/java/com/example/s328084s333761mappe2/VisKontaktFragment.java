package com.example.s328084s333761mappe2;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;

public class VisKontaktFragment extends Fragment {
    private String navn;

    public void init(String navn) { this.navn = navn;
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {super.onActivityCreated(savedInstanceState);
    }

    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View kontaktvindu = inflater.inflate(R.layout.viskontakt_layout, container, false);
        if( kontaktvindu != null) {

            DBHandler db = new DBHandler(getActivity());
            db.getWritableDatabase();
            Kontakt kontakt = db.finnKontakt(navn);
            TextView boks = kontaktvindu.findViewById(R.id.kontaktView);
            Log.d("boks",kontakt.toString());
            boks.setText(kontakt.toString());


            Button endreKontakt = kontaktvindu.findViewById(R.id.endreKontakt);
            endreKontakt.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick( View view) {
                    View foreldre = (View) view.getParent().getParent();
                    TextView textboks = foreldre.findViewById(R.id.kontaktView);
                    String boksInnhold = textboks.getText().toString();
                    String[] splittet = boksInnhold.split(":");
                    String navn = splittet[0];
                    Intent endreKontakt = new Intent(foreldre.getContext(),EndreKontaktActivity.class);
                    endreKontakt.putExtra("endrekontaktnavn", navn);
                    startActivity(endreKontakt);

                    getActivity().onBackPressed();
                }
            });

            Button slettKontakt = kontaktvindu.findViewById(R.id.slettKontakt);
            slettKontakt.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick( View view) {
                    View foreldre = (View) view.getParent().getParent();
                    TextView textboks = foreldre.findViewById(R.id.kontaktView);
                    String boksInnhold = textboks.getText().toString();
                    String[] splittet = boksInnhold.split(":");
                    String navn = splittet[0];
                    DBHandler db = new DBHandler(getActivity());
                    db.getWritableDatabase();
                    db.slettKontakt(navn);

                    getActivity().onBackPressed();
                }
            });
        }
        return kontaktvindu;
    }

    public void updateUrl(String navn) {
        this.navn = navn;
        DBHandler db = new DBHandler(getActivity());
        db.getWritableDatabase();
        Kontakt kontakt = db.finnKontakt(this.navn);
        TextView boks = getView().findViewById(R.id.kontaktView);
        boks.setText(kontakt.toString());
    }

}
