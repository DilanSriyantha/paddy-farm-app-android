package com.dtechsolutions.paddyfarm.adapters;

import android.content.Context;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.dtechsolutions.paddyfarm.R;
import com.dtechsolutions.paddyfarm.data.models.LanguageItem;
import com.dtechsolutions.paddyfarm.enums.PreferredLanguage;

import java.util.List;

public class PreferredLanguagesArrayAdapter extends ArrayAdapter<LanguageItem> {
    private final LayoutInflater layoutInflater;
    private final int layoutResource;

    public PreferredLanguagesArrayAdapter(@NonNull Context context, @NonNull List<LanguageItem> languagesList) {
        super(context, R.layout.single_line_dropdown_item, languagesList);
        this.layoutInflater = LayoutInflater.from(context);
        this.layoutResource = R.layout.single_line_dropdown_item;
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

    public PreferredLanguage getPreferredLanguage(String language) {
        if(language == null) return null;

        for(int i = 0; i < getCount(); i++) {
            LanguageItem item = getItem(i);
            if(item == null) continue;
            if(item.getCaption().equals(language)) return item.getPreferredLanguage();
        }

        return null;
    }

    public String getLanguageString(PreferredLanguage preferredLanguage) {
        if(preferredLanguage == null) return null;

        for(int i = 0; i < getCount(); i++) {
           LanguageItem item = getItem(i);
            if(item == null) continue;
            if(item.getPreferredLanguage() == preferredLanguage) return item.getCaption();
        }

        return null;
    }

    private View createViewFromResource(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder holder;

        if(convertView == null) {
            convertView = layoutInflater.inflate(layoutResource, parent, false);
            holder = new ViewHolder();
            holder.txtLine = convertView.findViewById(R.id.txtLine);

            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }

        LanguageItem language = getItem(position);

        if(language != null) {
            holder.txtLine.setText(language.getCaption());
        }

        return convertView;
    }

    private static class ViewHolder {
        TextView txtLine;
    }
}
