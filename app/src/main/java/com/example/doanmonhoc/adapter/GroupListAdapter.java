package com.example.doanmonhoc.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doanmonhoc.R;
import com.example.doanmonhoc.model.ProductGroup;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.List;

public class GroupListAdapter extends RecyclerView.Adapter<GroupListAdapter.GroupItemViewHolder> {
    private static final String TAG = "GroupListAdapter";

    private final Context mContext;
    private List<ProductGroup> mGroupList;
    private OnItemClickListener mOnItemClickListener;

    public GroupListAdapter(Context mContext) {
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public GroupItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.product_brand_or_group_list_item, parent, false);
        return new GroupItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GroupItemViewHolder holder, int position) {
        ProductGroup currentGroup = mGroupList.get(position);
        if (currentGroup == null) {
            return;
        }

        try {
            holder.mGroupAvatar.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.cat));
            holder.mGroupName.setText(currentGroup.getProductGroupName());
            holder.mGroupKey.setText(currentGroup.getProductGroupKey());
            holder.itemView.setOnClickListener(v -> {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClick(currentGroup);
                }
            });
        } catch (NullPointerException e) {
            Log.e(TAG, "onBindViewHolder: " + "Có trường null trong đối tượng currentGroup");
        }
    }

    @Override
    public int getItemCount() {
        if (mGroupList != null) {
            return mGroupList.size();
        }
        return 0;
    }

    public static class GroupItemViewHolder extends RecyclerView.ViewHolder {
        private final ShapeableImageView mGroupAvatar;
        private final TextView mGroupName;
        private final TextView mGroupKey;

        public GroupItemViewHolder(@NonNull View itemView) {
            super(itemView);
            mGroupAvatar = itemView.findViewById(R.id.image_item_avatar);
            mGroupName = itemView.findViewById(R.id.text_item_name);
            mGroupKey = itemView.findViewById(R.id.text_item_key);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(ProductGroup group);
    }

    public List<ProductGroup> getData() {
        return mGroupList;
    }

    public void setData(List<ProductGroup> mGroupList) {
        this.mGroupList = mGroupList;
        notifyDataSetChanged();
    }

    public void setOnClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }
}
