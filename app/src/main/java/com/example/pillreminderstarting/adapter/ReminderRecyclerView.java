package com.example.pillreminderstarting.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.LifecycleOwner;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pillreminderstarting.R;
import com.example.pillreminderstarting.databinding.RowOfReminderListBinding;
import com.example.pillreminderstarting.model.Reminder;

import java.sql.Timestamp;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ReminderRecyclerView extends RecyclerView.Adapter<ReminderRecyclerView.InnerViewHolder> {

    public static final String TAG = "tag10";
    private RowOfReminderListBinding mBinding;
    private ArrayList<Reminder> mList;
    private Context mContext;

    public ReminderRecyclerView(Context context, List<Reminder> list) {
        mContext = context;
        mList = (ArrayList<Reminder>) list;
    }

    @NonNull
    @Override
    public InnerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        mBinding = DataBindingUtil.inflate(inflater, R.layout.row_of_reminder_list, parent, false);
        InnerViewHolder viewHolder = new InnerViewHolder(mBinding);
        return viewHolder;
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull InnerViewHolder holder, int position) {
        holder.onBind(position);
    }


    public class InnerViewHolder extends RecyclerView.ViewHolder {

        RowOfReminderListBinding rowBinding;

        public InnerViewHolder(@NonNull RowOfReminderListBinding binding) {
            super(binding.getRoot());
            rowBinding = binding;
        }

        @SuppressLint("UseCompatLoadingForDrawables")
        @RequiresApi(api = Build.VERSION_CODES.O)
        private void onBind(int position) {

            rowBinding.name.setText(mList.get(position).getDrug().getName());
            long dateTimeStamp = mList.get(position).getDate().getTime();
            long nowTimeStamp = new Date().getTime();
            long diff = dateTimeStamp - nowTimeStamp;
            if (diff < 60000) {
                mBinding.time.setText("تا لحظاتی دیگر");
            } else {
                int bigMinute = (int) ((diff / 1000) / 60);
                int hour = bigMinute / 60;
                int minute = bigMinute - (hour * 60);
                rowBinding.time.setText(String.format("%d  ساعت  و  %d  دقیقه ", hour, minute));
            }
            switch (mList.get(position).getDrug().getTypeId()) {
                case 1: {
                    rowBinding.imageInRow.setImageResource(R.drawable.pill_png_resized);
                    break;
                }
                case 2: {
                    rowBinding.imageInRow.setImageResource(R.drawable.capsule_png_resized);
                    break;
                }
                case 3: {
                    rowBinding.imageInRow.setImageResource(R.drawable.bottle_png_resized);
                    break;
                }
                case 4: {
                    rowBinding.imageInRow.setImageResource(R.drawable.injection_png_resized);
                    break;
                }
                default: {
                    rowBinding.imageInRow.setImageResource(R.drawable.other_resized);
                    break;
                }
            }
            rowBinding.executePendingBindings();
        }
    }
}
