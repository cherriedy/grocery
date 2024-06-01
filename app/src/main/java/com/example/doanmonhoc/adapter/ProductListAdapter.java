package com.example.doanmonhoc.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;

import com.example.doanmonhoc.model.Product;
import com.example.doanmonhoc.R;

import java.util.List;

public class ProductListAdapter extends RecyclerView.Adapter<ProductListAdapter.ProductListViewHolder> {

    private Context context;
    private List<Product> productList;

    public ProductListAdapter(Context context) {
        this.context = context;
    }

    public void setData(List<Product> productList) {
        this.productList = productList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ProductListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_card_item, parent, false);
        return new ProductListViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ProductListViewHolder holder, int position) {
        Product product = productList.get(position);
        if (product == null) {
            return;
        }

//        holder.productAvatar.setImageResource(product.getAvatarPath());
        holder.productName.setText(product.getProductName());
        holder.productPrice.setText(Float.toString(product.getOutPrice()));
    }

    @Override
    public int getItemCount() {
        if (productList != null) {
            return productList.size();
        }
        return 0;
    }

    public static class ProductListViewHolder extends ViewHolder {

        private ImageView productAvatar;
        private TextView productName;
        private TextView productPrice;

        public ProductListViewHolder(@NonNull View itemView) {
            super(itemView);

            productAvatar = itemView.findViewById(R.id.product_avatar);
            productName = itemView.findViewById(R.id.product_name);
            productPrice = itemView.findViewById(R.id.product_price);
        }
    }
}
