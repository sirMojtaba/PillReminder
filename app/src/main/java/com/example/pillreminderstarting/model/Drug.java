package com.example.pillreminderstarting.model;

import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;


public class Drug {

    private String mName;
    private String mDescription;
    private int mTypeId;
    private int mNumber;
    private int mPeriod;

    public Drug(String name, String description, int typeId, int number, int period) {
        mName = name;
        mDescription = description;
        mTypeId = typeId;
        mNumber = number;
        mPeriod = period;
    }

    public int getPeriod() {
        return mPeriod;
    }

    public void setPeriod(int period) {
        mPeriod = period;
    }

    public int getTypeId() {
        return mTypeId;
    }

    public void setTypeId(int typeId) {
        mTypeId = typeId;
    }


    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        mDescription = description;
    }

    public int getNumber() {
        return mNumber;
    }

    public void setNumber(int number) {
        mNumber = number;
    }

}
