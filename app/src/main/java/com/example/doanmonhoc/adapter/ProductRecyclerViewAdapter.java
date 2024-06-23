package com.example.doanmonhoc.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doanmonhoc.R;
import com.example.doanmonhoc.model.Product;

import java.util.List;

public class ProductRecyclerViewAdapter extends RecyclerView.Adapter<ProductRecyclerViewAdapter.ProductItemViewHolder> {
    private final Context context;
    private final OnItemClickListener onItemClickListener;
    private List<Product> productList;              // Lưu data để binding
    public ProductRecyclerViewAdapter(Context context, OnItemClickListener onItemClickListener) {
        this.context = context;
        this.onItemClickListener = onItemClickListener;
    }

    // Gán data cho recyclerview
    @SuppressLint("NotifyDataSetChanged")
    public void setData(List<Product> productList) {
        this.productList = productList;
        notifyDataSetChanged();             // Thông báo data đã được thay đổi
    }

    @NonNull
    @Override
    public ProductItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Tạo đối tượng View từ product_card_item layout, cho các item của list
        View view = LayoutInflater.from(context).inflate(R.layout.product_card_item, parent, false);
        // Trả về viewholder
        return new ProductItemViewHolder(view, onItemClickListener);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ProductItemViewHolder holder, int position) {
        // Lấy đối tượng product tương ứng với vị trí dòng (row) hiện tại trong list
        Product product = productList.get(position);
        if (product == null) {
            return;
        }

        if (position == productList.size() - 1) {
            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) holder.itemView.getLayoutParams();
            params.bottomMargin = 250; // last item bottom margin
            holder.itemView.setLayoutParams(params);
        }

        // Gán dữ liệu các view trong layout
        holder.productName.setText(product.getProductName());
        holder.productPrice.setText(String.valueOf(product.getOutPrice() + " đ"));
        holder.productQuantity.setText(String.valueOf(product.getActualQuantity()));

        holder.itemView.setOnClickListener(v -> {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(product);
            }
        });
    }

    @Override
    public int getItemCount() {
        // Kiểm tra list có rỗng hay không, nếu không trả về size. Nếu có trả về 0
        if (productList != null) {
            return productList.size();
        }
        return 0;
    }

    public interface OnItemClickListener {
        void onItemClick(Product product);
    }

    public static class ProductItemViewHolder extends RecyclerView.ViewHolder {
        //        private final ImageView productAvatar;
        private final TextView productName;
        private final TextView productPrice;
        private final TextView productQuantity;

        public ProductItemViewHolder(@NonNull View itemView, OnItemClickListener onItemClickListener) {
            super(itemView);
//            productAvatar = itemView.findViewById(R.id.product_avatar);
            productName = itemView.findViewById(R.id.text_name);
            productPrice = itemView.findViewById(R.id.text_price);
            productQuantity = itemView.findViewById(R.id.text_quantity);
        }
    }
}
