package hu.anita.lifeassistant.cleaningduty;

import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;
import hu.anita.lifeassistant.R;
import hu.anita.lifeassistant.cleaningduty.db.CleaningDuty;
import hu.anita.lifeassistant.cleaningduty.db.CleaningDutyDao;
import hu.anita.lifeassistant.cleaningduty.db.CleaningDutyDatabase;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Objects;

public class CleaningDutyAdapter extends RecyclerView.Adapter<CleaningDutyAdapter.CleaningDutyViewHolder> {
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd. HH:mm");
    private LayoutInflater layoutInflater;
    private CleaningDutyDao cleaningDutyDao;
    private List<CleaningDuty> cleaningDutyList;
    private MutableLiveData<CleaningDutyEditMode> cleaningDutyEditMode;
    private Context context;
    private final Animation changeAnimation;

    public CleaningDutyAdapter(Context context) {
        this.layoutInflater = LayoutInflater.from(context);
        this.context = context;
        this.cleaningDutyDao = CleaningDutyDatabase.getDatabase(context).cleaningDutyDao();
        this.changeAnimation = AnimationUtils.loadAnimation(context, R.anim.shake);
    }

    public void setCleaningDutyList(List<CleaningDuty> cleaningDutyList) {
        this.cleaningDutyList = cleaningDutyList;
        checkExpiration();
        notifyDataSetChanged();
    }

    public void setCleaningDutyEditMode(MutableLiveData<CleaningDutyEditMode> cleaningDutyEditMode) {
        this.cleaningDutyEditMode = cleaningDutyEditMode;
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
        if (Objects.nonNull(cleaningDuty)) {
            holder.cleaningDutyButton.setId(cleaningDuty.id);
            holder.cleaningDutyExpirationFlag.setBackgroundColor(Color.WHITE);

            if (cleaningDutyEditMode.getValue() == CleaningDutyEditMode.NONE) {
                holder.cleaningDutyButton.setText(getCleaningDutyLabel(cleaningDuty));
                fillCleaningDutyViewHolder(holder, cleaningDuty);

            } else if (cleaningDutyEditMode.getValue() == CleaningDutyEditMode.UPDATE
                    || cleaningDutyEditMode.getValue() == CleaningDutyEditMode.DELETE) {

                holder.cleaningDutyButton.setText(cleaningDuty.name);
                holder.cleaningDutyButton.setEnabled(true);
                holder.cleaningDutyButton.startAnimation(changeAnimation);
                holder.cleaningDutyButton.setOnCheckedChangeListener((buttonView, isChecked) -> {
                    if (buttonView.getId() == cleaningDuty.id) {
                        if (cleaningDutyEditMode.getValue() == CleaningDutyEditMode.UPDATE) {
                            openCleaningDutyEditWindow(cleaningDuty, updateCleaningDutyPositiveCallback());
                        } else {
                            showDeleteConfirmDialog(cleaningDuty);
                        }
                    }
                });
            }
        }
    }

    public void changeCleaningDutyEditMode(CleaningDutyEditMode cleaningDutyEditMode) {
        this.cleaningDutyEditMode.setValue(cleaningDutyEditMode);

        if (cleaningDutyEditMode == CleaningDutyEditMode.ADD) {
            CleaningDuty newCleaningDuty = new CleaningDuty();
            openCleaningDutyEditWindow(newCleaningDuty, addCleaningDutyCallback(newCleaningDuty));
        } else {
            notifyDataSetChanged();
        }
    }

    private void resetCleaningDutyEditMode() {
        this.cleaningDutyEditMode.setValue(CleaningDutyEditMode.NONE);

        notifyDataSetChanged();
    }

