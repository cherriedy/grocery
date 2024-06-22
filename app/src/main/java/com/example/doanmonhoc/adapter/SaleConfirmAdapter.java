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

import androidx.cardview.widget.CardView;

import com.example.doanmonhoc.R;
import com.example.doanmonhoc.model.CartItem;
import com.example.doanmonhoc.model.Product;

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


            holder.btnPlus.setOnClickListener(v -> {
                int currentQuantity = Integer.parseInt(holder.tvQuantity.getText().toString());
                holder.tvQuantity.setText(String.valueOf(currentQuantity + 1));
                updateCart(position, currentQuantity + 1);
                updateTotalUI();
            });

            holder.btnMinus.setOnClickListener(v -> {
                int currentQuantity = Integer.parseInt(holder.tvQuantity.getText().toString());
                if (currentQuantity > 0) {
                    holder.tvQuantity.setText(String.valueOf(currentQuantity - 1));
                    updateCart(position, currentQuantity - 1);
                    updateTotalUI();
                }
            });
            convertView.setTag(holder);
        } else{
            holder = (ViewHolder) convertView.getTag();
        }

        CartItem cartItem = list.get(position);
        Product product = cartItem.getProduct();
        String imageName = product.getAvatarPath();
        // Lấy ID của tài nguyên drawable từ tên ảnh
        int resID = context.getResources().getIdentifier(imageName, "drawable", context.getPackageName());
        holder.imageView.setImageResource(resID);

        holder.tvName.setText(product.getProductName());
        holder.tvPrice.setText(String.valueOf(cartItem.getPrice()));
        holder.tvQuantity.setText(String.valueOf(cartItem.getQuantity()));
        return convertView;
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
