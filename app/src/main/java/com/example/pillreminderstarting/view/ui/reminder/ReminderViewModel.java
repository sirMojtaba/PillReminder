package com.example.pillreminderstarting.view.ui.reminder;

import android.app.Application;
import android.appwidget.AppWidgetProvider;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import com.example.pillreminderstarting.model.Reminder;
import com.example.pillreminderstarting.repository.AppRepository;
import com.example.pillreminderstarting.workers.NotificationWorker;

import java.util.List;

public class ReminderViewModel extends AndroidViewModel {

    private Context mContext;
    private LiveData<List<Reminder>> mLiveReminderList;
    private AppRepository mRepository;

    public ReminderViewModel(@NonNull Application application) {
        super(application);
        mContext = application.getApplicationContext();
        mRepository = AppRepository.getInstance(mContext);
        mLiveReminderList = mRepository.getReminderArrayList();
    }

    public LiveData<List<Reminder>> getLiveReminderList() {
        return mLiveReminderList;
    }

    public void setWorkerManager() {
        OneTimeWorkRequest.Builder builder = new OneTimeWorkRequest.Builder(NotificationWorker.class);
        WorkManager workManager = WorkManager.getInstance(getApplication().getApplicationContext());
        workManager.enqueue(builder.build());
    }
}
