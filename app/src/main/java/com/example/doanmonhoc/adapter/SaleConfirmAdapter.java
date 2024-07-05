package com.example.doanmonhoc.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;

import com.example.doanmonhoc.R;
import com.example.doanmonhoc.model.CartItem;
import com.example.doanmonhoc.model.Product;
import com.squareup.picasso.Picasso;

import java.util.List;

public class SaleConfirmAdapter extends BaseAdapter {
    private Context context;
    private List<CartItem> list;
    private TextView tvTongTien, tvTongSoLuong,tvTongGiam, tvThanhTien;

    public SaleConfirmAdapter(Context context, List<CartItem> list, TextView tvTongTien, TextView tvTongSoLuong, TextView tvTongGiam, TextView tvThanhTien) {
        this.context = context;
        this.list = list;
        this.tvTongTien = tvTongTien;
        this.tvTongSoLuong = tvTongSoLuong;
        this.tvTongGiam = tvTongGiam;
        this.tvThanhTien = tvThanhTien;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return list.get(position).getProduct().getId();
    }

    public static class ViewHolder{
        TextView tvQuantity, tvName, tvPrice;
        ImageButton btnPlus, btnMinus;
        ImageView imageView;
        LinearLayout cardView;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.sales_confirm_item, parent, false);

            holder = new ViewHolder();
            holder.tvName = convertView.findViewById(R.id.tvName);
            holder.tvPrice = convertView.findViewById(R.id.tvPrice);
            holder.tvQuantity = convertView.findViewById(R.id.tvQuantity);
            holder.btnPlus = convertView.findViewById(R.id.btnPlus);
            holder.btnMinus = convertView.findViewById(R.id.btnMinus);
            holder.cardView = convertView.findViewById(R.id.cardView);
            holder.imageView = convertView.findViewById(R.id.imageView);

            convertView.setTag(holder);
        } else{
            holder = (ViewHolder) convertView.getTag();
        }

        CartItem cartItem = list.get(position);
        Product product = cartItem.getProduct();
        if (product.getAvatarPath() != null) {
            Picasso.get().load(product.getAvatarPath()).into(holder.imageView);
        }

        holder.tvName.setText(product.getProductName());
        holder.tvPrice.setText(String.valueOf(cartItem.getPrice()));
        holder.tvQuantity.setText(String.valueOf(cartItem.getQuantity()));

        holder.btnPlus.setOnClickListener(v -> {
            int currentQuantity = Integer.parseInt(holder.tvQuantity.getText().toString());
            if (checkProductStock(product.getId(), currentQuantity + 1)) {
                holder.tvQuantity.setText(String.valueOf(currentQuantity + 1));
                updateCart(position, currentQuantity + 1);
            } else {
                Toast.makeText(context, "Số lượng tồn kho không đủ", Toast.LENGTH_SHORT).show();
            }
        });

        holder.btnMinus.setOnClickListener(v -> {
            int currentQuantity = Integer.parseInt(holder.tvQuantity.getText().toString());
            if (currentQuantity > 0) {
                holder.tvQuantity.setText(String.valueOf(currentQuantity - 1));
                updateCart(position, currentQuantity - 1);
            }
        });
        return convertView;
    }
    private boolean checkProductStock(long productId, int requiredQuantity) {
        // Logic để kiểm tra số lượng tồn kho
        for (CartItem item : list) {
            Product product = item.getProduct();
            if (product.getId() == productId) {
                return product.getInventoryQuantity() >= requiredQuantity;
            }
        }
        return false;
    }
    private void updateCart(int position, int quantity) {
        CartItem cartItem = list.get(position);
        Product product = cartItem.getProduct();
        float discountedPrice = product.getOutPrice() * (1 - product.getDiscount());

        if (quantity > 0) {
            cartItem.setQuantity(quantity);
            cartItem.setPrice(quantity * discountedPrice);
        } else {
            list.remove(position);
        }

        notifyDataSetChanged();
        updateTotalUI();
    }



    private void updateTotalUI() {
        int totalQuantity = 0;
        double totalAmount = 0;
        double totalDiscount = 0;
        double originalTotal = 0;

        for (CartItem item : list) {
            int quantity = item.getQuantity();
            Product product = item.getProduct();
            double originalPrice = product.getOutPrice() * quantity;
            double discountedPrice = item.getPrice();
            double discountAmount = originalPrice - discountedPrice;

            totalQuantity += quantity;
            originalTotal += originalPrice;
            totalAmount += discountedPrice;
            totalDiscount += discountAmount;
        }

        tvTongSoLuong.setText(totalQuantity + " sản phẩm");
        tvTongTien.setText(String.format("%.2f đ", originalTotal));
        tvTongGiam.setText(String.format("%.2f đ", totalDiscount));
        tvThanhTien.setText(String.format("%.2f đ", totalAmount));
    }
}
