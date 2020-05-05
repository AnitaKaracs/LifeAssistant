package hu.anita.lifeassistant.cleaningduty;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import hu.anita.lifeassistant.R;

public class CleaningDutyFragment extends Fragment {
    private CleaningDutyAdapter cleaningDutyAdapter;
    private Context context;
    private ImageButton editButton;
    private PopupMenu editOptions;

    public static CleaningDutyFragment newInstance() {
        return new CleaningDutyFragment();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
        cleaningDutyAdapter = new CleaningDutyAdapter(context);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initData();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_cleaning_duties, container, false);

        RecyclerView recyclerView = rootView.findViewById(R.id.recyclerview_cleaningduty);
        recyclerView.setAdapter(cleaningDutyAdapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(context, DividerItemDecoration.VERTICAL));
        recyclerView.setLayoutManager(new LinearLayoutManager(context));

        editButton = rootView.findViewById(R.id.cleaningDutyListEditButton);
        editOptions = new PopupMenu(context, editButton);
        editOptions.inflate(R.menu.cleaning_duty_edit_popup_menu);
        editOptions.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()) {
                case R.id.cleaning_duty_edit_add:
                    changeCleaningDutyEditMode(CleaningDutyEditMode.ADD);
                    return true;
                case R.id.cleaning_duty_edit_modify:
                    changeCleaningDutyEditMode(CleaningDutyEditMode.UPDATE);
                    return true;
                case R.id.cleaning_duty_edit_delete:
                    changeCleaningDutyEditMode(CleaningDutyEditMode.DELETE);
                    return true;
                default:
                    return false;
            }
        });

        editButton.setOnClickListener(v -> editOptions.show());

        return rootView;
    }

    private void changeCleaningDutyEditMode(CleaningDutyEditMode cleaningDutyEditMode) {
        cleaningDutyAdapter.changeCleaningDutyEditMode(cleaningDutyEditMode);
    }

    private void setCleaningDutyEditButton(CleaningDutyEditMode cleaningDutyEditMode) {
        if (cleaningDutyEditMode == CleaningDutyEditMode.NONE) {
            editButton.setImageResource(R.drawable.chores_edit);
            editButton.setOnClickListener(v -> editOptions.show());
        } else {
            editButton.setImageResource(R.drawable.cancel);
            editButton.setOnClickListener(v -> {
                changeCleaningDutyEditMode(CleaningDutyEditMode.NONE);
                editButton.setImageResource(R.drawable.chores_edit);
                editButton.setOnClickListener(view -> editOptions.show());
            });
        }
    }

    private void initData() {
        CleaningDutyViewModel cleaningDutyViewModel = ViewModelProviders.of(this).get(CleaningDutyViewModel.class);

        cleaningDutyViewModel.getCleaningDuties().observe(this,
                cleaningDuties -> cleaningDutyAdapter.setCleaningDutyList(cleaningDuties));

        MutableLiveData<CleaningDutyEditMode> cleaningDutyEditModeLiveData = cleaningDutyViewModel.getCleaningDutyEditMode();
        cleaningDutyEditModeLiveData.observe(this, this::setCleaningDutyEditButton);
        cleaningDutyAdapter.setCleaningDutyEditMode(cleaningDutyEditModeLiveData);
    }
}
