package hu.anita.lifeassistant.cleaningduty;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import java.util.List;

import hu.anita.lifeassistant.R;
import hu.anita.lifeassistant.db.CleaningDuty;

public class CleaningDutyAdapter extends RecyclerView.Adapter<CleaningDutyAdapter.CleaningDutyViewHolder> {
    private LayoutInflater layoutInflater;
    private List<CleaningDuty> cleaningDutyList;
    private Context context;

    public CleaningDutyAdapter(Context context) {
        this.layoutInflater = LayoutInflater.from(context);
        this.context = context;
    }

    public void setCleaningDutyList(List<CleaningDuty> cleaningDutyList) {
        this.cleaningDutyList = cleaningDutyList;
        notifyDataSetChanged();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public CleaningDutyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View itemView = layoutInflater.inflate(R.layout.cleaning_duty, parent, false);
        return new CleaningDutyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(CleaningDutyViewHolder holder, int position) {
        if (cleaningDutyList == null) {
            return;
        }

        final CleaningDuty cleaningDuty = cleaningDutyList.get(position);
        if (cleaningDuty != null) {
            holder.cleaningDutyButton.setId(cleaningDuty.id);
            holder.cleaningDutyButton.setText(cleaningDuty.name);
            holder.cleaningDutyButton.setEnabled(!cleaningDuty.checked);
            /*holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    *//*DialogFragment dialogFragment = MovieSaveDialogFragment.newInstance(movie.title, directorFullName);
                    dialogFragment.show(((AppCompatActivity) context).getSupportFragmentManager(), TAG_DIALOG_MOVIE_SAVE);*//*
                }
            });*/
        }
    }

    @Override
    public int getItemCount() {
        if (cleaningDutyList == null) {
            return 0;
        }
        return cleaningDutyList.size();
    }
    
    static class CleaningDutyViewHolder extends RecyclerView.ViewHolder {
        private CheckBox cleaningDutyButton;

        public CleaningDutyViewHolder(View itemView) {
            super(itemView);

            cleaningDutyButton = itemView.findViewById(R.id.cleaningDutyButton);
        }
    }
}
