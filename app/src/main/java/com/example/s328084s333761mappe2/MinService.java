package com.example.s328084s333761mappe2;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

public class MinService extends Service {
    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("TAG", "Er i minservice");
        Toast.makeText(getApplicationContext(), "I MinService", Toast.LENGTH_SHORT).show();
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        Intent i = new Intent(this, MainActivity.class);
        PendingIntent pIntent = PendingIntent.getActivity(this, 0, i, 0);
        Notification notifikasjon = new NotificationCompat.Builder(this)
                .setContentTitle(getString(R.string.møter_notif))
                .setContentText("Tekst") //Fiks dette :)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentIntent(pIntent).build();
        notifikasjon.flags |= Notification.FLAG_AUTO_CANCEL;
        notificationManager.notify(0, notifikasjon);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        if(prefs.getBoolean(getString(R.string.sms_nøkkel),false)) {
            // send SMS
        }

        return super.onStartCommand(intent, flags, startId);
    }
}
