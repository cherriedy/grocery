package com.example.doanmonhoc.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.doanmonhoc.R;
import com.example.doanmonhoc.activity.CreateImportProduct.ConfirmImportActivity;
import com.example.doanmonhoc.model.DetailedGoodsReceivedNote;
import com.example.doanmonhoc.model.GoodsReceivedNote;
import com.example.doanmonhoc.model.Product;

import java.util.List;

public class ListFoodsImportAdapter extends BaseAdapter {
    private Context context;
    private List<Product> productList;
    private List<Product> selectedProducts;

    public ListFoodsImportAdapter(Context context, List<Product> productList, List<Product> selectedProducts) {
        this.context = context;
        this.productList = productList;
        this.selectedProducts = selectedProducts;
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
        TextView productName,productKey;
        TextView quantity;
        ImageButton btnAddProduct;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.import_create_item, parent, false);
            holder = new ViewHolder();
            holder.productName = convertView.findViewById(R.id.item_productname);
            holder.quantity = convertView.findViewById(R.id.item_quantity);
            holder.productKey = convertView.findViewById(R.id.item_productKey);
            holder.btnAddProduct = convertView.findViewById(R.id.btn_add);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final Product product = productList.get(position);
        holder.quantity.setText(String.valueOf(product.getActualQuantity()));
        holder.productName.setText(product.getProductName());
        holder.productKey.setText(String.valueOf(product.getProductKey()));
        holder.btnAddProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!selectedProducts.contains(product)) {
                    selectedProducts.add(product);
                }
            }
        });

        return convertView;
    }
}