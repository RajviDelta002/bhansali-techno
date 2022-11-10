package com.delta.bhansalitechno.bottomsheets;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;


import com.delta.bhansalitechno.R;
import com.delta.bhansalitechno.interfaces.NoInternetInterface;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.crashlytics.FirebaseCrashlytics;

public class NoInternetFragment extends BottomSheetDialogFragment {

    NoInternetInterface noInternetInterface;

    public NoInternetFragment(NoInternetInterface noInternetInterface) {
        this.noInternetInterface = noInternetInterface;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        BottomSheetDialog dialog = null;
        try {
            dialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);
            @SuppressLint("InflateParams")
            View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_no_internet, null);
            dialog.setContentView(view);

            //Dialog Listener
            dialog.setOnShowListener(new DialogInterface.OnShowListener() {
                @Override
                public void onShow(DialogInterface dialog1) {
                    BottomSheetDialog d = (BottomSheetDialog) dialog1;
                    FrameLayout bottomSheet = d.findViewById(com.google.android.material.R.id.design_bottom_sheet);
                    if (bottomSheet != null) {
                        BottomSheetBehavior.from(bottomSheet).setState(BottomSheetBehavior.STATE_EXPANDED);
                    }
                }
            });

            //Casting
            TextView lblDismiss = view.findViewById(R.id.lblDismissNoInternet);

            //Listener
            lblDismiss.setOnClickListener(view1 -> dismiss());
        } catch (Exception e) {
            e.printStackTrace();
            FirebaseCrashlytics.getInstance().recordException(e);
        }

        return dialog;
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        noInternetInterface.onDismiss();
    }
}
