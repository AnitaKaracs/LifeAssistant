package hu.anita.lifeassistant.cleaningduty;

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

import hu.anita.lifeassistant.R;

public class CleaningDutyFragment extends Fragment {
    private CleaningDutyAdapter cleaningDutyAdapter;
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
        RecyclerView recyclerView = (RecyclerView) inflater.inflate(R.layout.fragment_cleaning_duties, container, false);
        recyclerView.setAdapter(cleaningDutyAdapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(context, DividerItemDecoration.VERTICAL));
        recyclerView.setLayoutManager(new LinearLayoutManager(context));

        return recyclerView;
    }

    private void initData() {
        CleaningDutyViewModel cleaningDutyViewModel = ViewModelProviders.of(this).get(CleaningDutyViewModel.class);
        cleaningDutyViewModel.getCleaningDuties().observe(this,
                cleaningDuties -> cleaningDutyAdapter.setCleaningDutyList(cleaningDuties));
    }
}
