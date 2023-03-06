package com.delta.bhansalitechno.adapter;

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

    Context context;
    ArrayList<MachineModel> list;
    MachineListInterface machineListInterface;
    int selected_position = -1;
    String pipeSizeType;

    public MachineAdapter(Context context, ArrayList<MachineModel> list, String pipeSizeType, MachineListInterface textListInterface) {
        this.context = context;
        this.list = list;
        this.pipeSizeType = pipeSizeType;
        this.machineListInterface = textListInterface;
    }

    @NonNull
    @Override
    public MachineAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_machine, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MachineAdapter.ViewHolder holder, int position) {
        try {
            MachineModel model = list.get(position);
            holder.tvNumber.setText(model.getMachineNo());
            holder.tvCode.setText(model.getMachineCode());
            holder.tvName.setText(model.getMachineName());

            if (!pipeSizeType.isEmpty()) {
                if (pipeSizeType.equals(model.getMachineName())) {
                    selected_position = position;
                }
            }

            holder.cbMachine.setChecked(selected_position == position);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        CheckBox cbMachine;
        TextView tvNumber;
        TextView tvCode;
        TextView tvName;

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
                notifyItemChanged(selected_position);
                selected_position = getAdapterPosition();
                notifyItemChanged(selected_position);
                pipeSizeType = "";
                machineListInterface.onSelect(list.get(selected_position).getMachineId(), list.get(selected_position).getMachineName(),
                        list.get(selected_position).getMachineNo(), list.get(selected_position).getMachineCode());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
