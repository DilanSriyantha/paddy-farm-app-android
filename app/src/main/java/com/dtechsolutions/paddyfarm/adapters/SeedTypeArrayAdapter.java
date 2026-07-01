package com.dtechsolutions.paddyfarm.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.dtechsolutions.paddyfarm.R;
import com.dtechsolutions.paddyfarm.data.models.SeedType;

import java.util.List;

public class SeedTypeArrayAdapter extends ArrayAdapter<SeedType> {
    private final LayoutInflater inflater;
    private final int layoutResource;

    public SeedTypeArrayAdapter(@NonNull Context context, @NonNull List<SeedType> seedTypeList) {
        super(context, R.layout.seed_type_dropdown_item, seedTypeList);
        this.inflater = LayoutInflater.from(context);
        this.layoutResource = R.layout.seed_type_dropdown_item;
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
            holder.txtTitle = convertView.findViewById(R.id.txtSeedTypeTitle);
            holder.txtDescription = convertView.findViewById(R.id.txtSeedTypeDescription);

            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }

        SeedType seedType = getItem(position);

        if(seedType != null) {
            holder.txtTitle.setText(seedType.getTitle());
            holder.txtDescription.setText(seedType.getDescription());
        }

        return convertView;
    }

    private static class ViewHolder {
        TextView txtTitle;
        TextView txtDescription;
    }
}
