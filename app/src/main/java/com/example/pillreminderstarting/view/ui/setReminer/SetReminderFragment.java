package com.example.pillreminderstarting.view.ui.setReminer;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import androidx.work.WorkRequest;

import android.util.JsonWriter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.example.pillreminderstarting.R;
import com.example.pillreminderstarting.adapter.IntRecyclerView;
import com.example.pillreminderstarting.adapter.StringRecyclerView;
import com.example.pillreminderstarting.databinding.CustomToastLayoutBinding;
import com.example.pillreminderstarting.databinding.FragmentSetReminderBinding;
import com.example.pillreminderstarting.model.Drug;
import com.example.pillreminderstarting.model.Reminder;
import com.example.pillreminderstarting.view.other_fragments.DrugTypeDialogFragment;
import com.example.pillreminderstarting.view.other_fragments.TimePickerFragment;
import com.example.pillreminderstarting.workers.NotificationWorker;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import ir.hamsaa.persiandatepicker.Listener;
import ir.hamsaa.persiandatepicker.PersianDatePickerDialog;
import ir.hamsaa.persiandatepicker.util.PersianCalendar;

public class SetReminderFragment extends Fragment
        implements StringRecyclerView.OnStringRowClickListener, IntRecyclerView.OnIntegerRowClickListener {

    public static final int TYPE_REQUEST_CODE = 0;
    public static final int TIME_REQUEST_CODE = 1;

    public static final String DRUG_TYPE_DIALOG_FRAGMENT = "drug type dialog fragment";
    public static final String TIME_PICKER = "time picker";
    public static final String TAG = "tag3";
    public static final String STRING_JSON_REMINDER = "string json reminder";

    private SetReminderViewModel mViewModel;

    private FragmentSetReminderBinding mBinding;
    private CustomToastLayoutBinding mCustomToastBinding;

    private int mTypeId;

    private Date mDate = new Date();

    private NavController mNavController;


    public static SetReminderFragment newInstance() {
        return new SetReminderFragment();
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_set_reminder, container, false);
        mCustomToastBinding = DataBindingUtil
                .inflate(inflater, R.layout.custom_toast_layout, (ViewGroup) container.findViewById(R.id.toast_layout_root), false);
        setListeners();
        initRecyclerView();
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mNavController = Navigation.findNavController(view);
        mViewModel = new ViewModelProvider(this).get(SetReminderViewModel.class);
    }

    private void setListeners() {
        mBinding.setDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PersianDatePickerDialog picker = getPersianDatePickerDialog();
                picker.show();
            }
        });

        mBinding.setType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DrugTypeDialogFragment drugTypeDialogFragment = DrugTypeDialogFragment.newInstance();
                drugTypeDialogFragment.setTargetFragment(SetReminderFragment.this, TYPE_REQUEST_CODE);
                drugTypeDialogFragment.show(getParentFragmentManager(), DRUG_TYPE_DIALOG_FRAGMENT);
            }
        });

        mBinding.addButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                String drugName = mBinding.setDrugName.getText().toString();
                String drugNumber = mBinding.setNumber.getText().toString();
                String drugPeriod = mBinding.setPeriod.getText().toString();

                if (drugName.length() == 0) {
                    mCustomToastBinding.setInputString("نام دارو چی شد پس؟!");
                    makeCustomToast();
                } else if (mTypeId == 0) {
                    mCustomToastBinding.setInputString("نوع دارو رو فراموش کردی!");
                    makeCustomToast();
                } else if (drugNumber.length() == 0) {
                    mCustomToastBinding.setInputString("تعداد دارو رو نگفتی!");
                    makeCustomToast();
                } else if (Integer.parseInt(drugNumber) > 1 && drugPeriod.length() == 0) {
                    mCustomToastBinding.setInputString("بازه زمانی مصرف رو مشخص کن");
                    makeCustomToast();
                } else {
                    if (mBinding.nowButton.isChecked()) {
                        Date date = new Date();
                        setDate(date.getHours(), date.getMinutes() + 1);
                    }

                    if (Integer.parseInt(drugNumber) == 1) {
                        drugPeriod = "0";
                    }
                    if (mDate.getTime() - new Date().getTime() < -1000) {
                        mCustomToastBinding.setInputString("زمان ورودی نمی تونه از زمان حال عقب تر باشه!");
                        makeCustomToast();
                    } else {
                        Drug drug = new Drug(
                                drugName,
                                "وقت خوردن دارو رسید",
                                mTypeId,
                                Integer.parseInt(drugNumber),
                                Integer.parseInt(drugPeriod));
                        Reminder reminder = new Reminder(mDate, drug);
                        String stringJsonObject = new Gson().toJson(reminder);
                        Data workRequestData = new Data.Builder()
                                .putString(STRING_JSON_REMINDER, stringJsonObject)
                                .build();
                        WorkRequest workRequest = new OneTimeWorkRequest.Builder(NotificationWorker.class)
                                .setInitialDelay(mDate.getTime() - new Date().getTime(), TimeUnit.MILLISECONDS)
                                .setInputData(workRequestData)
                                .build();
                        WorkManager workManager = WorkManager.getInstance(getActivity());
                        workManager.enqueue(workRequest);
                        mViewModel.getRepository().addReminder(reminder);
                        mNavController.popBackStack();
                    }
                }
            }
        });

        mBinding.setTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerFragment timePickerFragment = TimePickerFragment.newInstance();
                timePickerFragment.setTargetFragment(SetReminderFragment.this, TIME_REQUEST_CODE);
                timePickerFragment.show(getParentFragmentManager(), TIME_PICKER);
            }
        });


        mBinding.todayButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mBinding.setDate.setText(getResources().getString(R.string.today));
                    mDate = new Date();
                } else {
                    mBinding.setDate.setText("");
                }
            }
        });

        mBinding.nowButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mBinding.setTime.setText(getResources().getString(R.string.now));
                } else {
                    mBinding.setTime.setText("");
                }
            }
        });
    }

    private void makeCustomToast() {
        Toast toast = new Toast(getActivity());
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(mCustomToastBinding.getRoot());
        toast.show();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == DrugTypeDialogFragment.type_dialog_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            mTypeId = data.getIntExtra(DrugTypeDialogFragment.TYPE_ID, 0);
            mBinding.setType.setText(getTypeName(mTypeId));
        }

        if (requestCode == TimePickerFragment.TIME_PICKER_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            int hour = data.getIntExtra(TimePickerFragment.HOUR_FROM_TIME_PICKER, 12);
            int minute = data.getIntExtra(TimePickerFragment.MINUTE_FROM_TIME_PICKER, 30);
            setDate(hour, minute);
            mBinding.setTime.setText(hour + " : " + minute);

            Log.d(TAG, "onDateSelected: " + mDate.toString());
            Log.d(TAG, "onDateSelected: " + mDate.getTime());
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void setDate(int hour, int minute) {
        mDate.setHours(hour);
        mDate.setMinutes(minute);
    }

    private PersianDatePickerDialog getPersianDatePickerDialog() {
        PersianCalendar initDate = new PersianCalendar();
        initDate.setPersianDate(1399, 1, 1);
        return new PersianDatePickerDialog(getActivity())
                .setPositiveButtonString("خویه")
                .setNegativeButton("بیخیال")
                .setTodayButtonVisible(false)
                .setBackgroundColor(getResources().getColor(R.color.mainBackgroundColor))
                .setPickerBackgroundColor(getResources().getColor(R.color.colorAccent))
                .setMinYear(PersianDatePickerDialog.THIS_YEAR)
                .setMaxYear(PersianDatePickerDialog.THIS_YEAR + 1)
                .setInitDate(initDate)
                .setActionTextColor(Color.BLACK)
                .setTitleType(PersianDatePickerDialog.WEEKDAY_DAY_MONTH_YEAR)
                .setShowInBottomSheet(true)
                .setListener(new Listener() {
                    @Override
                    public void onDateSelected(PersianCalendar persianCalendar) {
                        mDate.setTime(persianCalendar.getTimeInMillis());
                        Log.d(TAG, "onDateSelected: " + mDate.toString());
                        Log.d(TAG, "onDateSelected: " + mDate.getTime());
                        mBinding.setDate.setText(persianCalendar.getPersianShortDate());
                    }

                    @Override
                    public void onDismissed() {

                    }
                });
    }

    private String getTypeName(int typeId) {
        switch (typeId) {
            case 1:
                return getResources().getString(R.string.pill);
            case 2:
                return getResources().getString(R.string.capsule);
            case 3:
                return getResources().getString(R.string.draft);
            case 4:
                return getResources().getString(R.string.injection);
            default:
                return getResources().getString(R.string.drug);
        }
    }

    private void initRecyclerView() {
        ArrayList<Integer> numberList = new ArrayList<>();
        for (int i = 1; i <= 100; i++) {
            numberList.add(i);
        }

        ArrayList<String> periodList = new ArrayList<String>() {{
            add("4");
            add("6");
            add("8");
            add("12");
            add("روزی یک عدد");
            add("یک روز در میان");
            add("دو روز در میان");
            add("سه روز در میان");
            add("هفتگی");
        }};

        IntRecyclerView numberIntAdapter = new IntRecyclerView(getActivity(), numberList, SetReminderFragment.this);
        mBinding.numberList.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false));
        mBinding.numberList.setAdapter(numberIntAdapter);

        StringRecyclerView periodStringAdapter = new StringRecyclerView(getActivity(), periodList, SetReminderFragment.this);
        mBinding.periodList.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false));
        mBinding.periodList.setAdapter(periodStringAdapter);
    }

    private int getPeriodInt(String period) {
        switch (period) {
            case "4":
                return 4;
            case "6":
                return 6;
            case "8":
                return 8;
            case "10":
                return 10;
            case "12":
                return 12;
            case "روزی یک عدد":
                return 24;
            case "یک روز در میان":
                return 48;
            case "دو روز در میان":
                return 72;
            case "سه روز در میان":
                return 96;
            case "هفتگی":
                return 168;
            default:
                return 0;
        }
    }

    @Override
    public void onIntegerRowClick(int number) {
        mBinding.setNumber.setText(String.valueOf(number));
    }

    @Override
    public void onStringRowClick(String string) {
        mBinding.setPeriod.setText(string);
    }

}