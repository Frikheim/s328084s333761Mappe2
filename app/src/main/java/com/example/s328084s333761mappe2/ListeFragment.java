package com.example.s328084s333761mappe2;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.fragment.app.Fragment;

import java.util.ArrayList;

public class ListeFragment extends Fragment {
    private static ArrayAdapter<String> adapter;
    private static MøteEndret listener;

    public interface MøteEndret {
        public void idEndret(String innhold);
    }

    public View v;

    @Override public void onAttach(Context context) {
        super.onAttach(context);
        Activity activity;
        activity= (Activity) context;
        try {
            listener= (MøteEndret) activity;
            System.out.println("satt lytter");
        }
        catch(ClassCastException e) {
            throw new ClassCastException(activity.toString()+ "må implementere UrlEndret");
        }
    }
    public ListeFragment() {}
    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.liste_layout, container, false);
        ListView lv = (ListView) v.findViewById(R.id.liste);
        DBHandler db = new DBHandler(getActivity());
        db.getWritableDatabase();
        ArrayList<Møte> møter = db.finnAlleMøter();
        final ArrayList<Møte> møteListe = sorterMøter(møter);
        for (Møte møte: møteListe) {
            Log.d("TAG",møte.getDato());
        }
        //final ArrayList<String> list = db.møteListe();
        final MoteAdapter adapter = new MoteAdapter(getContext(),møteListe);
        //final ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),android.R.layout.simple_list_item_1, list);
        lv.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Møte data = adapter.getItem(i);
                listener.idEndret(data.get_ID().toString());
            }
        });
        return v;
    }

    public void oppdater() {
        ListView lv = (ListView) v.findViewById(R.id.liste);
        DBHandler db = new DBHandler(getActivity());
        db.getWritableDatabase();
        ArrayList<Møte> møter = db.finnAlleMøter();
        final ArrayList<Møte> møteListe = sorterMøter(møter);
        //final ArrayList<String> list = db.møteListe();
        final MoteAdapter adapter = new MoteAdapter(getContext(),møteListe);
        //final ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),android.R.layout.simple_list_item_1, list);
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Møte data = adapter.getItem(i);
                listener.idEndret(data.get_ID().toString());
            }
        });
    }


    @Override
    public void onResume() {
        super.onResume();
        oppdater();
    }

    public ArrayList<Møte> sorterMøter(ArrayList<Møte> møter) {
        ArrayList<Møte> sortertListe = new ArrayList();
        sortertListe.add(møter.get(0));
        boolean leter;
        for (int i = 1; i < møter.size(); i++) {
            leter = true;
            String dato = møter.get(i).getDato();
            Log.d("TAG",dato);
            String[] splittet = dato.split("\\.");
            int dag = Integer.parseInt(splittet[0]);
            int måned = Integer.parseInt(splittet[1]);
            int år = Integer.parseInt(splittet[2]);
            int plassering = 0;
            Log.d("TAG", "Nå sorterer vi " + i);
            while(leter) {
                Log.d("TAG", "Nå er vi på plassering " + plassering);
                String datoSortert = sortertListe.get(plassering).getDato();
                String[] splittetSortert = datoSortert.split("\\.");
                int dagSortert = Integer.parseInt(splittetSortert[0]);
                int månedSortert = Integer.parseInt(splittetSortert[1]);
                int årSortert = Integer.parseInt(splittetSortert[2]);
                if(årSortert == år) {
                    if(månedSortert == måned) {
                        if(dagSortert == dag) {
                            String tid = møter.get(i).getTidspunkt();
                            String tidSortert = sortertListe.get(plassering).getTidspunkt();
                            String[] splittetTid = tid.split(":");
                            String[] splittetTidSortert = tidSortert.split(":");
                            int time = Integer.parseInt(splittetTid[0]);
                            int timeSortert = Integer.parseInt(splittetTidSortert[0]);
                            int minutt = Integer.parseInt(splittetTid[1]);
                            int minuttSortert = Integer.parseInt(splittetTidSortert[1]);

                            if(timeSortert == time) {
                                if(minuttSortert == minutt) {
                                    sortertListe.add(møter.get(i));
                                    leter = false;
                                }
                                else if(minuttSortert < minutt) {
                                    plassering++;
                                    if(plassering == sortertListe.size()) {
                                        sortertListe.add(møter.get(i));
                                        leter = false;
                                    }
                                }
                                else {
                                    sortertListe.add(plassering,møter.get(i));
                                    leter = false;
                                }
                            }
                            else if(timeSortert < time) {
                                plassering++;
                                if(plassering == sortertListe.size()) {
                                    sortertListe.add(møter.get(i));
                                    leter = false;
                                }
                            }
                            else {
                                sortertListe.add(plassering,møter.get(i));
                                leter = false;
                            }
                        }
                        else if(dagSortert < dag) {
                            plassering++;
                            if(plassering == sortertListe.size()) {
                                sortertListe.add(møter.get(i));
                                Log.d("TAG", "Ble plassert etter");
                                leter = false;
                            }
                        }
                        else {
                            sortertListe.add(plassering,møter.get(i));
                            Log.d("TAG", "Ble plassert før");
                            leter = false;
                        }
                    }
                    else if(månedSortert < måned) {
                        plassering++;
                        if(plassering == sortertListe.size()) {
                            sortertListe.add(møter.get(i));
                            leter = false;
                        }
                    }
                    else {
                        sortertListe.add(plassering,møter.get(i));
                        leter = false;
                    }
                }
                else if(årSortert < år) {
                    plassering++;
                    if(plassering == sortertListe.size()) {
                        sortertListe.add(møter.get(i));
                        leter = false;
                    }
                }
                else {
                    sortertListe.add(plassering,møter.get(i));
                    leter = false;
                }
            }
        }
        return sortertListe;
    }
}