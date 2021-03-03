package com.example.pillreminderstarting.view.other_fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;

import android.view.LayoutInflater;

import com.example.pillreminderstarting.R;
import com.example.pillreminderstarting.databinding.FragmentDrugTypeDialogBinding;


public class DrugTypeDialogFragment extends DialogFragment {

    public static final String TYPE_ID = "type id";
    public static final int type_dialog_REQUEST_CODE = 0;
    private FragmentDrugTypeDialogBinding mBinding;

    public static DrugTypeDialogFragment newInstance() {
        DrugTypeDialogFragment fragment = new DrugTypeDialogFragment();
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
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_drug_type_dialog, null, true);
        mBinding.setFragment(this);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder
                .setView(mBinding.getRoot());
        AlertDialog dialog = builder.create();
        return dialog;
    }

    public void setListeners(int typeId){
        if (typeId != 0) {
            Intent intent = new Intent();
            intent.putExtra(TYPE_ID, typeId);
            getTargetFragment().onActivityResult(type_dialog_REQUEST_CODE, Activity.RESULT_OK, intent);
        }
        dismiss();
    }
}