package com.example.doanmonhoc.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.constraintlayout.utils.widget.ImageFilterView;
import androidx.core.content.ContextCompat;

import com.example.doanmonhoc.R;
import com.example.doanmonhoc.model.Shortcut;

import java.util.ArrayList;
import java.util.List;

public class BottomSheetShortcutAdapter extends BaseAdapter {
    public static final int SHORTCUT_PRODUCT_BRAND = 0;
    public static final int SHORTCUT_PRODUCT_TYPE = 1;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    private final Context context;
    private List<Shortcut> shortcutsList;
    private OnItemClickListener onItemClickListener;

    public BottomSheetShortcutAdapter(Context context) {
        this.context = context;
        shortcutsList = new ArrayList<>();
    }

    @Override
    public int getCount() {
        if (shortcutsList != null) {
            return shortcutsList.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return shortcutsList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.bottom_sheet_shortcut_item, parent, false);
        }

        TextView textShortcut;
        ImageFilterView imageShortcut;
        Shortcut currentShortcut = shortcutsList.get(position);

        if (currentShortcut != null) {
            try {
                imageShortcut = convertView.findViewById(R.id.image_shortcut_avatar);
                textShortcut = convertView.findViewById(R.id.text_shortcut_name);

                textShortcut.setText(currentShortcut.getName());
                imageShortcut.setColorFilter(ContextCompat.getColor(context, currentShortcut.getColor()));
                imageShortcut.setImageDrawable(ContextCompat.getDrawable(context, currentShortcut.getResource()));
            } catch (Exception e) {
                Log.e("BottomSheetShortcutAdapter", e.getMessage());
            }
        }

        if (onItemClickListener != null) {
            convertView.setOnClickListener(v -> onItemClickListener.onItemClick(position));
        }

        return convertView;
    }

    public void setData(List<Shortcut> shortcutsList) {
        this.shortcutsList = shortcutsList;
    }

    public void setOnClickOnListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
}
