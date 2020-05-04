package hu.anita.lifeassistant.menu;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import hu.anita.lifeassistant.R;

import java.util.List;

public class MenuAdapter extends BaseAdapter {

    private Context context;
    private List<MenuItem> menuItems;
    private LayoutInflater layoutInflater;

    public MenuAdapter(Context context, List<MenuItem> menuItems) {
        this.layoutInflater = LayoutInflater.from(context);
        this.context = context;
        this.menuItems = menuItems;
    }

    @Override
    public int getCount() {
        return menuItems.size();
    }

    @Override
    public Object getItem(int position) {
        return menuItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;

        if (view == null) {
            view = layoutInflater.inflate(R.layout.menu_item, parent, false);
        }

        TextView titleView = view.findViewById(R.id.title);
        ImageView iconView = view.findViewById(R.id.icon);

        titleView.setText( menuItems.get(position).getTitle() );
        iconView.setImageResource(menuItems.get(position).getIcon());

        return view;
    }

}
