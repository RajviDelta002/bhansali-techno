package com.delta.bhansalitechno.bottomsheets;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;


import com.delta.bhansalitechno.R;
import com.delta.bhansalitechno.interfaces.ExitAppInterface;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.crashlytics.FirebaseCrashlytics;

public class AppExitFragment extends BottomSheetDialogFragment {

     ExitAppInterface exitAppInterface;

    public AppExitFragment(ExitAppInterface exitAppInterface) {
        this.exitAppInterface = exitAppInterface;
    }

    @SuppressLint("RestrictedApi")
    @Override
    public void setupDialog(Dialog dialog, int style) {
        super.setupDialog(dialog, style);

        try {

            //Setting Up View
            @SuppressLint("InflateParams")
            View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_app_exit, null);
            dialog.setContentView(view);

            //Casting
            TextView lblDismiss = view.findViewById(R.id.lblDismissExitApp);
            TextView lblExit = view.findViewById(R.id.lblYesExitApp);

            //Listener --> Dismiss
            lblDismiss.setOnClickListener(view1 -> dismiss());

            //Listener --> Exit App
            lblExit.setOnClickListener(view12 -> exitAppInterface.onExitAppClicked());

        } catch (Exception e) {
            e.printStackTrace();
            FirebaseCrashlytics.getInstance().recordException(e);
        }
    }
}
