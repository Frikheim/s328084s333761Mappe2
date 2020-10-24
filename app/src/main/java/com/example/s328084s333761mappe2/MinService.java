package com.example.s328084s333761mappe2;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.telephony.SmsManager;
import androidx.core.app.NotificationCompat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MinService extends Service {
    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    DBHandler db;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        Intent i = new Intent(this, MainActivity.class);
        PendingIntent pIntent = PendingIntent.getActivity(this, 0, i, 0);

        //Sjekker om det er noen møter i dag
        db = new DBHandler(this);
        db.getWritableDatabase();
        String dato = new SimpleDateFormat("d.M.yyyy", Locale.getDefault()).format(new Date());
        ArrayList<Møte> møter = db.finnAlleMøterIDag(dato);
        if(!møter.isEmpty()) {
            //Lager tekst for notifikasjon
            String tekst = "";
            for (Møte møte: møter) {
                tekst += møte.getType() + " " + møte.getTidspunkt() + "\n";
            }
            //Lager og sender notifikasjon
            Notification notifikasjon = new NotificationCompat.Builder(this)
                    .setContentTitle(getString(R.string.møter_notif))
                    .setContentText(tekst)
                    .setSmallIcon(R.drawable.app_icon)
                    .setContentIntent(pIntent).build();
            notifikasjon.flags |= Notification.FLAG_AUTO_CANCEL;
            notificationManager.notify(0, notifikasjon);

            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
            //Sender SMS til deltakere
            SmsManager smsMan= SmsManager.getDefault();
            String beskjed = prefs.getString(getString(R.string.varsel_tekst_nøkkel),getString(R.string.standard_varsel));
            for(Møte møte : møter) {
                List<MøteDeltakelse> kontakter = db.finnMøteDeltakelseIMøte(møte.get_ID());
                for(MøteDeltakelse deltaker : kontakter) {
                    Kontakt kontakt = db.finnKontakt(deltaker.getDeltaker_ID().intValue());
                    smsMan.sendTextMessage(kontakt.getTelefon(), null, beskjed, null, null);
                }
            }
        }
        return super.onStartCommand(intent, flags, startId);
    }
}
