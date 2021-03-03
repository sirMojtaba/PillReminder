package com.example.pillreminderstarting.view.ui.setReminer;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.example.pillreminderstarting.repository.AppRepository;

public class SetReminderViewModel extends AndroidViewModel {

    private Context mContext;
    private AppRepository mRepository;

    public SetReminderViewModel(@NonNull Application application) {
        super(application);
        mContext = application.getApplicationContext();
        mRepository = AppRepository.getInstance(mContext);
    }

    public AppRepository getRepository() {
        return mRepository;
    }
}
