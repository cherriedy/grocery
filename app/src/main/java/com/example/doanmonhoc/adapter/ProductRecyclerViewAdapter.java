package com.example.doanmonhoc.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;

import com.example.doanmonhoc.model.Product;
import com.example.doanmonhoc.R;

import java.util.List;

public class ProductRecyclerViewAdapter extends RecyclerView.Adapter<ProductRecyclerViewAdapter.ProductListViewHolder> {

    private final Context context;
    private List<Product> productList;              // Lưu data để binding

    public ProductRecyclerViewAdapter(Context context) {
        this.context = context;
    }

    // Gán data cho recyclerview
    @SuppressLint("NotifyDataSetChanged")
    public void setData(List<Product> productList) {
        this.productList = productList;
        notifyDataSetChanged();             // Thông báo data đã được thay đổi
    }

    @NonNull
    @Override
    public ProductListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Tạo đối tượng View từ product_card_item layout, cho các item của list
        View view = LayoutInflater.from(context).inflate(R.layout.product_card_item, parent, false);
        // Trả về viewholder
        return new ProductListViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ProductListViewHolder holder, int position) {
        // Lấy đối tượng product tương ứng với vị trí dòng (row) hiện tại trong list
        Product product = productList.get(position);
        if (product == null) {
            return;
        }

        // Gán dữ liệu các view trong layout
        holder.productName.setText(product.getProductName());
        holder.productPrice.setText(String.valueOf(product.getOutPrice()));
    }

    @Override
    public int getItemCount() {
        // Kiểm tra list có rỗng hay không, nếu không trả về size. Nếu có trả về 0
        if (productList != null) {
            return productList.size();
        }
        return 0;
    }

    public static class ProductListViewHolder extends ViewHolder {
//        private final ImageView productAvatar;
        private final TextView productName;
        private final TextView productPrice;

        public ProductListViewHolder(@NonNull View itemView) {
            super(itemView);

//            productAvatar = itemView.findViewById(R.id.product_avatar);
            productName = itemView.findViewById(R.id.product_name);
            productPrice = itemView.findViewById(R.id.product_price);
        }
    }
}
