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
import com.example.doanmonhoc.model.DetailedGoodsReceivedNote;
import com.example.doanmonhoc.model.GoodsReceivedNote;
import com.example.doanmonhoc.model.Product;

import java.util.List;

public class ConfirmImportAdapter extends BaseAdapter {

    private Context context;
    private List<Product> productList;

    public ConfirmImportAdapter(Context context, List<Product> productList) {
        this.context = context;
        this.productList = productList;
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
        ImageButton btnMinus, btnAdd;
        TextView productName;
        TextView quantity,price, totalPrice;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.import_confirm_item, parent, false);
            holder = new ViewHolder();
            holder.productName = convertView.findViewById(R.id.item_name);
            holder.btnMinus = convertView.findViewById(R.id.btn_minus);
            holder.quantity = convertView.findViewById(R.id.item_quantity);
            holder.btnAdd = convertView.findViewById(R.id.btn_plus);
            holder.price = convertView.findViewById(R.id.item_price);
            holder.totalPrice = convertView.findViewById(R.id.tv_totalPrice);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final Product product = productList.get(position);
        Integer quantity = 0;
        holder.quantity.setText(String.valueOf(quantity));
        holder.productName.setText(product.getProductName());
        holder.price.setText(String.valueOf(product.getInPrice()));

        double totalPrice = quantity * product.getInPrice();
        holder.totalPrice.setText(String.format("%.0f", totalPrice));

        holder.btnMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int quantity = Integer.parseInt(holder.quantity.getText().toString());
                if (quantity > 0) {
                    quantity -= 1;
                    holder.quantity.setText(String.valueOf(quantity));
                    double totalPrice = quantity * product.getInPrice();
                    holder.totalPrice.setText(String.format("%.0f", totalPrice));
                }
            }
        });

        holder.btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int quantity = Integer.parseInt(holder.quantity.getText().toString());
                quantity += 1;
                holder.quantity.setText(String.valueOf(quantity));
                double totalPrice = quantity * product.getInPrice();
                holder.totalPrice.setText(String.format("%.0f", totalPrice));
            }
        });

        return convertView;
    }

}
