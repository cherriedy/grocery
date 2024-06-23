package com.example.doanmonhoc.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doanmonhoc.R;
import com.example.doanmonhoc.model.Brand;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.List;

public class ProductBrandRecyclerViewAdapter extends RecyclerView.Adapter<ProductBrandRecyclerViewAdapter.ProductBrandViewHolder> {
    public interface OnItemClickListener {
        void itemClickListener(Brand brand);
    }

    private final Context context;
    private List<Brand> brandList;
    private OnItemClickListener onItemClickListener;

    public ProductBrandRecyclerViewAdapter(Context context) {
        this.context = context;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setData(List<Brand> brandList) {
        this.brandList = brandList;
        notifyDataSetChanged();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public ProductBrandViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.product_brand_or_group_list_item, parent, false);
        return new ProductBrandViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ProductBrandViewHolder holder, int position) {
        try {
            Brand currentBrand = brandList.get(position);
            holder.textProductBrandKey.setText(currentBrand.getBrandKey());
            holder.textProductBrandName.setText(currentBrand.getBrandName());
            holder.itemView.setOnClickListener(v -> {
                if (onItemClickListener != null) {
                    onItemClickListener.itemClickListener(currentBrand);
                }
            });
        } catch (Exception e) {
            holder.textProductBrandName.setText("<NULL>");
            holder.textProductBrandName.setText("<NULL>");
            holder.imageProductBrandAvatar.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.cat));
        }
    }

    @Override
    public int getItemCount() {
        if (brandList != null) {
            return brandList.size();
        }
        return 0;
    }

    public static class ProductBrandViewHolder extends RecyclerView.ViewHolder {
        private ShapeableImageView imageProductBrandAvatar;
        private TextView textProductBrandName;
        private TextView textProductBrandKey;

        public ProductBrandViewHolder(@NonNull View itemView) {
            super(itemView);
            imageProductBrandAvatar = itemView.findViewById(R.id.image_item_avatar);
            textProductBrandName = itemView.findViewById(R.id.text_item_name);
            textProductBrandKey = itemView.findViewById(R.id.text_item_key);
        }
    }
}
