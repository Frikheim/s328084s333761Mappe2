package com.example.s328084s333761mappe2;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import androidx.fragment.app.Fragment;
import java.util.ArrayList;

public class ListeFragment extends Fragment {
    private static MøteEndret listener;

    public interface MøteEndret {
        void idEndret(String innhold);
    }

    public View v;

    @Override public void onAttach(Context context) {
        super.onAttach(context);
        Activity activity;
        activity = (Activity) context;
        try {
            listener= (MøteEndret) activity;
        }
        catch(ClassCastException e) {
            throw new ClassCastException(activity.toString() + "må implementere MøteEndret");
        }
    }
    public ListeFragment() {}
    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.liste_layout, container, false);
        ListView lv = v.findViewById(R.id.liste);
        DBHandler db = new DBHandler(getActivity());
        db.getWritableDatabase();
        //Oppretter en liste med alle møte-objekter
        ArrayList<Møte> møter = db.finnAlleMøter();
        final ArrayList<Møte> møteListe = db.sorterMøter(møter); //Sorterer møtelisten på dato og tid
        final MoteAdapter adapter = new MoteAdapter(getContext(),møteListe);
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

    //Oppdaterer listefragmenetet
    public void oppdater() {
        ListView lv = v.findViewById(R.id.liste);
        DBHandler db = new DBHandler(getActivity());
        db.getWritableDatabase();
        ArrayList<Møte> møter = db.finnAlleMøter();
        final ArrayList<Møte> møteListe = db.sorterMøter(møter);
        final MoteAdapter adapter = new MoteAdapter(getContext(),møteListe);
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

}