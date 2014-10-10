package app.rappla;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import app.rappla.activities.RapplaActivity;
import app.rappla.calendar.RaplaCalendar;
import app.rappla.inet.DownloadRaplaTask;

public class RaplaBackgroundService extends Service {

    public static final int ID_UPDATE_NOTIFICATION = 97035;

    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        Log.v("RaplaBackgroundService", System.currentTimeMillis() + ": Service erstellt.");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.v("RaplaBackgroundService", System.currentTimeMillis() + ": Service gestartet.");

        updateData();

        // Nachdem unsere Methode abgearbeitet wurde, soll sich der Service
        // selbst stoppen.
        stopSelf();
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        Log.v("RaplaBackgroundService", System.currentTimeMillis() + ": Service zerstoert.");
    }

    private void updateData() {
        if (isWifiOnly(this)) {
            ConnectivityManager connManager = (ConnectivityManager) StaticContext.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            if (!mWifi.isConnected()) {
                Log.d("BackgroundUpdateService", "Not wifi, aborting");
                return;
            }
        }

        final int notificationHash = getCalendarHash(this);

        final Context context = getApplicationContext();

        Log.d("BackgroundUpdateService", "Last Calendar Hash: " + notificationHash);

        DownloadRaplaTask downloadTask = new DownloadRaplaTask(this, new OnTaskCompleted<RaplaCalendar>() {
            public void onTaskCompleted(RaplaCalendar result) {
                if (notificationHash != result.hashCode()) {
                    result.save(context);
                    Log.d("BackgroundUpdateService", "new CalendarHash: " + result.hashCode());
                    Log.d("BackgroundUpdateService", "Showing Notification");
                    showNotification(StaticContext.getContext());
                } else {
                    Log.d("BackgroundUpdateService", "new CalendarHash equals oldCalendarHash");
                }
            }
        }, false);

        Log.d("BackgroundUpdateService", "Downloading Rapla");
        downloadTask.execute(RapplaActivity.getCalendarURL(this));

    }

    private void showNotification(Context context) {
        Uri notificationSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        Intent resultIntent = new Intent(context, RapplaActivity.class);
        resultIntent.setAction("NotificationButton");
        PendingIntent eventIntent = PendingIntent.getActivity(context, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.ic_launcher)
                .setContentTitle("Der Rapla hat sich Verändert!")
                .setContentText("Öffnen sie die Rapla-App um die Änderugnen einzusehen.")
                .setLights(0xFFff4500, 100, 100)
                .setSound(notificationSound).setVibrate(new long[]{0, 150, 150, 250, 100, 100})
                .setContentIntent(eventIntent);
        Notification notification = mBuilder.build();

        nm.notify(ID_UPDATE_NOTIFICATION, notification);
    }

    private int getCalendarHash(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getInt(RapplaActivity.lastCalendarHashString, 0);
    }

    private boolean isWifiOnly(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getBoolean("onlyWifiSync", false);
    }
}