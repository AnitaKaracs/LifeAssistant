package hu.anita.lifeassistant.menu;

import android.app.Activity;
import android.content.Context;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.View;
import android.widget.ListView;
import android.widget.RelativeLayout;
import hu.anita.lifeassistant.R;

import java.util.ArrayList;
import java.util.List;

public class MenuService {

    private Context context;
    private ActionBarDrawerToggle menuToggle;
    private DrawerLayout menuLayout;
    private RelativeLayout menuPane;
    private ListView menuView;
    private List<MenuItem> menuItemList;

    public MenuService(Context context, DrawerLayout menuLayout, RelativeLayout menuPane, ListView menuView) {
        this.context = context;
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
                //Log.d(TAG, "onDrawerClosed: " + getTitle());

                ((Activity) context).invalidateOptionsMenu();
            }
        };

        menuLayout.setDrawerListener(menuToggle);
    }

    public void populateMenu() {
        menuItemList = new ArrayList<>();
        menuItemList.add(new MenuItem(context.getString(R.string.menu_cleaning), R.drawable.chores));
        menuItemList.add(new MenuItem(context.getString(R.string.menu_health),  R.drawable.chores));
        menuItemList.add(new MenuItem(context.getString(R.string.menu_tips),  R.drawable.chores));

        MenuAdapter adapter = new MenuAdapter(context, menuItemList);
        menuView.setAdapter(adapter);

        // Drawer Item click listeners
        menuView.setOnItemClickListener((parent, view, position, id) -> selectItemFromDrawer(position));
    }

    public boolean isMenuToggleItemSelected(android.view.MenuItem item) {
        return menuToggle.onOptionsItemSelected(item);
    }

    public void syncToggleState() {
        menuToggle.syncState();
    }

    private void selectItemFromDrawer(int position) {
        /*Fragment fragment = new PreferencesFragment();

        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.mainContent, fragment)
                .commit();

        mDrawerList.setItemChecked(position, true);
        setTitle(menuItemList.get(position).mTitle);

        // Close the drawer
        menuLayout.closeDrawer(menuPane);*/
    }
}
