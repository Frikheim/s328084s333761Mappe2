package com.example.s328084s333761mappe2;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class VisMoteFragment extends Fragment {
    private String id;

    public void init(String id) { this.id = id;
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {super.onActivityCreated(savedInstanceState);
    }

    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View motevindu = inflater.inflate(R.layout.vismote_layout, container, false);

        if( motevindu != null) {
            DBHandler db = new DBHandler(getActivity());
            db.getWritableDatabase();
            //finner møte og setter info
            Møte møte = db.finnMøte(Integer.parseInt(id));
            TextView type = motevindu.findViewById(R.id.visType);
            TextView sted = motevindu.findViewById(R.id.visSted);
            TextView tidspunkt = motevindu.findViewById(R.id.visTidspunkt);
            TextView dato = motevindu.findViewById(R.id.visDato);
            type.setText(møte.getType());
            sted.setText(møte.getSted());
            tidspunkt.setText(møte.getTidspunkt());
            dato.setText(møte.getDato());

            ListView liste = motevindu.findViewById(R.id.deltakerListeView);
            List<MøteDeltakelse> møteDeltakelse = db.finnMøteDeltakelseIMøte(møte.get_ID());

            final ArrayList<String> list = db.deltakerListe(møteDeltakelse);
            Collections.sort(list);
            final ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),android.R.layout.simple_list_item_1, list);
            liste.setAdapter(adapter);

            Button endreMøte = motevindu.findViewById(R.id.endreMøte);
            endreMøte.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick( View view) {
                    View foreldre = (View) view.getParent().getParent();
                    Intent endreMote = new Intent(foreldre.getContext(),EndreMøteActivity.class);
                    endreMote.putExtra("endremoteid", id);
                    startActivity(endreMote);

                    getActivity().onBackPressed();
                }
            });

            ImageButton slettMøte = motevindu.findViewById(R.id.slettMøte);
            slettMøte.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick( View view) {

                    DBHandler db = new DBHandler(getActivity());
                    db.getWritableDatabase();
                    db.slettMøte(Long.parseLong(id));

                    getActivity().onBackPressed();
                }
            });
        }
        return motevindu;
    }

}