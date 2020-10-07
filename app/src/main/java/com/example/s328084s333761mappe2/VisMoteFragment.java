package com.example.s328084s333761mappe2;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;

public class VisMoteFragment extends Fragment {
    private int id;

    public void init(String id) { this.id = Integer.parseInt(id);
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {super.onActivityCreated(savedInstanceState);
    }

    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View motevindu = inflater.inflate(R.layout.vismote_layout, container, false);
        if( motevindu != null) {
            /*WebView script = (WebView) motevindu.findViewById(R.id.visscript);
            File imgFile= new File("file:///android_asset/" +scriptnavn);
            script.loadUrl("file:///android_asset/" +scriptnavn);

             */
            DBHandler db = new DBHandler(getActivity());
            db.getWritableDatabase();
            Møte møte = db.finnMøte(id);
            TextView boks = motevindu.findViewById(R.id.møteView);
            Log.d("boks",møte.toString());
            boks.setText(møte.toString());
            ListView liste = motevindu.findViewById(R.id.deltakerListeView);
            List<MøteDeltakelse> møteDeltakelse = db.finnMøteDeltakelseIMøte(møte.get_ID());
            final ArrayList<String> list = db.deltakerListe(møteDeltakelse);
            final ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),android.R.layout.simple_list_item_1, list);
            liste.setAdapter(adapter);
        }
        return motevindu;
    }

    public void updateUrl(String id) {
        this.id = Integer.parseInt(id);
        DBHandler db = new DBHandler(getActivity());
        db.getWritableDatabase();
        Møte møte = db.finnMøte(this.id);
        TextView boks = getView().findViewById(R.id.møteView);
        boks.setText(møte.toString());
        ListView liste = getView().findViewById(R.id.deltakerListeView);
        List<MøteDeltakelse> møteDeltakelse = db.finnMøteDeltakelseIMøte(møte.get_ID());
        final ArrayList<String> list = db.deltakerListe(møteDeltakelse);
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),android.R.layout.simple_list_item_1, list);
        liste.setAdapter(adapter);
    }
}