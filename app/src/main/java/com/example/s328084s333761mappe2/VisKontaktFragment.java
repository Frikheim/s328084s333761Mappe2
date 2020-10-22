package com.example.s328084s333761mappe2;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
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
            EditText visNavn = kontaktvindu.findViewById(R.id.visNavn);
            EditText visTelefon = kontaktvindu.findViewById(R.id.visTelefon);
            Log.d("boks",kontakt.toString());
            visNavn.setText(kontakt.getNavn());
            visTelefon.setText(kontakt.getTelefon());


            Button endreKontakt = kontaktvindu.findViewById(R.id.endreKontakt);
            endreKontakt.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick( View view) {
                    View foreldre = (View) view.getParent().getParent();
                    Intent endreKontakt = new Intent(foreldre.getContext(),EndreKontaktActivity.class);
                    endreKontakt.putExtra("endrekontaktnavn", navn);
                    startActivity(endreKontakt);

                    getActivity().onBackPressed();
                }
            });

            ImageButton slettKontakt = kontaktvindu.findViewById(R.id.slettKontakt);
            slettKontakt.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick( View view) {

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
        EditText visNavn = getView().findViewById(R.id.visNavn);
        EditText visTelefon = getView().findViewById(R.id.visTelefon);
        visNavn.setText(kontakt.getNavn());
        visTelefon.setText(kontakt.getTelefon());
    }

}
