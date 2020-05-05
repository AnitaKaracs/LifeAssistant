package hu.anita.lifeassistant.cleaningduty;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;
import hu.anita.lifeassistant.cleaningduty.db.CleaningDuty;
import hu.anita.lifeassistant.cleaningduty.db.CleaningDutyDao;
import hu.anita.lifeassistant.cleaningduty.db.CleaningDutyDatabase;

import java.util.List;

public class CleaningDutyViewModel extends AndroidViewModel {
    private final LiveData<List<CleaningDuty>> cleaningDuties;
    private final MutableLiveData<CleaningDutyEditMode> cleaningDutyEditMode;

    public CleaningDutyViewModel(@NonNull Application application) {
        super(application);
        CleaningDutyDao cleaningDutyDao = CleaningDutyDatabase.getDatabase(application).cleaningDutyDao();
        cleaningDuties = cleaningDutyDao.getAll();
        cleaningDutyEditMode = new MutableLiveData<>();
        cleaningDutyEditMode.postValue(CleaningDutyEditMode.NONE);
    }

    public LiveData<List<CleaningDuty>> getCleaningDuties() {
        return cleaningDuties;
    }

    public MutableLiveData<CleaningDutyEditMode> getCleaningDutyEditMode() {
        return cleaningDutyEditMode;
    }
}
