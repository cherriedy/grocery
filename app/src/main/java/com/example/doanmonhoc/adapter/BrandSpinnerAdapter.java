package com.example.doanmonhoc.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.doanmonhoc.R;
import com.example.doanmonhoc.model.Brand;

import java.util.List;

public class ProductBrandSpinnerAdapter extends BaseAdapter {
    private static final String TAG = "BrandSpinnerAdapter";

    private final Context context;
    private List<Brand> brandList;

    public ProductBrandSpinnerAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        if (brandList != null) {
            return brandList.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return brandList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        BrandItemViewHolder viewHolder;

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.dropdown_item, parent, false);
            viewHolder = new BrandItemViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (BrandItemViewHolder) convertView.getTag();
        }

        Brand currentBrand = brandList.get(position);
        if (currentBrand != null) {
            try {
                viewHolder.textBrandName.setText(currentBrand.getBrandName());
            } catch (NullPointerException | NumberFormatException e) {
                e.printStackTrace();
            }
        }
        return convertView;
    }

    public void setData(List<Brand> brandList) {
        this.brandList = brandList;
        notifyDataSetChanged();
    }

    private static class BrandItemViewHolder {
        private final TextView textBrandName;

        public BrandItemViewHolder(View view) {
            textBrandName = (TextView) view.findViewById(R.id.textItemName);
        }
    }
}
