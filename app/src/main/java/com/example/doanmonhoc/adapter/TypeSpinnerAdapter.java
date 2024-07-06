package com.example.doanmonhoc.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.doanmonhoc.R;
import com.example.doanmonhoc.model.ProductGroup;
import com.example.doanmonhoc.utils.TextUtils;

import java.util.List;

public class TypeSpinnerAdapter extends BaseAdapter {
    private static final String TAG = TypeSpinnerAdapter.class.getSimpleName();

    private final Context mContext;
    private List<ProductGroup> mGroupList;

    public TypeSpinnerAdapter(Context context) {
        this.mContext = context;
    }

    @Override
    public int getCount() {
        if (!mGroupList.isEmpty()) {
            return mGroupList.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return mGroupList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TypeItemViewHolder viewHolder;

        if (convertView == null) {
            convertView = LayoutInflater.from(mContext)
                    .inflate(R.layout.dropdown_item, parent, false);
            viewHolder = new TypeItemViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (TypeItemViewHolder) convertView.getTag();
        }

        ProductGroup currentGroup = mGroupList.get(position);
        if (currentGroup != null) {
            String name = currentGroup.getProductGroupName();
            if (TextUtils.isValidString(name)) {
                viewHolder.textTypeName.setText(name);
            } else {
                Log.e(TAG, "getView:" + "Tên là null hoặc rỗng");
            }
        }
        return convertView;
    }

    public List<ProductGroup> getData() {
        return mGroupList;
    }

    public void setData(List<ProductGroup> groupList) {
        this.mGroupList = groupList;
        notifyDataSetChanged();
    }

    private static class TypeItemViewHolder {
        private final TextView textTypeName;

        public TypeItemViewHolder(View view) {
            textTypeName = (TextView) view.findViewById(R.id.textItemName);
        }
    }
}
