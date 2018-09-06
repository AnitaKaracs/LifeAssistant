package hu.anita.lifeassistant.cleaningduty;

import android.arch.lifecycle.Observer;
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

import java.util.List;

import hu.anita.lifeassistant.R;
import hu.anita.lifeassistant.db.CleaningDuty;

public class CleaningDutyFragment extends Fragment {
    private CleaningDutyAdapter cleaningDutyAdapter;
    private CleaningDutyViewModel cleaningDutyViewModel;
    private Context context;

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
        View view = inflater.inflate(R.layout.fragment_cleaning_duties, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerview_cleaningduty);
        recyclerView.setAdapter(cleaningDutyAdapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(context, DividerItemDecoration.VERTICAL));
        recyclerView.setLayoutManager(new LinearLayoutManager(context));

        return view;
    }

    private void initData() {
        cleaningDutyViewModel = ViewModelProviders.of(this).get(CleaningDutyViewModel.class);
        cleaningDutyViewModel.getCleaningDuties().observe(this, new Observer<List<CleaningDuty>>() {
            @Override
            public void onChanged(@Nullable List<CleaningDuty> cleaningDuties) {
                cleaningDutyAdapter.setCleaningDutyList(cleaningDuties);
            }
        });
    }
}
