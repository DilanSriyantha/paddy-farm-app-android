package com.dtechsolutions.paddyfarm.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dtechsolutions.paddyfarm.R;
import com.dtechsolutions.paddyfarm.data.models.FertilizerPrice;
import com.dtechsolutions.paddyfarm.utils.ImageLoader;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class FertilizerPricesAdapter extends RecyclerView.Adapter<FertilizerPricesAdapter.FertilizerPriceViewHolder> {
    private final Context ctx;
    private final List<FertilizerPrice> list;
    private final ImageLoader imageLoader;
    private final NumberFormat lkFormat;

    public FertilizerPricesAdapter(Context ctx, List<FertilizerPrice> list) {
        this.ctx = ctx;
        this.list = list;
        this.imageLoader = ImageLoader.getInstance();
        lkFormat = NumberFormat.getCurrencyInstance(new Locale("en", "LK"));
    }

    @NonNull
    @Override
    public FertilizerPricesAdapter.FertilizerPriceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fertilizer_price_element, parent, false);

        return new FertilizerPriceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FertilizerPricesAdapter.FertilizerPriceViewHolder holder, int position) {
        FertilizerPrice fertilizerPrice = list.get(position);

        imageLoader.loadImage(ctx, holder.imgFertilizerImage, fertilizerPrice.getImageUrl());
        holder.txtFertilizerName.setText(fertilizerPrice.getName());
        holder.txtFertilizerPrice.setText(lkFormat.format(fertilizerPrice.getPrice()));
    }

    @Override
    public int getItemCount() {
        return list != null ? list.size() : 0;
    }

    public static class FertilizerPriceViewHolder extends RecyclerView.ViewHolder {
        ImageView imgFertilizerImage;
        TextView txtFertilizerName, txtFertilizerPrice;

        public FertilizerPriceViewHolder(@NonNull View itemView) {
            super(itemView);
            imgFertilizerImage = itemView.findViewById(R.id.imgFertilizerImage);
            txtFertilizerName = itemView.findViewById(R.id.txtFertilizerName);
            txtFertilizerPrice = itemView.findViewById(R.id.txtFertilizerPrice);
        }
    }
}
