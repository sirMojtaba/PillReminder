package com.example.pillreminderstarting.view.ui.reminder;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.pillreminderstarting.R;
import com.example.pillreminderstarting.adapter.ReminderRecyclerView;
import com.example.pillreminderstarting.databinding.FragmentHomeBinding;
import com.example.pillreminderstarting.model.Reminder;

import java.util.ArrayList;
import java.util.List;

public class ReminderFragment extends Fragment {

    public static final String TAG = "tag";
    private FragmentHomeBinding mBinding;
    private ReminderViewModel mViewModel;
    private NavController mNavController;
    private ReminderRecyclerView mAdapter;
    private LiveData<List<Reminder>> mLiveReminderList;

    public static ReminderFragment newInstance() {

        return new ReminderFragment();
    }

    public ReminderFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false);
        return mBinding.getRoot();
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(ReminderViewModel.class);
        mNavController = Navigation.findNavController(view);
        mLiveReminderList = mViewModel.getLiveReminderList();
        setObserver();
        setListeners();
    }

    private void setObserver() {
        mLiveReminderList.observe(getViewLifecycleOwner(), new Observer<List<Reminder>>() {
            @Override
            public void onChanged(List<Reminder> reminders) {
                initRecyclerView((ArrayList<Reminder>) reminders);
            }
        });
    }

    private void setListeners() {
        mBinding.addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mNavController.navigate(R.id
                        .action_navigation_home_to_setReminderFragment);
            }
        });
    }

    private void initRecyclerView(ArrayList<Reminder> reminders) {
        mAdapter = new ReminderRecyclerView(getActivity(), reminders);
        mBinding.reminderList.setLayoutManager(new LinearLayoutManager(getActivity()));
        mBinding.reminderList.setAdapter(mAdapter);
        updateScreen(reminders);
    }

    public void updateScreen(ArrayList<Reminder> reminders){
        if(reminders.size() == 0){
            mBinding.reminderList.setVisibility(View.INVISIBLE);
            mBinding.notFountIcon.setVisibility(View.VISIBLE);
            mBinding.notFoundText.setVisibility(View.VISIBLE);
        } else {
            mBinding.reminderList.setVisibility(View.VISIBLE);
            mBinding.notFountIcon.setVisibility(View.INVISIBLE);
            mBinding.notFoundText.setVisibility(View.INVISIBLE);
        }
    }
}