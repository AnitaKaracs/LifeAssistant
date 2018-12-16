package hu.anita.lifeassistant.cleaningduty.notification;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import hu.anita.lifeassistant.MainActivity;

public class CleaningDutyNotificationReceiver extends BroadcastReceiver {
    public void onReceive(Context context, Intent intent) {
        CleaningDutyNotificationHelper.createInstance(context);
        Intent notificationIntent = new Intent(context, MainActivity.class);
        notificationIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                | Intent.FLAG_ACTIVITY_CLEAR_TASK
                | Intent.FLAG_ACTIVITY_NEW_TASK);

        PendingIntent mainIntent = PendingIntent.getActivity(context, 0,
                notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        CleaningDutyNotificationHelper.postNotification(mainIntent);
    }
}
