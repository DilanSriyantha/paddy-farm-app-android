package com.dtechsolutions.paddyfarm.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.dtechsolutions.paddyfarm.R;

import java.util.List;

public class PaddyVarietyArrayAdapter extends ArrayAdapter<String> {
    private final String TAG = "[PaddyVarietyAdapter]";

    private final LayoutInflater inflater;
    private final int layoutResource;

    public PaddyVarietyArrayAdapter(@NonNull Context context, @NonNull List<String> paddyVarietyList) {
        super(context, R.layout.paddy_veriety_dropdown_item, paddyVarietyList);
        this.inflater = LayoutInflater.from(context);
        this.layoutResource = R.layout.paddy_veriety_dropdown_item;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return createViewFromResource(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return createViewFromResource(position, convertView, parent);
    }

    private View createViewFromResource(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder holder;

        if(convertView == null) {
            convertView = inflater.inflate(layoutResource, parent, false);
            holder = new ViewHolder();
            holder.txtTitle = convertView.findViewById(R.id.txtPaddyVarietyTitle);

            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }

        String paddyVarietyTitle = getItem(position);

        if(paddyVarietyTitle != null) {
            holder.txtTitle.setText(paddyVarietyTitle);
        }

        return convertView;
    }

    private static class ViewHolder {
        TextView txtTitle;
    }
}
