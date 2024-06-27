package com.example.doanmonhoc.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.doanmonhoc.R;
import com.example.doanmonhoc.activity.CreateImportProduct.ConfirmImportActivity;
import com.example.doanmonhoc.model.CartItem;
import com.example.doanmonhoc.model.DetailedGoodsReceivedNote;
import com.example.doanmonhoc.model.GoodsReceivedNote;
import com.example.doanmonhoc.model.ImportItem;
import com.example.doanmonhoc.model.Product;

import java.util.List;

public class ConfirmImportAdapter extends BaseAdapter {

    private Context context;
    private List<ImportItem>list;
    private List<Product> productList;
    private TextView tvTongTien, tvTongSoLuong, tvThanhTien;


    public ConfirmImportAdapter(Context context, List<ImportItem> list, TextView tvTongTien, TextView tvTongSoLuong, TextView tvThanhTien) {
        this.context = context;
        this.list = list;
        this.tvTongTien = tvTongTien;
        this.tvTongSoLuong = tvTongSoLuong;

        this.tvThanhTien = tvThanhTien;
    }

    @Override
    public int getCount() {
        return productList != null ? productList.size() : 0;
    }

    @Override
    public Object getItem(int position) {
        return productList != null ? productList.get(position) : null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    static class ViewHolder {
        ImageButton btnMinus, btnPlus;
        TextView productName;
        TextView quantity,price, totalPrice;
        ImageView image;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.import_confirm_item, parent, false);
            holder = new ViewHolder();
            holder.image = convertView.findViewById(R.id.image);
            holder.productName = convertView.findViewById(R.id.item_name);
            holder.btnMinus = convertView.findViewById(R.id.btn_minus);
            holder.quantity = convertView.findViewById(R.id.item_quantity);
            holder.btnPlus = convertView.findViewById(R.id.btn_plus);
            holder.price = convertView.findViewById(R.id.item_price);
            holder.totalPrice = convertView.findViewById(R.id.tv_totalPrice);
            convertView.setTag(holder);


        holder.btnPlus.setOnClickListener(v -> {
            int currentQuantity = Integer.parseInt(holder.quantity.getText().toString());
            holder.quantity.setText(String.valueOf(currentQuantity + 1));
            updateCart(position, currentQuantity + 1);
            updateTotalUI();
        });

        holder.btnMinus.setOnClickListener(v -> {
            int currentQuantity = Integer.parseInt(holder.quantity.getText().toString());
            if (currentQuantity > 0) {
                holder.quantity.setText(String.valueOf(currentQuantity - 1));
                updateCart(position, currentQuantity - 1);
                updateTotalUI();
            }
        });
        convertView.setTag(holder);
    } else{
        holder = (ConfirmImportAdapter.ViewHolder) convertView.getTag();
    }

    ImportItem importItem = list.get(position);
    Product product = importItem.getProduct();
    String imageName = product.getAvatarPath();
    // Lấy ID của tài nguyên drawable từ tên ảnh
    int resID = context.getResources().getIdentifier(imageName, "drawable", context.getPackageName());
        holder.image.setImageResource(resID);
        holder.productName.setText(product.getProductName());
        holder.price.setText(String.valueOf(importItem.getPrice()));
        holder.quantity.setText(String.valueOf(importItem.getQuantity()));
        return convertView;
}

    private void updateCart(int position, int quantity) {
        ImportItem importItem = list.get(position);
        Product product = importItem.getProduct();
        float discountedPrice = product.getOutPrice() * (1 - product.getDiscount());

        if (quantity > 0) {
            importItem.setQuantity(quantity);
            importItem.setPrice(quantity * discountedPrice);
        } else {
            list.remove(position);
        }

        notifyDataSetChanged();
    }



    private void updateTotalUI() {
        int totalQuantity = 0;
        double totalAmount = 0;
  //      double totalDiscount = 0;
        double originalTotal = 0;

        for (ImportItem item : list) {
            int quantity = item.getQuantity();
            Product product = item.getProduct();
            double originalPrice = product.getOutPrice() * quantity;
            double discountedPrice = item.getPrice();
            double discountAmount = originalPrice - discountedPrice;

            totalQuantity += quantity;
            originalTotal += originalPrice;
            totalAmount += discountedPrice;
        //    totalDiscount += discountAmount;
        }

        tvTongSoLuong.setText(totalQuantity + " sản phẩm");
        tvTongTien.setText(String.format("%.2f đ", originalTotal));
   //     tvTongGiam.setText(String.format("%.2f đ", totalDiscount));
        tvThanhTien.setText(String.format("%.2f đ", totalAmount));
    }

}
