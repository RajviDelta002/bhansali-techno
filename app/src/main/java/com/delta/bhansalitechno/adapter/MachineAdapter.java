package com.delta.bhansalitechno.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.delta.bhansalitechno.R;
import com.delta.bhansalitechno.interfaces.MachineListInterface;
import com.delta.bhansalitechno.model.MachineModel;

import java.util.ArrayList;

public class MachineAdapter extends RecyclerView.Adapter<MachineAdapter.ViewHolder> {

    private final ArrayList<MachineModel> list;
    private final MachineListInterface machineListInterface;
    private int selectedPosition = -1;
    private String selectedType;

    public MachineAdapter(ArrayList<MachineModel> list, String selectedType, MachineListInterface textListInterface) {
        this.list = list;
        this.selectedType = selectedType;
        this.machineListInterface = textListInterface;
    }

    @NonNull
    @Override
    public MachineAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_machine, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull MachineAdapter.ViewHolder holder, int position) {
        try {
            MachineModel model = list.get(position);
            holder.tvNumber.setText("Number : " + model.getMachineNo());
            holder.tvCode.setText("Code        : " + model.getMachineCode());
            holder.tvName.setText("Name       : " + model.getMachineName());

            if (!selectedType.isEmpty()) {
                if (selectedType.equals(model.getMachineName())) {
                    selectedPosition = position;
                }
            }

            holder.cbMachine.setChecked(selectedPosition == position);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private CheckBox cbMachine;
        private TextView tvNumber;
        private TextView tvCode;
        private TextView tvName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            try {
                cbMachine = itemView.findViewById(R.id.cbMachine);
                tvNumber = itemView.findViewById(R.id.tvNumber);
                tvName = itemView.findViewById(R.id.tvName);
                tvCode = itemView.findViewById(R.id.tvCode);
                itemView.setOnClickListener(this);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onClick(View view) {
            try {
                notifyItemChanged(selectedPosition);
                selectedPosition = getAdapterPosition();
                notifyItemChanged(selectedPosition);
                selectedType = "";
                machineListInterface.onSelect(list.get(selectedPosition).getMachineId(), list.get(selectedPosition).getMachineName(),
                        list.get(selectedPosition).getMachineNo(), list.get(selectedPosition).getMachineCode());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
