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
import com.delta.bhansalitechno.interfaces.RecyclerViewClickListener;
import com.delta.bhansalitechno.model.JobNoModel;

import java.util.ArrayList;

public class StartJobListAdapter extends RecyclerView.Adapter<StartJobListAdapter.StartJobListAdapterVH> {

    private final ArrayList<JobNoModel> list;

    public StartJobListAdapter(ArrayList<JobNoModel> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public StartJobListAdapterVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_start_job_list, parent, false);
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
            holder.tvRemark.setText(noModel.getJobStartStopRemarks().replace("u0026","&").replace("u0027","'"));

            holder.cbJob.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (isChecked) {
                    list.get(position).setItemSelected(true);
                    holder.cbJob.setChecked(true);

                    /*if (getSelected().size() > 0) {
                        if (getSelected().size() > 1) {
                            String lastPartNo = "";
                            String lastProcessName = "";
                            for (int i = 0; i < getSelected().size(); i++) {
                                if (lastPartNo.isEmpty() && lastProcessName.isEmpty()) {
                                    lastPartNo = getSelected().get(i).getRefNo();
                                    lastProcessName = getSelected().get(i).getProcessName();
                                } else {
                                    if (!lastPartNo.equalsIgnoreCase(getSelected().get(i).getRefNo())) {
                                        list.get(position).setItemSelected(false);
                                        holder.cbJob.setChecked(false);
                                        Toast.makeText(context, "Selected Same Part No In Job List.", Toast.LENGTH_SHORT).show();
                                        break;
                                    }

                                    if (!lastProcessName.equalsIgnoreCase(getSelected().get(i).getProcessName())) {
                                        list.get(position).setItemSelected(false);
                                        holder.cbJob.setChecked(false);
                                        Toast.makeText(context, "Selected Same Process In Job List.", Toast.LENGTH_SHORT).show();
                                        break;
                                    }

                                    *//*if (!lastPartNo.equalsIgnoreCase(getSelected().get(i).getRefNo()) ||
                                            !lastProcessName.equalsIgnoreCase(getSelected().get(i).getProcessName())) {
                                        Toast.makeText(this, "Selected Same Part No & Process In Job List.", Toast.LENGTH_SHORT).show();
                                        break;
                                    }*//*

                                    if (i == getSelected().size() - 1) {
                                        list.get(position).setItemSelected(true);
                                        holder.cbJob.setChecked(true);
                                    }
                                }
                            }
                        } else {
                            list.get(position).setItemSelected(true);
                            holder.cbJob.setChecked(true);
                        }
                    } else {
                        list.get(position).setItemSelected(true);
                        holder.cbJob.setChecked(true);
                    }*/

                    /*if (!holder.tvQty.getText().toString().trim().isEmpty()) {
                        if (qty <= pendingQty) {

                        } else {
                            Toast.makeText(context, "Quantity Can't Be Greater Than Pending Quantity", Toast.LENGTH_SHORT).show();
                            list.get(position).setItemSelected(false);
                            holder.cbJob.setChecked(false);
                        }
                    } else {
                        Toast.makeText(context, "Please Enter Quantity", Toast.LENGTH_SHORT).show();
                        list.get(position).setItemSelected(false);//TODO : Add 12-05-2023
                        holder.cbJob.setChecked(false);
                    }*/

                    /*if (getSelected().size() > 0) {
                        if (getSelected().size() >= 1) {
                            String lastPartNo = "";
                            String lastSelectProcess = "";
                            for (int i = 0; i < getSelected().size(); i++) {
                                if (lastPartNo.isEmpty() && lastSelectProcess.isEmpty()) {
                                    lastPartNo = getSelected().get(i).getRefNo();
                                    lastSelectProcess = getSelected().get(i).getProcessName();
                                } else {
                                    if (!lastPartNo.equalsIgnoreCase(getSelected().get(i).getRefNo())
                                            && !lastSelectProcess.equalsIgnoreCase(getSelected().get(i).getProcessName())) {
                                        Toast.makeText(context, "Selected Same Part No & Process", Toast.LENGTH_SHORT).show();
                                        break;
                                    }
                                    if (i == getSelected().size() - 1) {
                                        holder.cbJob.setChecked(false);
                                        list.get(position).setItemSelected(false);
                                    }
                                }
                            }
                        }
                    } else {
                        holder.cbJob.setChecked(true);
                        list.get(position).setItemSelected(true);
                    }*/

                } else {
                    list.get(position).setItemSelected(false);
                    holder.cbJob.setChecked(false);
                }
            });

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

    public static class StartJobListAdapterVH extends RecyclerView.ViewHolder {

        private CheckBox cbJob;
        private TextView tvJobNo, tvMachine, tvShop, tvPartNo, tvQty, tvProcess, tvHubPort, tvFinalSize, tvRemark;

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
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