    private void fillCleaningDutyViewHolder(CleaningDutyViewHolder holder, CleaningDuty cleaningDuty) {
        holder.cleaningDutyButton.setChecked(cleaningDuty.checked);
        holder.cleaningDutyButton.setEnabled(!cleaningDuty.checked);

        if (!cleaningDuty.checked && Objects.nonNull(cleaningDuty.latestCheck) && Objects.nonNull(cleaningDuty.expiration)) {
            long daysFromLatestCheck = ChronoUnit.DAYS.between(cleaningDuty.latestCheck, LocalDateTime.now());
            float cleaningIsNeededPercent = daysFromLatestCheck / (float) cleaningDuty.expiration * 100;

            int cleaningIsNeededColor = defineCleaningIsNeededColor(cleaningIsNeededPercent);
            if (cleaningIsNeededColor != 0) {
                holder.cleaningDutyExpirationFlag.setBackgroundColor(cleaningIsNeededColor);
            }
        }

        holder.cleaningDutyButton.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked && buttonView.getId() == cleaningDuty.id && !cleaningDuty.checked) {
                showOnCheckConfirmDialog(cleaningDuty, buttonView);
            }
        });
    }

    private void showOnCheckConfirmDialog(CleaningDuty cleaningDuty, CompoundButton buttonView) {
        new AlertDialog.Builder(context)
                .setTitle(R.string.cleaning_duty_dialog_title)
                .setMessage(context.getString(R.string.cleaning_duty_on_check_confirm_message, cleaningDuty.name))
                .setCancelable(false)
                .setPositiveButton(R.string.cleaning_duty_dialog_yes, (dialog, whichButton) -> {
                    cleaningDuty.checkedTime = LocalDateTime.now();
                    cleaningDuty.latestCheck = LocalDateTime.now();
                    cleaningDuty.checked = true;
                    cleaningDutyDao.update(cleaningDuty);

                    Toast.makeText(context, R.string.cleaning_duty_dialog_yes_toast, Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton(R.string.cleaning_duty_dialog_no,
                    (dialog, whichButton) -> {
                        buttonView.setChecked(false);
                        resetCleaningDutyEditMode();
                    })
                .show();
    }

    private void showDeleteConfirmDialog(CleaningDuty cleaningDuty) {
        new AlertDialog.Builder(context)
                .setTitle(R.string.cleaning_duty_dialog_title)
                .setMessage(context.getString(R.string.cleaning_duty_delete_confirm_message, cleaningDuty.name))
                .setCancelable(false)
                .setPositiveButton(R.string.cleaning_duty_dialog_yes, (dialog, whichButton) -> {
                    cleaningDutyDao.delete(cleaningDuty);

                    cleaningDutyList.remove(cleaningDuty);
                    resetCleaningDutyEditMode();

                    Toast.makeText(context, context.getString(R.string.cleaning_duty_delete_confirm_yes, cleaningDuty.name), Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton(R.string.cleaning_duty_dialog_no,
                        (dialog, whichButton) -> resetCleaningDutyEditMode())
                .show();
    }
    private void openCleaningDutyEditWindow(CleaningDuty cleaningDuty, Runnable callback) {
        DialogFragment dialogFragment = CleaningDutyUpdateFragment.newInstance(cleaningDuty, callback);
        dialogFragment.show(((AppCompatActivity) context).getSupportFragmentManager(), CleaningDutyUpdateFragment.TAG_CLEANING_DUTY_SAVE);
    }

    private Runnable addCleaningDutyCallback(CleaningDuty cleaningDuty) {
        return (Runnable & Serializable) () -> {
            this.cleaningDutyList.add(cleaningDuty);
            changeCleaningDutyEditMode(CleaningDutyEditMode.NONE);
        };
    }

    private Runnable updateCleaningDutyPositiveCallback() {
        return (Runnable & Serializable) () -> changeCleaningDutyEditMode(CleaningDutyEditMode.NONE);
    }

    private int defineCleaningIsNeededColor(float cleaningIsNeededPercent) {
        int cleaningIsNeededColor = 0;
        if (cleaningIsNeededPercent >= 200) {
            cleaningIsNeededColor = Color.rgb(255, 51, 51);
        } else if (cleaningIsNeededPercent >= 150) {
            cleaningIsNeededColor = Color.rgb(255, 102, 102);
        } else if (cleaningIsNeededPercent >= 100) {
            cleaningIsNeededColor = Color.rgb(255, 153, 153);
        } else if (cleaningIsNeededPercent >= 75) {
            cleaningIsNeededColor = Color.rgb(255, 179, 179);
        } else if (cleaningIsNeededPercent >= 50) {
            cleaningIsNeededColor = Color.rgb(255, 204, 204);
        } else if (cleaningIsNeededPercent >= 30) {
            cleaningIsNeededColor = Color.rgb(255, 230, 230);
        }
        return cleaningIsNeededColor;
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

    private void checkExpiration() {
        this.cleaningDutyList.stream()
            .filter(cleaningDuty -> cleaningDuty.checkedTime != null)
            .filter(cleaningDuty ->
                cleaningDuty.checkedTime.toLocalDate().compareTo(LocalDate.now().minusDays(cleaningDuty.expiration)) < 0)
            .forEach(cleaningDuty -> {
                cleaningDuty.checked = false;
                cleaningDuty.checkedTime = null;
                cleaningDutyDao.update(cleaningDuty);
            });
    }

    static class CleaningDutyViewHolder extends RecyclerView.ViewHolder {
        private CheckBox cleaningDutyButton;
        private TextView cleaningDutyExpirationFlag;

        public CleaningDutyViewHolder(View itemView) {
            super(itemView);

            cleaningDutyButton = itemView.findViewById(R.id.cleaningDutyButton);
            cleaningDutyExpirationFlag = itemView.findViewById(R.id.cleaningDutyExpirationFlag);
        }
    }
}
