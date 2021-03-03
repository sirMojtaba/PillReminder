package com.example.pillreminderstarting.repository;

import android.content.Context;

import androidx.lifecycle.MutableLiveData;

import com.example.pillreminderstarting.model.Drug;
import com.example.pillreminderstarting.model.Reminder;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class AppRepository {

    private static AppRepository sAppRepository;
    private static Context sContext;

    public static AppRepository getInstance(Context context) {
        if(sAppRepository == null){
            sAppRepository = new AppRepository();
            sContext = context;
        }
        return sAppRepository;
    }

    private MutableLiveData<List<Reminder>> mLiveReminderList = new MutableLiveData<>();
    private ArrayList<Reminder> mReminderArrayList = new ArrayList<>();

    private AppRepository() {
    }

    public MutableLiveData<List<Reminder>> getReminderArrayList() {
        mLiveReminderList.setValue(mReminderArrayList);
        return mLiveReminderList;
    }

    public void addReminder(Reminder reminder){
        mReminderArrayList.add(reminder);
        if(sContext.getMainLooper().getThread() == Thread.currentThread()) {
            mLiveReminderList.setValue(mReminderArrayList);
        } else {
            mLiveReminderList.postValue(mReminderArrayList);
        }
    }


    public void deleteReminder(UUID id){
        for (Reminder reminder: mReminderArrayList) {

            if(reminder.getReminderId() == id){
                mReminderArrayList.remove(reminder);
                if(sContext.getMainLooper().getThread() == Thread.currentThread()) {
                    mLiveReminderList.setValue(mReminderArrayList);
                } else {
                    mLiveReminderList.postValue(mReminderArrayList);
                }
            }
        }
    }

    public void updateReminder(Reminder inputReminder){
        for (Reminder reminder: mReminderArrayList) {
            if (reminder.getReminderId() == inputReminder.getReminderId()){
                reminder.getDrug().setNumber(inputReminder.getDrug().getNumber());
                reminder.setDate(inputReminder.getDate());
                if(sContext.getMainLooper().getThread() == Thread.currentThread()) {
                    mLiveReminderList.setValue(mReminderArrayList);
                } else {
                    mLiveReminderList.postValue(mReminderArrayList);
                }
            }
        }
    }
}
