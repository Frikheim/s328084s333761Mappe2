package com.example.s328084s333761mappe2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class KontaktAdapter extends ArrayAdapter<Kontakt> {
    public KontaktAdapter(Context context, ArrayList<Kontakt> kontakter) {
        super(context, 0, kontakter);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Kontakt kontakt = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.kontakt_view, parent, false);
        }
        // Lookup view for data population
        TextView navn = (TextView) convertView.findViewById(R.id.navnView);
        // Populate the data into the template view using the data object
        navn.setText(kontakt.getNavn());
        // Return the completed view to render on screen
        return convertView;
    }
}
