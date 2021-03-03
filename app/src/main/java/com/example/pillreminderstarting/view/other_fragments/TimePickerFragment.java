package com.example.pillreminderstarting.view.other_fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.DialogFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.TimePicker;

import com.example.pillreminderstarting.R;
import com.google.android.material.button.MaterialButton;

public class TimePickerFragment extends DialogFragment {

    public static final int TIME_PICKER_REQUEST_CODE = 1;
    public static final String HOUR_FROM_TIME_PICKER = "hour";
    public static final String MINUTE_FROM_TIME_PICKER = "minute";
    private TimePicker mTimePicker;
    private MaterialButton mOkButton;

    public static TimePickerFragment newInstance() {
        TimePickerFragment fragment = new TimePickerFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View view = inflater.inflate(R.layout.time_picker_dialog, null, true);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(view);
        findViews(view);
        setListeners();
        AlertDialog dialog = builder.create();
        return dialog;
    }

    private void findViews(View view) {
        mTimePicker = view.findViewById(R.id.time_picker);
        mOkButton = view.findViewById(R.id.ok_button);
    }

    private void setListeners() {
        mOkButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                int hour = mTimePicker.getHour();
                int minute = mTimePicker.getMinute();
                Intent intent = new Intent();
                intent.putExtra(HOUR_FROM_TIME_PICKER, hour);
                intent.putExtra(MINUTE_FROM_TIME_PICKER, minute);
                getTargetFragment().onActivityResult(TIME_PICKER_REQUEST_CODE, Activity.RESULT_OK, intent);
                dismiss();
            }
        });
    }
}