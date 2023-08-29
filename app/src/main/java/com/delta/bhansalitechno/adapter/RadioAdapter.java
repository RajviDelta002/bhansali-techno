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

    private  ArrayList<RadioModel> list;
    private RecyclerViewClickListener listener;
    MachineListInterface machineListInterface;
    int selected_position = 0;
    String pipeSizeType;

    public RadioAdapter(ArrayList<RadioModel> list) {
        this.list = list;
    }

    public RadioAdapter(ArrayList<RadioModel> list, String pipeSizeType, MachineListInterface textListInterface) {
        this.list = list;
        this.pipeSizeType = pipeSizeType;
        this.machineListInterface = textListInterface;
        //notifyDataSetChanged();
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

            if (!pipeSizeType.isEmpty()) {
                if (pipeSizeType.equals(model.getFileType())) {
                    selected_position = position;
                }
            }

            holder.rdBtn.setChecked(selected_position == position);
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

    public class RadioAdapterVH extends RecyclerView.ViewHolder implements View.OnClickListener{

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
                notifyItemChanged(selected_position);
                selected_position = getAdapterPosition();
                notifyItemChanged(selected_position);
                pipeSizeType = "";
                machineListInterface.onSelect("", list.get(selected_position).getFileType(),
                        "", list.get(selected_position).getPDFFile());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
