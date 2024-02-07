package com.delta.bhansalitechno.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.delta.bhansalitechno.R;
import com.delta.bhansalitechno.interfaces.RecyclerViewClickListener;
import com.delta.bhansalitechno.model.JobNoModel;

import java.util.ArrayList;

public class StopJobListAdapter extends RecyclerView.Adapter<StopJobListAdapter.StartJobListAdapterVH> {

    private final ArrayList<JobNoModel> list;
    private RecyclerViewClickListener listener;

    public StopJobListAdapter(ArrayList<JobNoModel> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public StartJobListAdapterVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_stop_job_list, parent, false);
        return new StartJobListAdapterVH(view);
    }

    @SuppressLint({"SetTextI18n", "DefaultLocale"})
    @Override
    public void onBindViewHolder(@NonNull StartJobListAdapterVH holder, int position) {
        try {
            JobNoModel noModel = list.get(position);
            holder.tvJobNo.setText(noModel.getJobNo());
            holder.tvMachine.setText(noModel.getMachineName());
            holder.tvShop.setText(noModel.getShopName());
            holder.tvPartNo.setText(noModel.getPartNo() + " / " + noModel.getpCNo());
            holder.tvQty.setText(String.format("%.0f", Double.parseDouble(noModel.getPendingQty())));
            holder.tvProcess.setText(noModel.getProcessName().replace("u0026","&").replace("u0027","'"));
            holder.tvHubPort.setText(noModel.getHubPartNo());
            holder.tvFinalSize.setText(noModel.getSize());
            holder.tvRemark.setText(noModel.getRemarks().replace("u0026","&").replace("u0027","'"));

            holder.cbJob.setChecked(list.get(position).isItemSelected());
            holder.cbJob.setClickable(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @NonNull
    public ArrayList<JobNoModel> getSelected() {
        ArrayList<JobNoModel> selected = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).isItemSelected()) {
                selected.add(list.get(i));
            }
        }
        notifyDataSetChanged();
        return selected;
    }

    public void setItemClick(RecyclerViewClickListener mListener) {
        this.listener = mListener;
    }

    public class StartJobListAdapterVH extends RecyclerView.ViewHolder {

        private CheckBox cbJob;
        private TextView tvJobNo, tvMachine, tvShop, tvPartNo, tvQty, tvProcess, tvHubPort, tvFinalSize, tvRemark;
        private TextView btnHold, btnStop;
        //private Button webPdfview;

        public StartJobListAdapterVH(@NonNull View itemView) {
            super(itemView);
            try {
                cbJob = itemView.findViewById(R.id.cbJob);
                tvJobNo = itemView.findViewById(R.id.tvJobNo);
                tvMachine = itemView.findViewById(R.id.tvMachine);
                tvShop = itemView.findViewById(R.id.tvShop);
                tvPartNo = itemView.findViewById(R.id.tvPartNo);
                tvQty = itemView.findViewById(R.id.tvQty);
                tvProcess = itemView.findViewById(R.id.tvProcess);
                tvHubPort = itemView.findViewById(R.id.tvHubPort);
                tvFinalSize = itemView.findViewById(R.id.tvFinalSize);
                tvRemark = itemView.findViewById(R.id.tvRemark);
                //webPdfview = itemView.findViewById(R.id.webPdfview);

                btnHold = itemView.findViewById(R.id.btnHold);
                btnStop = itemView.findViewById(R.id.btnStop);

                btnHold.setOnClickListener(view -> listener.onItemClick(view, getAdapterPosition()));
                btnStop.setOnClickListener(view -> listener.onItemClick(view, getAdapterPosition()));
                //webPdfview.setOnClickListener(view -> listener.onItemClick(view, getAdapterPosition()));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
