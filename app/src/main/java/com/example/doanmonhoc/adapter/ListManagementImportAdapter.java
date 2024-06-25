// ListManagementImportAdapter.java
package com.example.doanmonhoc.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.doanmonhoc.R;
import com.example.doanmonhoc.activity.CreateImportProduct.DetailedGoodsReceivedNoteActivity;
import com.example.doanmonhoc.activity.CreateImportProduct.ManagementImportActivity;
import com.example.doanmonhoc.model.DetailedGoodsReceivedNote;
import com.example.doanmonhoc.model.GoodsReceivedNote;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class ListManagementImportAdapter extends BaseAdapter {

    private Context context;
    private List<GoodsReceivedNote> goodsReceivedNoteList;

    public ListManagementImportAdapter(Context context, List<GoodsReceivedNote> goodsReceivedNoteList) {
        this.context = context;
        this.goodsReceivedNoteList = goodsReceivedNoteList;
    }

    public ListManagementImportAdapter(ManagementImportActivity context, List<DetailedGoodsReceivedNote> detailList) {
    }

    @Override
    public int getCount() {
        return goodsReceivedNoteList != null ? goodsReceivedNoteList.size() : 0;
    }

    @Override
    public Object getItem(int position) {
        return goodsReceivedNoteList != null ? goodsReceivedNoteList.get(position) : null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    static class ViewHolder {
        TextView tvId, tvDateTime, tvPrice;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.import_management_item, parent, false);
            holder = new ViewHolder();
            holder.tvId = convertView.findViewById(R.id.tvId);
            holder.tvDateTime = convertView.findViewById(R.id.tvDate);
            holder.tvPrice = convertView.findViewById(R.id.totalPrice);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        GoodsReceivedNote goodsReceivedNote = goodsReceivedNoteList.get(position);
        holder.tvId.setText(goodsReceivedNote.getGrnKey());

        Timestamp timestamp = goodsReceivedNote.getCreatedAt();
        String timestampString = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US).format(timestamp);
        holder.tvDateTime.setText(timestampString);

       // holder.tvPrice.setText("Total Price: " + goodsReceivedNote.getTotalPrice());

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DetailedGoodsReceivedNoteActivity.class);
                intent.putExtra("goodsReceivedNoteId", goodsReceivedNote.getId());
                context.startActivity(intent);
            }
        });

        return convertView;
    }
}
