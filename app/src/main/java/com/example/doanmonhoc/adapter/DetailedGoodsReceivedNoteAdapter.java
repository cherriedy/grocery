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
        TextView tvProductId, tvQuantity, tvPrice,tvProductName;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.import_item_details, parent, false);
            holder = new ViewHolder();

            holder.tvProductName = convertView.findViewById(R.id.item_name);
            holder.tvQuantity = convertView.findViewById(R.id.item_quantity);
            holder.tvPrice = convertView.findViewById(R.id.tv_totalPrice);
          //  holder.tvProductName = convertView.findViewById(R.id.tv_product_name);  // Thêm dòng này vào layout của bạn
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        DetailedGoodsReceivedNote detailedGoodsReceivedNote = detailedGoodsReceivedNoteList.get(position);
        holder.tvQuantity.setText(String.valueOf(detailedGoodsReceivedNote.getQuantity()));
        holder.tvPrice.setText(String.valueOf(detailedGoodsReceivedNote.getPrice()));
        holder.tvProductName.setText((detailedGoodsReceivedNote.getProduct() != null ? detailedGoodsReceivedNote.getProduct().getProductName() : "Loading..."));  // Thêm dòng này

        return convertView;
    }


}
