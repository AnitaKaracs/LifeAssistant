package hu.anita.lifeassistant.cleaningduty;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import hu.anita.lifeassistant.R;
import hu.anita.lifeassistant.cleaningduty.db.CleaningDuty;
import hu.anita.lifeassistant.cleaningduty.db.CleaningDutyDao;
import hu.anita.lifeassistant.cleaningduty.db.CleaningDutyDatabase;

public class CleaningDutyAdapter extends RecyclerView.Adapter<CleaningDutyAdapter.CleaningDutyViewHolder> {
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd. HH:mm");
    private LayoutInflater layoutInflater;
    private CleaningDutyDao cleaningDutyDao;
    private List<CleaningDuty> cleaningDutyList;
    private Context context;

    public CleaningDutyAdapter(Context context) {
        this.layoutInflater = LayoutInflater.from(context);
        this.context = context;
        this.cleaningDutyDao = CleaningDutyDatabase.getDatabase(context).cleaningDutyDao();
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
            holder.cleaningDutyButton.setText(getCleaningDutyLabel(cleaningDuty));
            holder.cleaningDutyButton.setChecked(cleaningDuty.checked);
            holder.cleaningDutyButton.setEnabled(!cleaningDuty.checked);
            holder.cleaningDutyButton.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (isChecked && buttonView.getId() == cleaningDuty.id && !cleaningDuty.checked) {

                    new AlertDialog.Builder(context)
                            .setTitle(R.string.cleaning_duty_dialog_title)
                            .setMessage(context.getString(R.string.cleaning_duty_dialog_message, cleaningDuty.name))
                            .setCancelable(false)
                            .setPositiveButton(R.string.cleaning_duty_dialog_yes, (dialog, whichButton) -> {
                                cleaningDuty.checkedTime = LocalDateTime.now();
                                cleaningDuty.checked = true;
                                cleaningDutyDao.update(cleaningDuty);

                                boolean allDone = cleaningDutyList.stream().allMatch(duty -> duty.checked);
                                if (allDone) {
                                    cleaningDutyDao.resetChecklist();
                                    Toast.makeText(context, R.string.cleaning_duty_dialog_done, Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(context, R.string.cleaning_duty_dialog_yes_toast, Toast.LENGTH_SHORT).show();
                                }
                            })
                            .setNegativeButton(R.string.cleaning_duty_dialog_no,
                                (dialog, whichButton) -> buttonView.setChecked(false))
                            .show();
                }
            });
        }

        holder.cleaningDutyUpdateButton.setOnLongClickListener(view -> {
            DialogFragment dialogFragment = CleaningDutyUpdateFragment.newInstance(cleaningDuty);
            dialogFragment.show(((AppCompatActivity) context).getSupportFragmentManager(), CleaningDutyUpdateFragment.TAG_CLEANING_DUTY_SAVE);
            return false;
        });
    }

    @NonNull
    private String getCleaningDutyLabel(CleaningDuty cleaningDuty) {
        String cleaningDutyLabelPart = "";
        if (cleaningDuty.checked) {
            if (cleaningDuty.checkedTime != null) {
                cleaningDutyLabelPart = context.getString(R.string.cleaning_duty_checked, cleaningDuty.checkedTime.format(formatter));
            }
        } else {
            if (cleaningDuty.timeRequiredMin != null && cleaningDuty.timeRequiredMin > 0) {
                cleaningDutyLabelPart = context.getString(R.string.cleaning_duty_unchecked, cleaningDuty.timeRequiredMin.toString());
            }
        }
        return context.getString(R.string.cleaning_duty_label, cleaningDuty.name, cleaningDutyLabelPart);
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
        private Button cleaningDutyUpdateButton;

        public CleaningDutyViewHolder(View itemView) {
            super(itemView);

            cleaningDutyButton = itemView.findViewById(R.id.cleaningDutyButton);
            cleaningDutyUpdateButton = itemView.findViewById(R.id.cleaningDutyUpdateButton);
        }
    }
}
