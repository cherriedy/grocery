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


import java.util.List;

public class ListManagementImport extends BaseAdapter {

    private Context context;
    private List<GoodsReceivedNote> goodsReceivedNoteList;
    private List<DetailedGoodsReceivedNote> detailedGoodsReceivedNoteList;
    public ListManagementImport(Context context,List<GoodsReceivedNote> goodsReceivedNoteList, List<DetailedGoodsReceivedNote> detailedGoodsReceivedNoteList) {
        this.context = context;
       this.goodsReceivedNoteList = goodsReceivedNoteList;
       this.detailedGoodsReceivedNoteList = detailedGoodsReceivedNoteList;

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
        ListManagementImport.LogoViewHolder holder;

        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.import_management_item, parent, false);
            holder = new ListManagementImport.LogoViewHolder();

            holder.tvId = convertView.findViewById(R.id.tvId);
            holder.tvDateTime = convertView.findViewById(R.id.item_quantity);
            holder.tvPrice = convertView.findViewById(R.id.item_grnKey);
            convertView.setTag(holder);
        } else {
            holder = (ListManagementImport.LogoViewHolder) convertView.getTag();
        }
            holder.tvId.setText(goodsReceivedNoteList.get(position).getGrnKey());


           // holder.tvDateTime.setText((CharSequence) goodsReceivedNoteList.get(position).getCreatedAt());


            holder.tvPrice.setText(goodsReceivedNoteList.get(position).getId());


        return convertView;
    }

}
