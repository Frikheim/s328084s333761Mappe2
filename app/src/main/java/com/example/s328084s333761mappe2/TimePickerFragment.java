package com.example.s328084s333761mappe2;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.format.DateFormat;
import android.widget.TimePicker;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.util.Calendar;

public class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        // Use the current time as the default values for the picker
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        // Create a new instance of TimePickerDialog and return it
        return new TimePickerDialog(getActivity(), this, hour, minute, true);
    }

    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this.getActivity());
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(getString(R.string.velgTidspunkt),hourOfDay + ":" + minute);
        editor.apply();
        mCallback.onDialogDismissListener();
    }

    public interface OnDialogDismissListener {
        public void onDialogDismissListener();
    }

    TimePickerFragment.OnDialogDismissListener mCallback;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            mCallback = (TimePickerFragment.OnDialogDismissListener) getActivity();
        }
        catch (ClassCastException e) {
            throw new  ClassCastException("kallende klasse må implementere interface");
        }
    }
}
