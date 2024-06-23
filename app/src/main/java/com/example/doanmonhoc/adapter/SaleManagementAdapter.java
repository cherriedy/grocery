package com.example.doanmonhoc.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

import com.example.doanmonhoc.R;
import com.example.doanmonhoc.activity.SaleManagement.SaleDetailedInvoiceActivity;
import com.example.doanmonhoc.model.Invoice;
import com.example.doanmonhoc.model.Product;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class SaleManagementAdapter extends BaseAdapter {
    private Context context;
    private List<Invoice> list;

    public SaleManagementAdapter(Context context, List<Invoice> list) {
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
        return position;
    }

    public static class ViewHolder{
        TextView txtHD, txtDate, txtTotalAmount;
        CardView cardView;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.sales_item, parent, false);

            holder = new ViewHolder();
            holder.txtHD = convertView.findViewById(R.id.maHD);
            holder.txtDate = convertView.findViewById(R.id.dateTime);
            holder.txtTotalAmount = convertView.findViewById(R.id.totalAmount);
            holder.cardView = convertView.findViewById(R.id.cardView);
            convertView.setTag(holder);
        } else{
            holder = (ViewHolder) convertView.getTag();
        }

        Invoice invoice = list.get(position);
        holder.txtHD.setText(invoice.getInvoiceKey());

        Timestamp timestamp = invoice.getCreatedAt();
        // Convert Timestamp to String
        String timestampString = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US).format(timestamp);
        holder.txtDate.setText(timestampString);
        holder.txtTotalAmount.setText(String.valueOf(invoice.getTotalAmount()));

        holder.cardView.setOnClickListener(v -> {
            Intent intent = new Intent(context, SaleDetailedInvoiceActivity.class);
            intent.putExtra("invoiceId", invoice.getId());
            intent.putExtra("invoiceKey", invoice.getInvoiceKey());
            intent.putExtra("staffId", invoice.getStaffid());
            intent.putExtra("createAt", invoice.getCreatedAt().getTime());
            intent.putExtra("totalAmount", invoice.getTotalAmount());
            intent.putExtra("note", invoice.getNote());
            context.startActivity(intent);
        });


        return convertView;
    }
}
