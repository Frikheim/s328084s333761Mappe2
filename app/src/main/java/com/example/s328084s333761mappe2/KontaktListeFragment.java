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
    private static ArrayAdapter<String> adapter;
    private static KontaktEndret listener;

    public interface KontaktEndret {
        public void idKontaktEndret(String innhold);
    }

    public View v;

    @Override public void onAttach(Context context) {
        super.onAttach(context);
        Activity activity;
        activity= (Activity) context;
        try {
            listener= (KontaktEndret) activity;
            System.out.println("satt lytter");
        }
        catch(ClassCastException e) {
            throw new ClassCastException(activity.toString()+ "må implementere UrlEndret");
        }
    }
    public KontaktListeFragment() {}
    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.liste_layout, container, false);
        ListView lv = (ListView) v.findViewById(R.id.liste);
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
        return v;
    }

    public void oppdater() {
        ListView lv = (ListView) v.findViewById(R.id.liste);
        DBHandler db = new DBHandler(getActivity());
        db.getWritableDatabase();
        ArrayList<Kontakt> kontakter = db.finnAlleKontakter();
        final ArrayList<Kontakt> list = sortertKontaktliste(kontakter);
        //final ArrayList<String> list = db.kontaktListe();
        final KontaktAdapter adapter = new KontaktAdapter(getContext(),list);
        //final ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),android.R.layout.simple_list_item_1, list);
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
        ArrayList<Kontakt> sortertListe = new ArrayList<>();
        sortertListe.add(kontakter.get(0));

        for(int i = 1; i < kontakter.size(); i++) {
            for(int j = 0; j < sortertListe.size(); j++) {
                if(sortertListe.get(j).getNavn().equals(kontakter.get(i).getNavn())) {
                    sortertListe.add(kontakter.get(i));
                    break;
                }
                else if(sortertListe.get(j).getNavn().compareTo(kontakter.get(i).getNavn()) > 0) {
                    sortertListe.add(j,kontakter.get(i));
                    break;
                }
                else {
                    if((sortertListe.size() - j) == 1) {
                        sortertListe.add(kontakter.get(i));
                        break;
                    }
                }
            }
        }
        return sortertListe;
    }
}