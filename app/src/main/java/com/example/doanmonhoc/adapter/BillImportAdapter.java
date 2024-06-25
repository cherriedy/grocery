package com.example.doanmonhoc.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.doanmonhoc.R;
import com.example.doanmonhoc.model.Product;

import java.util.List;

public class BillImportAdapter extends BaseAdapter {

    private Context context;
    private List<Product> productList;

    public BillImportAdapter(Context context, List<Product> productList) {
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
        TextView productName;
        TextView quantity, price, totalPrice, stt;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        BillImportAdapter.ViewHolder holder;
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.import_bill_item, parent, false);
            holder = new BillImportAdapter.ViewHolder();
            holder.stt = convertView.findViewById(R.id.tv_stt);
            holder.productName = convertView.findViewById(R.id.item_name);
            holder.quantity = convertView.findViewById(R.id.item_quantity);
            holder.price = convertView.findViewById(R.id.item_price);
            holder.totalPrice = convertView.findViewById(R.id.item_totalprice);
            convertView.setTag(holder);
        } else {
            holder = (BillImportAdapter.ViewHolder) convertView.getTag();
        }

        final Product product = productList.get(position);
        holder.stt.setText(String.format("%d", position + 1));
        holder.quantity.setText(String.valueOf(product.getActualQuantity()));
        holder.productName.setText(product.getProductName());
        holder.price.setText(String.valueOf(product.getInPrice()));

        double totalPrice = product.getActualQuantity() * product.getInPrice();
        holder.totalPrice.setText(String.format("%.0f", totalPrice));

        return convertView;
    }

}
