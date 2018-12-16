package hu.anita.lifeassistant;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import java.util.Calendar;

import hu.anita.lifeassistant.cleaningduty.CleaningDutyFragment;
import hu.anita.lifeassistant.cleaningduty.notification.CleaningDutyNotificationHelper;
import hu.anita.lifeassistant.cleaningduty.notification.CleaningDutyNotificationReceiver;

public class MainActivity extends AppCompatActivity {
    private Fragment shownFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CleaningDutyNotificationHelper.createInstance(this);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            showFragment(CleaningDutyFragment.newInstance());
            scheduleCleaningDutyNotification();
        }
    }

    private void showFragment(final Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragmentHolder, fragment);
        fragmentTransaction.commitNow();
        shownFragment = fragment;
    }

    private void scheduleCleaningDutyNotification() {
        Intent notifyIntent = new Intent(this, CleaningDutyNotificationReceiver.class);
        PendingIntent pendingIntent
                = PendingIntent.getBroadcast(this, 0, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 21);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);

        AlarmManager alarmManager = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
        if (alarmManager != null) {
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                    AlarmManager.INTERVAL_DAY, pendingIntent);
        }
    }

    //TODO details megjelenítése a name alatt kicsi betűvel
    //TODO menü kialakítása: health, cleaning, napi tanács
    //TODO health-en belül vérnyomás adatok rögzítése, citológiák ideje, kórtörténet testrészenként?
}
