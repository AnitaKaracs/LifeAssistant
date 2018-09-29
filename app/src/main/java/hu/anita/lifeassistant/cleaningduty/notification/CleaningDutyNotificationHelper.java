package hu.anita.lifeassistant.cleaningduty.notification;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Color;

import java.time.LocalDate;
import java.util.List;

import hu.anita.lifeassistant.R;
import hu.anita.lifeassistant.cleaningduty.db.CleaningDuty;
import hu.anita.lifeassistant.cleaningduty.db.CleaningDutyDao;
import hu.anita.lifeassistant.cleaningduty.db.CleaningDutyDatabase;

public class CleaningDutyNotificationHelper extends ContextWrapper {
    public static final String CHANNEL_ID = "CHANNEL_ID";
    public static final String CHANNEL_NAME = "Takarítási emlékeztető";
    public static final int NOTIFICATION_ID = 101;
    private static CleaningDutyNotificationHelper instance;
    private NotificationManager notificationManager;
    private CleaningDutyDao cleaningDutyDao;

    private CleaningDutyNotificationHelper(Context context) {
        super(context);

        this.cleaningDutyDao = CleaningDutyDatabase.getDatabase(context).cleaningDutyDao();

        NotificationChannel notificationChannel
                = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH);

        notificationChannel.enableLights(false);
        notificationChannel.enableVibration(true);
        notificationChannel.setLightColor(Color.YELLOW);
        notificationChannel.setShowBadge(false);
        notificationChannel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);

        getManager().createNotificationChannel(notificationChannel);
    }

    public static void createInstance(Context base) {
        if (instance == null) {
            instance = new CleaningDutyNotificationHelper(base);
        }
    }

    private static Notification buildNotification(PendingIntent intent) {
        Notification.Builder builder = new Notification.Builder(instance, CHANNEL_ID);
        builder.setContentTitle(instance.getApplicationContext().getString(R.string.cleaning_duty_reminder_title));
        builder.setContentText(instance.getApplicationContext().getString(R.string.cleaning_duty_reminder_message));
        builder.setAutoCancel(true);
        builder.setContentIntent(intent);
        builder.setSmallIcon(R.drawable.chores_notification);
        return builder.build();
    }

    public static void postNotification(PendingIntent intent) {
        if (instance.hasUndoneChoresToday()) {
            instance.getManager().notify(NOTIFICATION_ID, buildNotification(intent));
        }
    }

    private boolean hasUndoneChoresToday() {
        List<CleaningDuty> allDoneChores = cleaningDutyDao.getDoneChores();
        return allDoneChores.stream()
                .noneMatch(chore -> chore.checkedTime != null &&
                                    chore.checkedTime.toLocalDate().isEqual(LocalDate.now()));
    }

    private NotificationManager getManager() {
        if (notificationManager == null) {
            notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        }
        return notificationManager;
    }
}
