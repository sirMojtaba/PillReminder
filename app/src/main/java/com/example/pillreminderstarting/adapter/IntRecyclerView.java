package com.example.pillreminderstarting.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pillreminderstarting.R;

import java.util.ArrayList;

public class IntRecyclerView extends RecyclerView.Adapter<IntRecyclerView.InnerViewHolder> {

    private ArrayList<Integer> mIntegerArrayList;
    private Context mContext;
    private OnIntegerRowClickListener mListener;

    public IntRecyclerView(Context context, ArrayList<Integer> integerArrayList, OnIntegerRowClickListener listener) {
        mContext = context;
        mIntegerArrayList = integerArrayList;
        mListener = listener;
    }

    @NonNull
    @Override
    public InnerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.horizontal_row_style, parent, false);
        InnerViewHolder viewHolder = new InnerViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull InnerViewHolder holder, int position) {
        holder.mTextView.setText(mIntegerArrayList.get(position).toString());
        holder.mTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onIntegerRowClick(mIntegerArrayList.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return mIntegerArrayList.size();
    }

    public class InnerViewHolder extends RecyclerView.ViewHolder {

        TextView mTextView;

        public InnerViewHolder(@NonNull View itemView) {
            super(itemView);
            mTextView = itemView.findViewById(R.id.number_in_horizontal_row);
        }
    }

    public interface OnIntegerRowClickListener{
        public void onIntegerRowClick(int number);
    }
}
