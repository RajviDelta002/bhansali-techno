package com.delta.bhansalitechno.bottomsheets;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.delta.bhansalitechno.R;
import com.delta.bhansalitechno.interfaces.StopInterface;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.crashlytics.FirebaseCrashlytics;

import org.jetbrains.annotations.NotNull;

public class StopFragment extends BottomSheetDialogFragment {

    private  String quantity;
    private  StopInterface addQuantityInterface;
    EditText txtQuantity;
    TextView btnStop;

    public StopFragment() {

    }

    public StopFragment(String quantity, StopInterface addQuantityInterface) {
        this.quantity = quantity;
        this.addQuantityInterface = addQuantityInterface;
    }

    @SuppressLint({"RestrictedApi", "InflateParams"})
    @Override
    public void setupDialog(@NonNull @NotNull Dialog dialog, int style) {
        super.setupDialog(dialog, style);
        View view = null;
        try {
            view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_stop, null);
            dialog.setContentView(view);

            txtQuantity = view.findViewById(R.id.txtQuantity);
            btnStop = view.findViewById(R.id.btnStop);

            txtQuantity.setText(quantity);
            btnStop.setOnClickListener(v -> {
                if (txtQuantity.getText().toString().isEmpty()) {
                    Toast.makeText(requireActivity(), "Please enter production quantity.", Toast.LENGTH_SHORT).show();
                } else {
                    //addQuantityInterface.onClick(txtQuantity.getText().toString().trim());
                    dismiss();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            FirebaseCrashlytics.getInstance().recordException(e);
        }
    }
}
