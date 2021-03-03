package com.example.pillreminderstarting.view.other_fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.pillreminderstarting.R;
import com.example.pillreminderstarting.databinding.FragmentPresenterContainerBinding;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class PresenterContainerFragment extends Fragment {

    private FragmentPresenterContainerBinding mBinding;
    private ListenerCallback mCallback;


    public static PresenterContainerFragment newInstance() {
        PresenterContainerFragment fragment = new PresenterContainerFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mCallback = (ListenerCallback) context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = DataBindingUtil
                .inflate(LayoutInflater.from(getActivity()), R.layout.fragment_presenter_container,container,false);
        mBinding.starterViewPager.setAdapter(new StarterPagerAdapter(getActivity()));
        new TabLayoutMediator(mBinding.starterTabLayout, mBinding.starterViewPager, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
            }
        }).attach();
        setListeners();
        return mBinding.getRoot();
    }

    public void setListeners(){
        mBinding.skipButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallback.onSkipButtonListener();
            }
        });
    }

    private class StarterPagerAdapter extends FragmentStateAdapter {

        public StarterPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
            super(fragmentActivity);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            PresenterFragment presenterFragment = PresenterFragment.newInstance(position);
            return presenterFragment;
        }

        @Override
        public int getItemCount() {
            return 3;
        }
    }

    public interface ListenerCallback{
        public void onSkipButtonListener();
    }
}