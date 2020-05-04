package hu.anita.lifeassistant.menu;

import android.support.v4.app.Fragment;

import java.util.function.Supplier;

public class MenuItem {
    private final String title;
    private final int icon;
    private final Supplier<Fragment> fragment;

    public MenuItem(String title, int icon, Supplier<Fragment> fragment) {
        this.title = title;
        this.icon = icon;
        this.fragment = fragment;
    }

    public String getTitle() {
        return title;
    }

    public int getIcon() {
        return icon;
    }

    public Fragment getFragment() {
        return fragment.get();
    }
}
