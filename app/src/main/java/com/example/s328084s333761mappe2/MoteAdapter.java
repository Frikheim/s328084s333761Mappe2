package com.example.s328084s333761mappe2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class MoteAdapter extends ArrayAdapter<Møte> {
    public MoteAdapter(Context context, ArrayList<Møte> møter) {
        super(context, 0, møter);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Møte møte = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.mote_view, parent, false);
        }
        // Lookup view for data population
        TextView type = (TextView) convertView.findViewById(R.id.typeView);
        TextView sted = (TextView) convertView.findViewById(R.id.stedView);
        TextView tidspunkt = (TextView) convertView.findViewById(R.id.tidView);
        TextView dato = (TextView) convertView.findViewById(R.id.datoView);
        // Populate the data into the template view using the data object
        type.setText(møte.getType());
        sted.setText(møte.getSted());
        tidspunkt.setText(møte.getTidspunkt());
        dato.setText(møte.getDato());
        // Return the completed view to render on screen
        return convertView;
    }
}
