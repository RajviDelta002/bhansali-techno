package com.delta.bhansalitechno.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.delta.bhansalitechno.R;
import com.delta.bhansalitechno.interfaces.MachineListInterface;
import com.delta.bhansalitechno.interfaces.RecyclerViewClickListener;
import com.delta.bhansalitechno.model.RadioModel;

import java.util.ArrayList;

public class RadioAdapter extends RecyclerView.Adapter<RadioAdapter.RadioAdapterVH> {

    private final ArrayList<RadioModel> list;
    private RecyclerViewClickListener listener;
    private MachineListInterface machineListInterface;
    private int selectedPosition = 0;
    private String selectedType;

    public RadioAdapter(ArrayList<RadioModel> list) {
        this.list = list;
    }

    public RadioAdapter(ArrayList<RadioModel> list, String pipeSizeType, MachineListInterface textListInterface) {
        this.list = list;
        this.selectedType = pipeSizeType;
        this.machineListInterface = textListInterface;
    }

    @NonNull
    @Override
    public RadioAdapterVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_radio, parent, false);
        return new RadioAdapterVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RadioAdapterVH holder, int position) {
        try {
            RadioModel model = list.get(position);
            holder.rdBtn.setText(model.getFileType());

            if (!selectedType.isEmpty()) {
                if (selectedType.equals(model.getFileType())) {
                    selectedPosition = position;
                }
            }

            holder.rdBtn.setChecked(selectedPosition == position);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void setItemClick(RecyclerViewClickListener mListener) {
        this.listener = mListener;
    }

    public class RadioAdapterVH extends RecyclerView.ViewHolder implements View.OnClickListener {

        private RadioButton rdBtn;

        public RadioAdapterVH(@NonNull View itemView) {
            super(itemView);
            try {
                rdBtn = itemView.findViewById(R.id.rdBtn);
                rdBtn.setOnClickListener(this);
                //rdBtn.setOnClickListener(view -> listener.onItemClick(view, getAdapterPosition()));
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
                machineListInterface.onSelect("", list.get(selectedPosition).getFileType(),
                        "", list.get(selectedPosition).getPDFFile());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
