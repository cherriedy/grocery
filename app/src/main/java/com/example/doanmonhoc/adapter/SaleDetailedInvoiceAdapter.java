package com.example.doanmonhoc.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.doanmonhoc.R;
import com.example.doanmonhoc.model.DetailedInvoice;
import com.example.doanmonhoc.model.Product;

import java.util.List;

public class SaleDetailedInvoiceAdapter extends BaseAdapter {
    private Context context;
    private List<DetailedInvoice> list;

    public SaleDetailedInvoiceAdapter(Context context, List<DetailedInvoice> list) {
        this.context = context;
        this.list = list;
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
        return list.get(position).getId();
    }

    public static class ViewHolder{
        TextView tvQuantity, tvName, tvPrice, tvAmount;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.sales_bill_item, parent, false);

            holder = new ViewHolder();
            holder.tvName = convertView.findViewById(R.id.tvName);
            holder.tvPrice = convertView.findViewById(R.id.tvPrice);
            holder.tvQuantity = convertView.findViewById(R.id.tvQuantity);
            holder.tvAmount = convertView.findViewById(R.id.tvAmount);
            convertView.setTag(holder);
        } else{
            holder = (ViewHolder) convertView.getTag();
        }

        DetailedInvoice detail = list.get(position);
        Product product = detail.getProduct();
        holder.tvName.setText(product.getProductName());
        holder.tvQuantity.setText(String.valueOf(detail.getQuantity())); // Ensure to convert int to String
        holder.tvPrice.setText(String.valueOf(product.getOutPrice() * (1 - product.getDiscount())));
        holder.tvAmount.setText(String.valueOf(detail.getPrice()));

        return convertView;
    }
}
