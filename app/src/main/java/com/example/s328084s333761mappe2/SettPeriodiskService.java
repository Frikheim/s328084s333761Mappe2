package com.example.s328084s333761mappe2;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.Calendar;

public class SettPeriodiskService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);

        if(prefs.getBoolean(getString(R.string.varsel_nøkkel),false)) {
            Calendar cal = Calendar.getInstance();
            Intent i = new Intent(this, MinService.class);
            PendingIntent pintent = PendingIntent.getService(this, 0, i, 0);
            AlarmManager alarm = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

            String klokkeslett = prefs.getString(getString(R.string.klokkeslett_nøkkel),getString(R.string.klokkeslett_default));
            Log.d("TAG",klokkeslett);
            cal.set(Calendar.HOUR_OF_DAY,Integer.parseInt(klokkeslett));
            Log.d("TAG",cal.toString());
            cal.set(Calendar.MINUTE, 0);
            cal.set(Calendar.SECOND, 0);
            alarm.setInexactRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pintent);
            Log.d("TAG",String.valueOf(cal.getTimeInMillis()));
        }
        Log.d("TAG", "Er i settperiodiskservice");

        return super.onStartCommand(intent, flags, startId);
    }
}
