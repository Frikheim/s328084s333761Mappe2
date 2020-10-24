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
    public View getView(int posisjon, View convertView, ViewGroup parent) {
        //Får tak i møte-objektet for denne posisjonen
        Møte møte = getItem(posisjon);
        //Sjekker om en eksisterende view blir brukt, hvis ikke inflater vi viewet
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.mote_view, parent, false);
        }

        TextView type = convertView.findViewById(R.id.typeView);
        TextView sted = convertView.findViewById(R.id.stedView);
        TextView tidspunkt = convertView.findViewById(R.id.tidView);
        TextView dato = convertView.findViewById(R.id.datoView);

        type.setText(møte.getType());
        sted.setText(møte.getSted());
        tidspunkt.setText(møte.getTidspunkt());
        dato.setText(møte.getDato());

        return convertView;
    }
}
