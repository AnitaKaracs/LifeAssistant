package hu.anita.lifeassistant;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import java.util.Calendar;

import hu.anita.lifeassistant.cleaningduty.CleaningDutyFragment;
import hu.anita.lifeassistant.cleaningduty.notification.CleaningDutyNotificationHelper;
import hu.anita.lifeassistant.cleaningduty.notification.CleaningDutyNotificationReceiver;

public class MainActivity extends AppCompatActivity {
    private static boolean active;
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

        if (getIntent().getBooleanExtra("crash", false)) {
            Toast.makeText(this, "App restarted after crash", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        active = true;
    }

    @Override
    protected void onStop() {
        super.onStop();
        active = false;
    }

    public static boolean isStopped() {
        return !active;
    }

    private void showFragment(final Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragmentHolder, fragment);
        fragmentTransaction.commitNow();
        shownFragment = fragment;
    }

    private void scheduleCleaningDutyNotification() {
        /*if (isStopped() || isDestroyed()) {
            startActivity(new Intent(this, MainActivity.class));
        }*/
        Intent notifyIntent = new Intent(this, CleaningDutyNotificationReceiver.class);
        PendingIntent pendingIntent
                = PendingIntent.getBroadcast(this, 0, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 12); //TODO este 9
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);

        AlarmManager alarmManager = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
        if (alarmManager != null) {
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                    1000 * 60 /*TODO AlarmManager.INTERVAL_DAY*/, pendingIntent);
        }
    }

    //TODO menü kialakítása: health, cleaning, napi tanács
    //TODO health-en belül vérnyomás adatok rögzítése, citológiák ideje, kórtörténet testrészenként?
}
