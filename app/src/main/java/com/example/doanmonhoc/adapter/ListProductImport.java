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
import com.example.doanmonhoc.model.Product;

import java.util.List;

public class ListProductImport extends BaseAdapter {

    private Context context;
    private List<DetailedGoodsReceivedNote> detailList;
    private List<Product> productList;
    private List<GoodsReceivedNote> grnList;

    public ListProductImport(Context context, List<DetailedGoodsReceivedNote> detaillist,
                             List<Product> productlist, List<GoodsReceivedNote> grnlist) {
        this.context = context;
        this.detailList = detaillist;
        this.productList = productlist;
        this.grnList = grnlist;
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
    private static class LogoViewHolder {
        TextView  imgProductTextView, quantityTextView,productNameTextView, grnKeyTextView;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LogoViewHolder holder;

        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.import_create_item, parent, false);
            holder = new LogoViewHolder();
         //   holder.imgProductTextView = convertView.findViewById(R.id.myShapeableImageView);
            holder.productNameTextView = convertView.findViewById(R.id.item_productname);
            holder.quantityTextView = convertView.findViewById(R.id.item_quantity);
            holder.grnKeyTextView = convertView.findViewById(R.id.item_grnKey);
            convertView.setTag(holder);
        } else {
            holder = (LogoViewHolder) convertView.getTag();
        }

        if (position < detailList.size()) {
            DetailedGoodsReceivedNote detail = detailList.get(position);
            holder.quantityTextView.setText(String.valueOf(detail.getQuantity()));
        }

        //    holder.imgProductTextView.setText(productList.get(position).getAvatarPath());
            holder.productNameTextView.setText(productList.get(position).getProductName());


        if (position < grnList.size()) {
            GoodsReceivedNote grn = grnList.get(position);
            holder.grnKeyTextView.setText(grn.getGrnKey());
        }

        return convertView;
    }


}
