package hu.anita.lifeassistant.cleaningduty;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import java.util.List;

import hu.anita.lifeassistant.cleaningduty.db.CleaningDuty;
import hu.anita.lifeassistant.cleaningduty.db.CleaningDutyDao;
import hu.anita.lifeassistant.cleaningduty.db.CleaningDutyDatabase;

public class CleaningDutyViewModel extends AndroidViewModel {
    private CleaningDutyDao cleaningDutyDao;
    private final LiveData<List<CleaningDuty>> cleaningDuties;

    public CleaningDutyViewModel(@NonNull Application application) {
        super(application);
        cleaningDutyDao = CleaningDutyDatabase.getDatabase(application).cleaningDutyDao();
        cleaningDuties = cleaningDutyDao.getAll();
    }

    public LiveData<List<CleaningDuty>> getCleaningDuties() {
        return cleaningDuties;
    }

    public void updateCleaningDuty(CleaningDuty cleaningDuty) {
        cleaningDutyDao.update(cleaningDuty);
    }
}
