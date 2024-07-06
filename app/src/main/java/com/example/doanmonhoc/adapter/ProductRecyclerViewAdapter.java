package com.example.doanmonhoc.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doanmonhoc.R;
import com.example.doanmonhoc.model.Product;
import com.example.doanmonhoc.utils.PicassoHelper;
import com.example.doanmonhoc.utils.TextUtils;
import com.squareup.picasso.Callback;

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
        // Lấy đối tượng currentProduct tương ứng với vị trí dòng (row) hiện tại trong list
        Product currentProduct = productList.get(position);
        if (currentProduct == null) {
            return;
        }

        if (position == productList.size() - 1) {
            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) holder.itemView.getLayoutParams();
            params.bottomMargin = 250; // last item bottom margin
            holder.itemView.setLayoutParams(params);
        }

        // Gán dữ liệu các view trong layout
        holder.productName.setText(currentProduct.getProductName());
        holder.productPrice.setText(currentProduct.getOutPrice() + " đ");
        holder.productQuantity.setText(String.valueOf(currentProduct.getActualQuantity()));

        String imagePath = currentProduct.getAvatarPath();
        if (!TextUtils.isValidString(imagePath)) {
            onFetchingImageFail(holder);
        } else {
            holder.productAvatar.setTag(imagePath);

            PicassoHelper.getPicassoInstance(context)
                    .load(imagePath)
                    .into(holder.productAvatar, new Callback() {
                        @Override
                        public void onSuccess() {
                            if (holder.productAvatar.getTag().equals(imagePath)) {
                                onFetchingImageSuccess(holder);
                            }
                        }

                        @Override
                        public void onError(Exception e) {
                            onFetchingImageFail(holder);
                        }
                    });
        }

        holder.itemView.setOnClickListener(v -> {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(currentProduct);
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

    private void onFetchingImageSuccess(ProductItemViewHolder holder) {
        holder.progressBar.setVisibility(View.GONE);
        holder.productAvatar.setVisibility(View.VISIBLE);
    }

    private void onFetchingImageFail(ProductItemViewHolder holder) {
        holder.productAvatar.setImageResource(R.drawable.img_no_image);
        holder.progressBar.setVisibility(View.GONE);
        holder.productAvatar.setVisibility(View.VISIBLE);
    }

    public interface OnItemClickListener {
        void onItemClick(Product product);
    }

    public static class ProductItemViewHolder extends RecyclerView.ViewHolder {
        private final ProgressBar progressBar;
        private final ImageView productAvatar;
        private final TextView productName;
        private final TextView productPrice;
        private final TextView productQuantity;

        public ProductItemViewHolder(@NonNull View itemView, OnItemClickListener onItemClickListener) {
            super(itemView);
            progressBar = itemView.findViewById(R.id.progress_bar);
            productAvatar = itemView.findViewById(R.id.image_avatar);
            productName = itemView.findViewById(R.id.text_name);
            productPrice = itemView.findViewById(R.id.text_price);
            productQuantity = itemView.findViewById(R.id.text_quantity);
        }
    }
}