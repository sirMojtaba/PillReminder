package com.example.pillreminderstarting.model;

import java.util.Date;
import java.util.UUID;

public class Reminder {

    private UUID mReminderId;
    private Date mDate;
    private Drug mDrug;

    public Reminder(Date date, Drug drug) {
        mReminderId = UUID.randomUUID();
        mDate = date;
        mDrug = drug;
    }

    public UUID getReminderId() {
        return mReminderId;
    }

    public Date
    getDate() {
        return mDate;
    }

    public void setDate(Date date) {
        mDate = date;
    }

    public Drug getDrug() {
        return mDrug;
    }

    public void setDrug(Drug drug) {
        mDrug = drug;
    }
}
