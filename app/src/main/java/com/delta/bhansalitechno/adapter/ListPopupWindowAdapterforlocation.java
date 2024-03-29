package com.delta.bhansalitechno.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.delta.bhansalitechno.R;
import com.delta.bhansalitechno.model.LocationModel;

import java.util.List;

public class ListPopupWindowAdapterforlocation extends BaseAdapter {
    private List<LocationModel> items;

    public ListPopupWindowAdapterforlocation(List<LocationModel> items) {
        this.items = items;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public LocationModel getItem(int i) {
        return items.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ListPopupWindowAdapter.ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.listitems_layout, null);
            holder = new ListPopupWindowAdapter.ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ListPopupWindowAdapter.ViewHolder) convertView.getTag();
        }

        holder.tvTitle.setText(getItem(position).getName());
        return convertView;
    }

    static class ViewHolder {
        TextView tvTitle;

        ViewHolder(View view) {
            tvTitle = view.findViewById(R.id.title);
        }
    }
}
