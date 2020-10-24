package com.example.s328084s333761mappe2;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.fragment.app.Fragment;

import java.util.ArrayList;

public class KontaktListeFragment extends Fragment {
    private static KontaktEndret listener;

    public interface KontaktEndret {
        void idKontaktEndret(String innhold);
    }

    public View v;

    @Override public void onAttach(Context context) {
        super.onAttach(context);
        Activity activity;
        activity= (Activity) context;
        try {
            listener = (KontaktEndret) activity;
        }
        catch(ClassCastException e) {
            throw new ClassCastException(activity.toString()+ "må implementere KontaktEndret");
        }
    }
    
    public KontaktListeFragment() {}
    
    @Override 
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.liste_layout, container, false);
        ListView lv = v.findViewById(R.id.liste);
        DBHandler db = new DBHandler(getActivity());
        db.getWritableDatabase();
        //Finner alle kontaktene i databasen
        ArrayList<Kontakt> kontakter = db.finnAlleKontakter();
        //Sorterer kontaktene alfabetisk
        final ArrayList<Kontakt> list = sortertKontaktliste(kontakter);
        final KontaktAdapter adapter = new KontaktAdapter(getContext(),list);
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Kontakt data = adapter.getItem(i);
                listener.idKontaktEndret(data.getNavn());
            }
        });
        return v;
    }

    //Oppdaterer kontaktlistefragmenetet
    public void oppdater() {
        ListView lv = v.findViewById(R.id.liste);
        DBHandler db = new DBHandler(getActivity());
        db.getWritableDatabase();
        ArrayList<Kontakt> kontakter = db.finnAlleKontakter();
        final ArrayList<Kontakt> list = sortertKontaktliste(kontakter);
        final KontaktAdapter adapter = new KontaktAdapter(getContext(),list);
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Kontakt data = adapter.getItem(i);
                listener.idKontaktEndret(data.getNavn());
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        oppdater();
    }

    public ArrayList<Kontakt> sortertKontaktliste(ArrayList<Kontakt> kontakter) {
        ArrayList<Kontakt> sortertListe = new ArrayList<>(); //Oppretter en ny sortert liste med kontakt-objekter
        sortertListe.add(kontakter.get(0)); //Legger til den første kontakten fra innparameter-listen i den sorterte listen

        for(int i = 1; i < kontakter.size(); i++) {
            for(int j = 0; j < sortertListe.size(); j++) {
                if(sortertListe.get(j).getNavn().equals(kontakter.get(i).getNavn())) {
                    //Dersom navnene er identiske legges det originale kontakt-objektet bakerst i den sorterte listen
                    sortertListe.add(kontakter.get(i));
                    break;
                }
                else if(sortertListe.get(j).getNavn().compareTo(kontakter.get(i).getNavn()) > 0) {
                    //Dersom testen over returnerer 1 vil det si at originalkontakten skal sorteres før kontakten vi sammenligner
                    //med alfabetisk, og legger derfor inn originalkontakten før kontakten den sammenlignes med
                    sortertListe.add(j,kontakter.get(i));
                    break;
                }
                else {
                    //Dersom testen i else if-en returnerer -1 vil det si at originalkontakten skal sorteres etter kontakten vi sammenligner
                    //med alfabetisk, og legger derfor inn originalkontakten sist i den sorterte listen
                    if((sortertListe.size() - j) == 1) {
                        //Dersom sjekker over er sann betyr det at vi er på det siste objektet i den sorterte listen,
                        //og legger dermed originalkontakten sist i den sorterte listen
                        sortertListe.add(kontakter.get(i));
                        break;
                    }
                }
            }
        }
        return sortertListe;
    }
}