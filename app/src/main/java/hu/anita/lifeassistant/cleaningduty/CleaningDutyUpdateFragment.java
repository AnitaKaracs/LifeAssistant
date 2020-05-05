package hu.anita.lifeassistant.cleaningduty;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.EditText;
import hu.anita.lifeassistant.R;
import hu.anita.lifeassistant.cleaningduty.db.CleaningDuty;
import hu.anita.lifeassistant.cleaningduty.db.CleaningDutyDao;
import hu.anita.lifeassistant.cleaningduty.db.CleaningDutyDatabase;

import java.io.Serializable;
import java.time.LocalDateTime;

public class CleaningDutyUpdateFragment extends DialogFragment {
    private CleaningDutyDao cleaningDutyDao;
    private Context context;
    private CleaningDuty updatableCleaningDuty;
    private Runnable resetEditButtonCallback;

    private static final String UPDATABLE_CLEANING_DUTY = "updatableCleaningDuty";
    private static final String CALLBACK = "callback";
    public static final String TAG_CLEANING_DUTY_SAVE = "dialog_cleaning_duty_save";

    public static CleaningDutyUpdateFragment newInstance(CleaningDuty updatableCleaningDuty, Runnable callback) {
        CleaningDutyUpdateFragment fragment = new CleaningDutyUpdateFragment();

        Bundle args = new Bundle();
        args.putSerializable(UPDATABLE_CLEANING_DUTY, updatableCleaningDuty);
        args.putSerializable(CALLBACK, (Runnable & Serializable) callback);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
        this.cleaningDutyDao = CleaningDutyDatabase.getDatabase(context).cleaningDutyDao();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = getArguments();
        updatableCleaningDuty = (CleaningDuty) args.getSerializable(UPDATABLE_CLEANING_DUTY);
        resetEditButtonCallback = (Runnable & Serializable) args.getSerializable(CALLBACK);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        View view = getActivity().getLayoutInflater().inflate(R.layout.cleaning_duty_update_dialog, null);
        final EditText nameField = view.findViewById(R.id.cleaningDutyUpdateName);
        final EditText timeRequiredField = view.findViewById(R.id.cleaningDutyUpdateTimeRequired);
        final EditText detailsField = view.findViewById(R.id.cleaningDutyUpdateDetails);
        final EditText expirationField = view.findViewById(R.id.cleaningDutyUpdateExpiration);

        if (updatableCleaningDuty != null) {
            nameField.setText(updatableCleaningDuty.name);
            if (updatableCleaningDuty.timeRequiredMin != null) {
                timeRequiredField.setText(String.valueOf(updatableCleaningDuty.timeRequiredMin));
            } else {
                timeRequiredField.setText("1");
            }
            timeRequiredField.setSelection(0, timeRequiredField.getText().toString().length());

            if (updatableCleaningDuty.expiration != null) {
                expirationField.setText(String.valueOf(updatableCleaningDuty.expiration));
            } else {
                expirationField.setText("14");
            }
            detailsField.setText(updatableCleaningDuty.details);
        }

        alertDialogBuilder.setView(view)
                .setTitle(R.string.cleaning_duty_update_title)
                .setPositiveButton(R.string.cleaning_duty_update_save,
                        (dialog, which) -> {
                            updatableCleaningDuty.name = nameField.getText().toString();
                            updatableCleaningDuty.timeRequiredMin = Integer.valueOf(timeRequiredField.getText().toString());
                            updatableCleaningDuty.details = detailsField.getText().toString();
                            updatableCleaningDuty.expiration = Integer.valueOf(expirationField.getText().toString());
                            if (updatableCleaningDuty.id == 0) {
                                updatableCleaningDuty.latestCheck = LocalDateTime.now();
                                cleaningDutyDao.insert(updatableCleaningDuty);
                            } else {
                                cleaningDutyDao.update(updatableCleaningDuty);
                            }

                            resetEditButtonCallback.run();
                        })
                .setNegativeButton(R.string.cleaning_duty_update_cancel,
                        (dialog, which) -> {
                            dialog.cancel();
                            resetEditButtonCallback.run();
                        });

        return alertDialogBuilder.create();
    }
}
