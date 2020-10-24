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
    public View getView(int posisjon, View convertView, ViewGroup parent) {
        //Får tak i kontakt-objektet for denne posisjonen
        Kontakt kontakt = getItem(posisjon);
        //Sjekker om en eksisterende view blir brukt, hvis ikke inflater vi viewet
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.kontakt_view, parent, false);
        }
        TextView navn = convertView.findViewById(R.id.navnView);
        navn.setText(kontakt.getNavn());
        return convertView;
    }
}
