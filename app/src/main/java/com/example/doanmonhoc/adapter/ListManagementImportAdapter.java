package com.example.doanmonhoc.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.doanmonhoc.R;
import com.example.doanmonhoc.model.DetailedGoodsReceivedNote;
import com.example.doanmonhoc.model.GoodsReceivedNote;


import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class ListManagementImportAdapter extends BaseAdapter {

    private Context context;
    private List<GoodsReceivedNote> goodsReceivedNoteList;
    private List<DetailedGoodsReceivedNote> detailedGoodsReceivedNoteList;

    public ListManagementImportAdapter(Context context, List<GoodsReceivedNote> goodsReceivedNoteList) {
        this.context = context;
        this.goodsReceivedNoteList = goodsReceivedNoteList;

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

    static class LogoViewHolder {
        TextView tvId, tvDateTime, tvPrice;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ListManagementImportAdapter.LogoViewHolder holder;

        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.import_management_item, parent, false);
            holder = new ListManagementImportAdapter.LogoViewHolder();

            holder.tvId = convertView.findViewById(R.id.tvId);
            holder.tvDateTime = convertView.findViewById(R.id.tvDate);
            holder.tvPrice = convertView.findViewById(R.id.totalPrice);
            convertView.setTag(holder);
        } else {
            holder = (ListManagementImportAdapter.LogoViewHolder) convertView.getTag();
        }



        GoodsReceivedNote goodsReceivedNote = goodsReceivedNoteList.get(position);
        holder.tvId.setText(goodsReceivedNote.getGrnKey());

        Timestamp timestamp = goodsReceivedNote.getCreatedAt();
        // Convert Timestamp to String
        String timestampString = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US).format(timestamp);
        holder.tvDateTime.setText(timestampString);

//        holder.tvPrice.setText(goodsReceivedNote.getId());

        return convertView;
    }

}
