package com.example.s328084s333761mappe2;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.preference.PreferenceManager;
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

        //Sjekker om varselNøkkel er true, dvs at brukeren har skrudd på SMS- og varseltjeneste
        if(prefs.getBoolean(getString(R.string.varsel_nøkkel),false)) {
            Calendar cal = Calendar.getInstance();
            Intent i = new Intent(this, MinService.class);
            PendingIntent pintent = PendingIntent.getService(this, 0, i, 0);
            AlarmManager alarm = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

            String klokkeslett = prefs.getString(getString(R.string.klokkeslett_nøkkel),getString(R.string.klokkeslett_default)); //Henter inn det valgte klokkeslettet fra SharedPreferences
            //Setter Calendar-objektet til klokkeslettet valgt av bruker
            cal.set(Calendar.HOUR_OF_DAY,Integer.parseInt(klokkeslett));
            cal.set(Calendar.MINUTE, 0);
            cal.set(Calendar.SECOND, 0);
            //Setter notifikasjon på og repeterer den med et intervall på ett døgn
            alarm.setInexactRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pintent);
        }
        return super.onStartCommand(intent, flags, startId);
    }
}
