package hu.anita.lifeassistant.menu;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.View;
import android.widget.ListView;
import android.widget.RelativeLayout;
import hu.anita.lifeassistant.R;
import hu.anita.lifeassistant.cleaningduty.CleaningDutyFragment;
import hu.anita.lifeassistant.health.HealthFragment;
import hu.anita.lifeassistant.settings.SettingsFragment;

import java.util.ArrayList;
import java.util.List;

public class MenuService {

    private Context context;
    private FragmentManager fragmentManager;
    private ActionBarDrawerToggle menuToggle;
    private DrawerLayout menuLayout;
    private RelativeLayout menuPane;
    private ListView menuView;
    private List<MenuItem> menuItemList;

    public MenuService(Context context, FragmentManager fragmentManager, DrawerLayout menuLayout,
                       RelativeLayout menuPane, ListView menuView) {
        this.context = context;
        this.fragmentManager = fragmentManager;
        this.menuLayout = menuLayout;
        this.menuPane = menuPane;
        this.menuView = menuView;

        menuToggle = new ActionBarDrawerToggle((Activity) context, menuLayout, R.string.app_name, R.string.cleaning_duty_update_minutes) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);

                ((Activity) context).invalidateOptionsMenu();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);

                ((Activity) context).invalidateOptionsMenu();
            }
        };

        menuLayout.addDrawerListener(menuToggle);
    }

    public void populateMenu() {
        menuItemList = new ArrayList<>();
        menuItemList.add(new MenuItem(context.getString(R.string.menu_cleaning), R.drawable.chores, CleaningDutyFragment::new));
        menuItemList.add(new MenuItem(context.getString(R.string.menu_health),  R.drawable.chores, HealthFragment::new));
        menuItemList.add(new MenuItem(context.getString(R.string.menu_tips),  R.drawable.chores, CleaningDutyFragment::new));
        menuItemList.add(new MenuItem(context.getString(R.string.menu_tips),  R.drawable.chores,
            SettingsFragment::new));

        MenuAdapter adapter = new MenuAdapter(context, menuItemList);
        menuView.setAdapter(adapter);

        menuView.setOnItemClickListener((parent, view, position, id) -> selectItemFromDrawer(position));
    }

    public boolean isMenuToggleItemSelected(android.view.MenuItem item) {
        return menuToggle.onOptionsItemSelected(item);
    }

    public void syncToggleState() {
        menuToggle.syncState();
    }

    public void showFragment(final Fragment fragment) {
        fragmentManager.beginTransaction()
            .replace(R.id.fragmentHolder, fragment)
            .commitNow();
    }

    private void selectItemFromDrawer(int position) {
        MenuItem selectedMenuItem = menuItemList.get(position);
        showFragment(selectedMenuItem.getFragment());
        menuView.setItemChecked(position, true);
        //context.setTitle(selectedMenuItem.getTitle());

        menuLayout.closeDrawer(menuPane);
    }
}
