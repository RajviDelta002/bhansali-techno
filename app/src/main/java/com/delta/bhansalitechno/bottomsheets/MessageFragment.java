package com.delta.bhansalitechno.bottomsheets;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.delta.bhansalitechno.R;
import com.delta.bhansalitechno.interfaces.MessageInterface;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.crashlytics.FirebaseCrashlytics;

public class MessageFragment extends BottomSheetDialogFragment {

    private String message;
    private EditText editText;
    private MessageInterface messageInterface;

    public MessageFragment(String message) {
        this.message = message;
        this.editText = null;
    }

    public MessageFragment(String message, EditText editText, MessageInterface messageInterface) {
        this.message = message;
        this.editText = editText;
        this.messageInterface  = messageInterface;
    }

    @SuppressLint("RestrictedApi")
    @Override
    public void setupDialog(@NonNull Dialog dialog, int style) {
        super.setupDialog(dialog, style);

        try {

            //Setting Up Layout
            @SuppressLint("InflateParams")
            View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragement_message, null);
            dialog.setContentView(view);

            //Casting
            TextView lblDismiss = view.findViewById(R.id.lblDismiss);
            TextView lblMessage = view.findViewById(R.id.lblMessageError);

            //Setting Up Text
            lblMessage.setText(message);

            //Listener
            lblDismiss.setOnClickListener(view1 -> dismiss());

        } catch (Exception e) {
            e.printStackTrace();
            FirebaseCrashlytics.getInstance().recordException(e);
        }
    }


    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        if (editText != null) {
            messageInterface.onDialogDismiss(editText);
        }
    }
}
