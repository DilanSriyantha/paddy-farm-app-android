package com.dtechsolutions.paddyfarm.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dtechsolutions.paddyfarm.R;
import com.dtechsolutions.paddyfarm.data.models.Cultivation;

import java.text.SimpleDateFormat;
import java.util.List;

public class CultivationHistoryAdapter extends RecyclerView.Adapter<CultivationHistoryAdapter.CultivationHistoryViewHolder> {
    private Context context;
    private List<Cultivation> list;

    public CultivationHistoryAdapter(Context context) {
        this.context = context;
    }

    public CultivationHistoryAdapter(Context context, List<Cultivation> list) {
        this.context = context;
        this.list = list;
    }

    public void setList(List<Cultivation> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CultivationHistoryAdapter.CultivationHistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.cultivation_history_element_layout,
                parent,
                false
        );

        return new CultivationHistoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CultivationHistoryAdapter.CultivationHistoryViewHolder holder, int position) {
        Cultivation cultivation = list.get(position);

        String id = "#" + cultivation.getId();
        holder.txtId.setText(id);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        holder.txtStartDate.setText(sdf.format(cultivation.getStartDate()));

        holder.txtSeedType.setText(cultivation.getSeedType());
        holder.txtSizeInAcres.setText(String.valueOf(cultivation.getSizeInAcres()));
        holder.txtPaddyVariety.setText(String.valueOf(cultivation.getPaddyVariety()));

        switch (cultivation.getStatus()) {
            case ACTIVE:
                holder.txtStatus.setText(context.getString(R.string.active));
                holder.txtStatus.setTextColor(context.getColor(R.color.primary));
                holder.statusBadge.setBackgroundResource(R.drawable.badge_2);
                break;

            case COMPLETED:
                holder.txtStatus.setText(context.getString(R.string.completed));
                holder.txtStatus.setTextColor(context.getColor(R.color.warning));
                holder.statusBadge.setBackgroundResource(R.drawable.badge_warning);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return list != null ? list.size() : 0;
    }

    public static class CultivationHistoryViewHolder extends RecyclerView.ViewHolder {
        TextView txtId;
        TextView txtStartDate;
        TextView txtSeedType;
        TextView txtSizeInAcres;
        TextView txtPaddyVariety;
        TextView txtStatus;
        LinearLayout statusBadge;

        public CultivationHistoryViewHolder(@NonNull View itemView) {
            super(itemView);

            txtId = itemView.findViewById(R.id.txtId);
            txtStartDate = itemView.findViewById(R.id.txtStartDate);
            txtSeedType = itemView.findViewById(R.id.txtSeedType);
            txtSizeInAcres = itemView.findViewById(R.id.txtSizeInAcres);
            txtPaddyVariety = itemView.findViewById(R.id.txtPaddyVariety);
            txtStatus = itemView.findViewById(R.id.txtStatus);
            statusBadge = itemView.findViewById(R.id.statusBadge);
        }
    }
}
