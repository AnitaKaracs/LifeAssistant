package hu.anita.lifeassistant.menu;

public class MenuItem {
    private final String title;
    private final int icon;

    public MenuItem(String title, int icon) {
        this.title = title;
        this.icon = icon;
    }

    public String getTitle() {
        return title;
    }

    public int getIcon() {
        return icon;
    }
}
