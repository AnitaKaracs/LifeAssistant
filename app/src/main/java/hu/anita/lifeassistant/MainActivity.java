package hu.anita.lifeassistant;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import hu.anita.lifeassistant.cleaningduty.CleaningDutyFragment;
import hu.anita.lifeassistant.cleaningduty.notification.CleaningDutyNotificationHelper;
import hu.anita.lifeassistant.cleaningduty.notification.CleaningDutyNotificationReceiver;
import hu.anita.lifeassistant.menu.MenuService;

import java.util.Calendar;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    private Fragment shownFragment;
    private MenuService menuService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CleaningDutyNotificationHelper.createInstance(this);
        setContentView(R.layout.activity_main);
        setTitle(R.string.app_name);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        menuService = new MenuService(
                this,
                findViewById(R.id.drawerLayout),
                findViewById(R.id.menuPane),
                findViewById(R.id.menuList));

        menuService.populateMenu();

        if (savedInstanceState == null) {
            showFragment(CleaningDutyFragment.newInstance());
            scheduleCleaningDutyNotification();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (menuService.isMenuToggleItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        menuService.syncToggleState();
    }

    private void showFragment(final Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentHolder, fragment)
                .commitNow();
        shownFragment = fragment;
    }

    private void scheduleCleaningDutyNotification() {
        Intent notifyIntent = new Intent(this, CleaningDutyNotificationReceiver.class);
        PendingIntent pendingIntent
                = PendingIntent.getBroadcast(this, 0, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 20);
        calendar.set(Calendar.MINUTE, 30);
        calendar.set(Calendar.SECOND, 0);

        AlarmManager alarmManager = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
        if (alarmManager != null) {
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                    AlarmManager.INTERVAL_DAY, pendingIntent);
        }
    }

    //TODO details megjelenítése a name alatt kicsi betűvel
    //TODO CRUD cleaning
    //TODO funkciók közti váltás a menüben, health és tips implementálása
    //TODO health-en belül vérnyomás adatok rögzítése, citológiák ideje, fogászat, kórtörténet testrészenként?
    //TODO DB backup?
}
