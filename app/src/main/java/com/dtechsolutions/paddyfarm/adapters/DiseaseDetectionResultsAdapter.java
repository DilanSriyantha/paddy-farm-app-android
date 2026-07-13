package com.dtechsolutions.paddyfarm.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dtechsolutions.paddyfarm.R;
import com.dtechsolutions.paddyfarm.data.models.DiseaseDetectionResult;
import com.dtechsolutions.paddyfarm.utils.ImageLoader;

import java.text.SimpleDateFormat;
import java.util.List;

public class DiseaseDetectionResultsAdapter extends RecyclerView.Adapter<DiseaseDetectionResultsAdapter.DiseaseDetectionResultViewHolder> {
    private final Context ctx;
    private List<DiseaseDetectionResult> list;

    public DiseaseDetectionResultsAdapter(Context ctx) {
        this.ctx = ctx;
    }

    public DiseaseDetectionResultsAdapter(Context ctx, List<DiseaseDetectionResult> list) {
        this.ctx = ctx;
        this.list = list;
    }

    public void setList(List<DiseaseDetectionResult> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public DiseaseDetectionResultsAdapter.DiseaseDetectionResultViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.detection_history_element, parent, false);
        return new DiseaseDetectionResultViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DiseaseDetectionResultsAdapter.DiseaseDetectionResultViewHolder holder, int position) {
        DiseaseDetectionResult result = list.get(position);

        ImageLoader.getInstance().loadImage(ctx, holder.imgPreview, result.getImageUrl());
        holder.txtDisease.setText(result.getDisease());
        holder.txtScientificName.setText(result.getDiseaseScientificName());

        switch (result.getRiskLevel()){
            case LOW:
                holder.riskLevelBadge.setBackgroundResource(R.drawable.badge_2);
                holder.txtRiskLevel.setTextColor(ctx.getColor(R.color.primary));
                holder.txtRiskLevel.setText(ctx.getString(R.string.low_risk));
                break;

            case MEDIUM:
                holder.riskLevelBadge.setBackgroundResource(R.drawable.badge_warning);
                holder.txtRiskLevel.setTextColor(ctx.getColor(R.color.warning));
                holder.txtRiskLevel.setText(ctx.getString(R.string.medium_risk));
                break;

            case HIGH:
                holder.riskLevelBadge.setBackgroundResource(R.drawable.badge_error);
                holder.txtRiskLevel.setTextColor(ctx.getColor(R.color.error));
                holder.txtRiskLevel.setText(ctx.getString(R.string.high_risk));
                break;
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        String date = sdf.format(result.getCreatedAt());
        holder.txtDate.setText(date);
    }

    @Override
    public int getItemCount() {
        return list != null ? list.size() : 0;
    }

    public static class DiseaseDetectionResultViewHolder extends RecyclerView.ViewHolder {
        ImageView imgPreview;
        TextView txtDisease;
        TextView txtScientificName;
        TextView txtRiskLevel;
        TextView txtDate;
        LinearLayout riskLevelBadge;

        public DiseaseDetectionResultViewHolder(@NonNull View itemView) {
            super(itemView);

            imgPreview = itemView.findViewById(R.id.imgHistoryElementPreview);
            txtDisease = itemView.findViewById(R.id.txtDiseaseHistoryElement);
            txtScientificName = itemView.findViewById(R.id.txtDiseaseScientificNameHistoryElement);
            txtRiskLevel = itemView.findViewById(R.id.txtRiskLevel);
            txtDate = itemView.findViewById(R.id.txtDateHistoryElement);
            riskLevelBadge = itemView.findViewById(R.id.riskLevelBadge);
        }
    }
}
