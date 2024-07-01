package com.example.doanmonhoc.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.doanmonhoc.R;
import com.example.doanmonhoc.activity.StaffManagement.StaffDetailManagementActivity;
import com.example.doanmonhoc.model.Staff;

import java.util.List;

public class StaffAdapter extends ArrayAdapter<Staff> {

    private List<Staff> staffList;
    private Context context;
    private OnDeleteClickListener onDeleteClickListener;

    public StaffAdapter(@NonNull Context context, @NonNull List<Staff> staffList, OnDeleteClickListener onDeleteClickListener) {
        super(context, 0, staffList);
        this.context = context;
        this.staffList = staffList;
        this.onDeleteClickListener = onDeleteClickListener;
    }

    public interface OnDeleteClickListener {
        void onDeleteClick(long staffId);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View itemView = convertView;
        ViewHolder holder;

        if (itemView == null) {
            itemView = LayoutInflater.from(context).inflate(R.layout.staff_item, parent, false);
            holder = new ViewHolder();
            holder.staffName = itemView.findViewById(R.id.staff_name);
            holder.staffKey = itemView.findViewById(R.id.staff_key);
            holder.staffEmail = itemView.findViewById(R.id.staff_email);
            holder.staffImage = itemView.findViewById(R.id.staff_photo);
            holder.btnDelete = itemView.findViewById(R.id.btnDelete);
            itemView.setTag(holder);
        } else {
            holder = (ViewHolder) itemView.getTag();
        }

        Staff staff = staffList.get(position);

        holder.staffName.setText(staff.getStaffName());
        holder.staffKey.setText(staff.getStaffKey());
        holder.staffEmail.setText(staff.getStaffEmail());

        // Load image using Glide from drawable
        String imageName = staff.getStaffImage(); // Assuming 'getStaffImage()' returns image file name
        Resources resources = context.getResources();
        int resourceId = resources.getIdentifier(imageName, "drawable", context.getPackageName());

        Glide.with(context)
                .load(resourceId)
                .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE))
                .placeholder(R.drawable.cat)
                .into(holder.staffImage);

        // Set delete button click listener
        holder.btnDelete.setOnClickListener(v -> onDeleteClickListener.onDeleteClick(staff.getId()));

        // Set item click listener to show staff detail
        itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, StaffDetailManagementActivity.class);
            intent.putExtra("staffId", staff.getId());
            context.startActivity(intent);
        });

        return itemView;
    }

    // ViewHolder pattern to optimize ListView performance
    static class ViewHolder {
        TextView staffName, staffKey, staffEmail;
        ImageView staffImage;
        ImageButton btnDelete;
    }
}
