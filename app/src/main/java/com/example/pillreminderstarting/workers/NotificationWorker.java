package com.example.pillreminderstarting.workers;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.text.style.TtsSpan;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import androidx.work.WorkRequest;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.example.pillreminderstarting.R;
import com.example.pillreminderstarting.model.Reminder;
import com.example.pillreminderstarting.repository.AppRepository;
import com.example.pillreminderstarting.view.ui.setReminer.SetReminderFragment;
import com.example.pillreminderstarting.view.ui.setReminer.SetReminderViewModel;
import com.google.gson.Gson;

import java.util.Date;
import java.util.concurrent.TimeUnit;

public class NotificationWorker extends Worker {

    public static final String CHANEL_ID = "drug";
    public static final String CHANEL_NAME = "drug";
    public static final String TAG = "wo";
    private Context mContext;
    private Reminder mReminder;

    public NotificationWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        mContext = context;
        mReminder = new Gson()
                .fromJson(getInputData().getString(SetReminderFragment.STRING_JSON_REMINDER), Reminder.class);
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    @NonNull
    @Override
    public Result doWork() {
        NotificationChannel channel =
                new NotificationChannel(CHANEL_ID, CHANEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
        NotificationManager manager =
                (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        manager.createNotificationChannel(channel);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), CHANEL_ID);
        builder.setContentTitle(mReminder.getDrug().getName())
                .setSmallIcon(R.mipmap.ic_launcher_foreground)
                .setLargeIcon(getBitmap(getDrugIcon(mReminder.getDrug().getTypeId())))
                .setContentText("وقت خوردن دارو رسیده");

        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(getApplicationContext());
        managerCompat.notify(0, builder.build());
//        Log.d(TAG, "number 1: "+ mReminder.getDrug().getNumber()+"      date 1: "+mReminder.getDate());
        mReminder.getDrug().setNumber(mReminder.getDrug().getNumber() - 1);
        Date date = new Date();
        date.setTime(new Date().getTime() + mReminder.getDrug().getPeriod()*60*60*1000);
        mReminder.setDate(date);
//        Log.d(TAG, "number 2: "+ mReminder.getDrug().getNumber()+"      date 2: "+mReminder.getDate());
        if (mReminder.getDrug().getNumber() > 0) {
            AppRepository.getInstance(mContext).updateReminder(mReminder);
            String stringJsonObject = new Gson().toJson(mReminder);
            Data workRequestData = new Data.Builder()
                    .putString(SetReminderFragment.STRING_JSON_REMINDER, stringJsonObject)
                    .build();
            WorkRequest workRequest = new OneTimeWorkRequest.Builder(NotificationWorker.class)
                    .setInitialDelay(mReminder.getDrug().getPeriod() * 60, TimeUnit.MINUTES)
                    .setInputData(workRequestData)
                    .build();
            WorkManager workManager = WorkManager.getInstance(mContext);
            workManager.enqueue(workRequest);
        } else {
            AppRepository.getInstance(mContext).deleteReminder(mReminder.getReminderId());
        }
        return Result.success();
    }

    private Bitmap getBitmap(int id) {
        return BitmapFactory.decodeResource(getApplicationContext().getResources(), id);
    }

    private int getDrugIcon(int typeId) {
        switch (typeId) {
            case 1:
                return R.drawable.pill_png_resized;
            case 2:
                return R.drawable.capsule_png_resized;
            case 3:
                return R.drawable.bottle_png_resized;
            case 4:
                return R.drawable.injection_png_resized;
            default:
                return R.drawable.other_resized;
        }
    }
}
