package com.example.s328084s333761mappe2;

import android.os.Bundle;
import android.preference.PreferenceFragment;
import androidx.fragment.app.Fragment;

public class PreferanserFragment extends Fragment {
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getActivity().getFragmentManager().beginTransaction()
                .replace(R.id.frameLayout, new MyPreferenceFragment()).addToBackStack(null)
                .commit();
    }

    public static class MyPreferenceFragment extends PreferenceFragment {
        public MyPreferenceFragment() {
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.preferanser);
        }
    }
}
