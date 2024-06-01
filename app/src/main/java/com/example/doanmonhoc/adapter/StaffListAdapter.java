package com.example.doanmonhoc.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doanmonhoc.R;
import com.example.doanmonhoc.model.Staff;

import java.util.List;

public class StaffListAdapter extends RecyclerView.Adapter<StaffListAdapter.StaffViewHolder> {
    private List<Staff> staffList;
    private Context context;

    public StaffListAdapter(Context context) {
        this.context = context;
    }

    public void setData(List<Staff> list) {
        this.staffList = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public StaffViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.staff_item, parent, false);
        return new StaffViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StaffViewHolder holder, int position) {
        Staff staff = staffList.get(position);
        if (staff == null) {
            return;
        }
        holder.name.setText(staff.getName());
        holder.email.setText(staff.getEmail());
        holder.photo.setImageResource(staff.getPhoto());
    }

    @Override
    public int getItemCount() {
        if (staffList != null) {
            return staffList.size();
        }
        return 0;
    }

    public static class StaffViewHolder extends RecyclerView.ViewHolder {
        private TextView name;
        private TextView email;
        private ImageView photo;

        public StaffViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.staff_name);
            email = itemView.findViewById(R.id.staff_email);
            photo = itemView.findViewById(R.id.staff_photo);
        }
    }
}
