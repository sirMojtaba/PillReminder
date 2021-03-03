package com.example.pillreminderstarting.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pillreminderstarting.R;

import java.util.ArrayList;

public class StringRecyclerView extends RecyclerView.Adapter<StringRecyclerView.InnerViewHolder> {

    private Context mContext;
    private ArrayList<String> mStringArrayList;
    private OnStringRowClickListener mListener;

    public StringRecyclerView(Context context, ArrayList<String> strings,OnStringRowClickListener listener ) {
        mContext = context;
        mStringArrayList = strings;
        mListener = listener;
    }

    @NonNull
    @Override
    public InnerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.horizontal_row_style, parent, false);
        InnerViewHolder innerViewHolder = new InnerViewHolder(view);
        return innerViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull InnerViewHolder holder, int position) {
        holder.number.setText(mStringArrayList.get(position));
        holder.number.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onStringRowClick(mStringArrayList.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return mStringArrayList.size();
    }

    public class InnerViewHolder extends RecyclerView.ViewHolder{

        private TextView number;

        public InnerViewHolder(@NonNull View itemView) {
            super(itemView);
            number = itemView.findViewById(R.id.number_in_horizontal_row);
        }
    }

    public interface OnStringRowClickListener{
        public void onStringRowClick(String string);
    }

}
