package com.example.doanmonhoc.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

import com.example.doanmonhoc.R;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.List;

public class ShortcutGridViewAdapter extends BaseAdapter {
    public static class Shortcut {
        private String name;
        private int resource;

        public Shortcut(String name, int resource) {
            this.name = name;
            this.resource = resource;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getResource() {
            return resource;
        }

        public void setResource(int resource) {
            this.resource = resource;
        }
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    private final Context context;
    private List<Shortcut> shortcutList;
    private OnItemClickListener onItemClickListener;
    public static int SHORTCUT_PRODUCT = 0;
    public static int SHORTCUT_REPORT = 1;
    public static int SHORTCUT_ORDER = 2;
    public static int SHORTCUT_INVENTORY = 3;
    public static int SHORTCUT_STAFF = 4;
    public static int SHORTCUT_MORE = 5;

    public ShortcutGridViewAdapter(Context context) {
        this.context = context;
    }

    public void setData(List<Shortcut> shortcutList) {
        this.shortcutList = shortcutList;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public int getCount() {
        if (shortcutList != null) {
            return shortcutList.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return shortcutList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.shortcut_grid_view_item, parent, false);
        }

        Shortcut currentShortcut = shortcutList.get(position);
        ShapeableImageView imageShortcutAvatar = convertView.findViewById(R.id.image_shortcut_avatar);
        TextView textShortcutName = convertView.findViewById(R.id.text_shortcut_name);

        try {
            imageShortcutAvatar.setImageDrawable(ContextCompat.getDrawable(context, currentShortcut.getResource()));
            textShortcutName.setText(currentShortcut.getName());
            convertView.setOnClickListener(v -> {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(position);
                }
            });
        } catch (Exception e) {
            imageShortcutAvatar.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.cat));
            textShortcutName.setText("<NULL>");
        }
        return convertView;
    }
}
