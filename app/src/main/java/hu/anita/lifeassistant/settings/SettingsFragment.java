package hu.anita.lifeassistant.settings;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import hu.anita.lifeassistant.R;
import hu.anita.lifeassistant.cleaningduty.db.CleaningDutyDatabase;

import java.util.Objects;

import static android.content.Context.CLIPBOARD_SERVICE;

public class SettingsFragment extends Fragment {
    private Context context;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_settings, container, false);

        Button dbBackupButton = rootView.findViewById(R.id.dbBackupButton);
        Button dbLoadButton = rootView.findViewById(R.id.dbLoadButton);
        EditText dbLoadText = rootView.findViewById(R.id.dbLoadText);

        return rootView;
    }
}
