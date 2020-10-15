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
        public void idEndret(String innhold);
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
        final ArrayList<String> list = db.kontaktListe();
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),android.R.layout.simple_list_item_1, list);
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String data = adapter.getItem(i);
                listener.idEndret(data);
            }
        });
        return v;
    }

    public void oppdater() {
        ListView lv = (ListView) v.findViewById(R.id.liste);
        DBHandler db = new DBHandler(getActivity());
        db.getWritableDatabase();
        final ArrayList<String> list = db.kontaktListe();
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),android.R.layout.simple_list_item_1, list);
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String data = adapter.getItem(i);listener.idEndret(data);
            }
        });
    }
}