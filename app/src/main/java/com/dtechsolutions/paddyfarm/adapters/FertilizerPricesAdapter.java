package com.dtechsolutions.paddyfarm.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dtechsolutions.paddyfarm.R;
import com.dtechsolutions.paddyfarm.data.api.ApiClient;
import com.dtechsolutions.paddyfarm.data.models.Fertilizer;
import com.dtechsolutions.paddyfarm.utils.ImageLoader;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class FertilizerPricesAdapter extends RecyclerView.Adapter<FertilizerPricesAdapter.FertilizerPriceViewHolder> implements Filterable {
    private final Context ctx;
    private final ImageLoader imageLoader;
    private final NumberFormat lkFormat;

    private List<Fertilizer> list;
    private List<Fertilizer> listFull;

    public FertilizerPricesAdapter(Context ctx, List<Fertilizer> list) {
        this.ctx = ctx;
        this.list = list;
        this.listFull = new ArrayList<>(list);
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
        Fertilizer fertilizerPrice = list.get(position);

        String imageUrl = ApiClient.baseUrl + fertilizerPrice.getImgPath();
        imageLoader.loadImage(ctx, holder.imgFertilizerImage, imageUrl);
        holder.txtFertilizerName.setText(fertilizerPrice.getName());
        holder.txtFertilizerPrice.setText(lkFormat.format(fertilizerPrice.getPricePerKg()));
    }

    @Override
    public int getItemCount() {
        return list != null ? list.size() : 0;
    }

    @Override
    public Filter getFilter() {
        return filter;
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

    private final Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Fertilizer> filteredList = new ArrayList<>();

            if(constraint == null || constraint.length() == 0) {
                filteredList.addAll(listFull);
            }else{
                String filterPattern = constraint.toString().toLowerCase().trim();

                for(Fertilizer item : listFull) {
                    if(item.getName().toLowerCase().contains(filterPattern)) {
                        filteredList.add(item);
                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values = filteredList;

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            list.clear();
            list.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };

    public void updateData(List<Fertilizer> newList) {
        this.list = newList;
        this.listFull = new ArrayList<>(newList);
        notifyDataSetChanged();
    }
}
