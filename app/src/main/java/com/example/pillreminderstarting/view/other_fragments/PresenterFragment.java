package com.example.pillreminderstarting.view.other_fragments;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.pillreminderstarting.R;
import com.example.pillreminderstarting.databinding.FragmentPresenterBinding;


public class PresenterFragment extends Fragment {

    public static final String POSITION = "position";
    private FragmentPresenterBinding mBinding;
    private int mPosition;

    public static PresenterFragment newInstance(int position) {
        PresenterFragment fragment = new PresenterFragment();
        Bundle args = new Bundle();
        args.putInt(POSITION, position);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPosition = getArguments().getInt(POSITION);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_presenter,container,false);
        initView();
        return mBinding.getRoot();
    }

    private void initView() {
        switch (mPosition){
            default:
                mBinding.image.setImageResource(R.drawable.starter1);
                mBinding.description.setText(R.string.starter_description_1);
                break;
            case 1:
                mBinding.image.setImageResource(R.drawable.starter2);
                mBinding.description.setText(R.string.starter_description_2);
                break;
            case 2:
                mBinding.image.setImageResource(R.drawable.starter3);
                mBinding.description.setText(R.string.starter_description_3);
                break;
        }
    }
}