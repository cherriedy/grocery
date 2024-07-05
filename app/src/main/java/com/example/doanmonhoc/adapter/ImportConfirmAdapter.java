package com.example.doanmonhoc.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.doanmonhoc.R;
import com.example.doanmonhoc.model.CartItem;
import com.example.doanmonhoc.model.Product;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ImportConfirmAdapter extends BaseAdapter {

    private Context context;
    private List<CartItem> list;
    private TextView tvTongSoLuong, tvThanhTien;

    public ImportConfirmAdapter(Context context, List<CartItem> list, TextView tvTongSoLuong, TextView tvThanhTien) {
        this.context = context;
        this.list = list;
        this.tvTongSoLuong = tvTongSoLuong;
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
        TextView tvName, tvInPrice, tvProductKey, tvAmount;
        EditText editQuantity;
        ImageButton btnPlus, btnMinus;
        ImageView imageView;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.import_confirm_item, parent, false);

            holder = new ViewHolder();
            holder.imageView = convertView.findViewById(R.id.imageView);
            holder.tvName = convertView.findViewById(R.id.tvName);
            holder.tvProductKey = convertView.findViewById(R.id.tvProductKey);
            holder.tvInPrice = convertView.findViewById(R.id.tvInPrice);
            holder.editQuantity = convertView.findViewById(R.id.tvQuantity);
            holder.btnPlus = convertView.findViewById(R.id.btnPlus);
            holder.btnMinus = convertView.findViewById(R.id.btnMinus);
            holder.tvAmount = convertView.findViewById(R.id.tvAmount);

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
        holder.tvProductKey.setText(product.getProductKey());
        holder.tvInPrice.setText(String.valueOf(product.getInPrice()));
        holder.editQuantity.setText(String.valueOf(cartItem.getQuantity()));
        holder.tvAmount.setText(String.valueOf(cartItem.getQuantity() * product.getInPrice()));

        holder.btnPlus.setOnClickListener(v -> {
            int currentQuantity = Integer.parseInt(holder.editQuantity.getText().toString());
            holder.editQuantity.setText(String.valueOf(currentQuantity + 1));
            updateCart(position, currentQuantity + 1);
        });

        holder.btnMinus.setOnClickListener(v -> {
            int currentQuantity = Integer.parseInt(holder.editQuantity.getText().toString());
            if (currentQuantity > 0) {
                holder.editQuantity.setText(String.valueOf(currentQuantity - 1));
                updateCart(position, currentQuantity - 1);
            }
        });

        // Set OnEditorActionListener to update the cart when the user presses "OK"
        holder.editQuantity.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                int enteredQuantity = Integer.parseInt(holder.editQuantity.getText().toString());
                updateCart(position, enteredQuantity);

                return true;
            }
            return false;
        });
        return convertView;
    }
    private void updateCart(int position, int quantity) {
        CartItem cartItem = list.get(position);
        Product product = cartItem.getProduct();

        if (quantity > 0) {
            cartItem.setQuantity(quantity);
            cartItem.setPrice(quantity * product.getInPrice());
        } else {
            list.remove(position);
        }
        notifyDataSetChanged();
        updateTotalUI();
    }

    private void updateTotalUI() {
        int totalQuantity = 0;
        double totalAmount = 0;

        for (CartItem item : list) {
            int quantity = item.getQuantity();
            double inPrice = item.getPrice();

            totalQuantity += quantity;
            totalAmount += inPrice;
        }

        tvTongSoLuong.setText(totalQuantity + " sản phẩm");
        tvThanhTien.setText(String.format("%.2f đ", totalAmount));
    }
}
