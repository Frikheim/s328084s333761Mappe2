package com.example.s328084s333761mappe2;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import androidx.fragment.app.Fragment;

import java.io.File;

public class VisScriptFragment extends Fragment {
    private String scriptnavn;
    public void init(String navn) {
        scriptnavn = navn;
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {super.onActivityCreated(savedInstanceState);
    }

    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View scriptvindu = inflater.inflate(R.layout.visfil_layout, container, false);
        if( scriptvindu != null) {
            WebView script = (WebView) scriptvindu.findViewById(R.id.visscript);
            File imgFile= new File("file:///android_asset/" +scriptnavn);
            script.loadUrl("file:///android_asset/" +scriptnavn);
        }
        return scriptvindu;
    }

    public void updateUrl(String navn) {
        scriptnavn = navn;
        WebView mittscript= (WebView) getView().findViewById(R.id.visscript);
        mittscript.loadUrl("file:///android_asset/" +scriptnavn);
    }
}