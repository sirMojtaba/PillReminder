package com.example.pillreminderstarting.view.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import com.example.pillreminderstarting.R;
import com.example.pillreminderstarting.databinding.ActivityStarterBinding;
import com.example.pillreminderstarting.view.ui.login.LoginFragment;
import com.example.pillreminderstarting.view.other_fragments.PresenterContainerFragment;

public class StarterActivity extends AppCompatActivity implements PresenterContainerFragment.ListenerCallback {

    private ActivityStarterBinding mBinding;
    private FragmentManager mFragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_starter);
        mFragmentManager = getSupportFragmentManager();
        Fragment fragment = mFragmentManager.findFragmentById(R.id.fragment_container);
        if(fragment == null){
            mFragmentManager.beginTransaction()
                    .add(R.id.fragment_container, PresenterContainerFragment.newInstance())
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .commit();
        }
    }

    @Override
    public void onSkipButtonListener() {
        mFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, LoginFragment.newInstance())
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .commit();
    }
}