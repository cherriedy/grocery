// DetailedGoodsReceivedNoteAdapter.java
package com.example.doanmonhoc.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.doanmonhoc.R;
import com.example.doanmonhoc.model.DetailedGoodsReceivedNote;

import java.util.List;

public class DetailedGoodsReceivedNoteAdapter extends BaseAdapter {

    private Context context;
    private List<DetailedGoodsReceivedNote> detailedGoodsReceivedNoteList;

    public DetailedGoodsReceivedNoteAdapter(Context context, List<DetailedGoodsReceivedNote> detailedGoodsReceivedNoteList) {
        this.context = context;
        this.detailedGoodsReceivedNoteList = detailedGoodsReceivedNoteList;
    }

    @Override
    public int getCount() {
        return detailedGoodsReceivedNoteList != null ? detailedGoodsReceivedNoteList.size() : 0;
    }

    @Override
    public Object getItem(int position) {
        return detailedGoodsReceivedNoteList != null ? detailedGoodsReceivedNoteList.get(position) : null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    static class ViewHolder {
        TextView tvProductId, tvQuantity, tvPrice;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.item_details_import, parent, false);
            holder = new ViewHolder();

            holder.tvProductId = convertView.findViewById(R.id.item_name);
            holder.tvQuantity = convertView.findViewById(R.id.item_quantity);
            holder.tvPrice = convertView.findViewById(R.id.tv_totalPrice);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        DetailedGoodsReceivedNote detailedGoodsReceivedNote = detailedGoodsReceivedNoteList.get(position);
        holder.tvProductId.setText("Product ID: " + detailedGoodsReceivedNote.getProductid());
        holder.tvQuantity.setText("Quantity: " + detailedGoodsReceivedNote.getQuantity());
        holder.tvPrice.setText("Price: " + detailedGoodsReceivedNote.getPrice());

        return convertView;
    }
}
