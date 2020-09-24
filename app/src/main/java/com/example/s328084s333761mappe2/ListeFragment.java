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

public class ListeFragment extends Fragment {
    private static ArrayAdapter<String> adapter;
    private static UrlEndret listener;
    public interface UrlEndret {
        public void linkEndret(String link);
    }

    @Override public void onAttach(Context context) {
        super.onAttach(context);
        Activity activity;
        activity= (Activity) context;
        try {
            listener= (UrlEndret) activity;
            System.out.println("satt lytter");
        }
        catch(ClassCastException e) {
            throw new ClassCastException(activity.toString()+ "må implementere UrlEndret");
        }
    }
    public ListeFragment() {}
    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.liste_layout, container, false);
        ListView lv = (ListView) v.findViewById(R.id.liste);
        String[] values= new String[]{"script1.sql", "script2.sql"};
        final ArrayList<String> list = new ArrayList<String>();
        for (int i = 0; i < values.length; ++i) {
            list.add(values[i]);
        }
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),android.R.layout.simple_list_item_1, list);
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String data = adapter.getItem(i);listener.linkEndret(data);
            }
        });
        return v;
    }
} 