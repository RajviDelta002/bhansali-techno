package com.delta.bhansalitechno.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.delta.bhansalitechno.R;
import com.delta.bhansalitechno.interfaces.TextListInterface;
import com.delta.bhansalitechno.model.JobNoModel;
import com.google.firebase.crashlytics.FirebaseCrashlytics;

import java.util.ArrayList;
import java.util.List;

public class TexListAdapterWithFilter extends RecyclerView.Adapter<TexListAdapterWithFilter.VhUserType> implements Filterable {

    Context context;
    List<JobNoModel> data;
    List<JobNoModel> filteredData;
    TextListInterface textListInterface;
    TextListFilter filter = new TextListFilter();

    public TexListAdapterWithFilter(@NonNull Context context, @NonNull List<JobNoModel> data, @NonNull TextListInterface textListInterface) {
        this.context = context;
        this.data = data;
        this.filteredData = data;
        this.textListInterface = textListInterface;
    }

    @NonNull
    @Override
    public VhUserType onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.item_problem, parent, false);
        return new VhUserType(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VhUserType holder, final int position) {

        try {
            if (!TextUtils.isEmpty(data.get(position).getJobNo())) {
                holder.lblProblem.setText(data.get(position).getJobNo());
            }

            holder.itemView.setOnClickListener(v ->
                    textListInterface.onSelect(
                            data.get(position).getJobListId(),
                            data.get(position).getJobNo()
                    ));
        } catch (Exception e) {
            e.printStackTrace();
            FirebaseCrashlytics.getInstance().recordException(e);
        }

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    static class VhUserType extends RecyclerView.ViewHolder {

        TextView lblProblem;

        VhUserType(@NonNull View itemView) {
            super(itemView);

            lblProblem = itemView.findViewById(R.id.lblProblemItemProblem);
        }
    }

    @NonNull
    @Override
    public Filter getFilter() {
        return filter;
    }

    class TextListFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            String charString = charSequence.toString();

            FilterResults filterResults = null;
            try {
                if (charString.isEmpty()) {
                    data = filteredData;
                } else {
                    ArrayList<JobNoModel> list = new ArrayList<>();

                    for (JobNoModel lists : filteredData) {
                        if (lists.getJobNo().toLowerCase().contains(charString.toLowerCase())) {
                            list.add(lists);
                        }
                    }
                    data = list;
                }

                filterResults = new FilterResults();
                filterResults.values = data;
            } catch (Exception e) {
                e.printStackTrace();
            }

            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            data = (List<JobNoModel>) filterResults.values;
            notifyDataSetChanged();
        }
    }

}
