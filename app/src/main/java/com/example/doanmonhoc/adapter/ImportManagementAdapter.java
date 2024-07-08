package com.example.doanmonhoc.adapter;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import androidx.cardview.widget.CardView;
import com.example.doanmonhoc.R;
import com.example.doanmonhoc.activity.ImportManagement.ImportBillActivity;
import com.example.doanmonhoc.activity.SaleManagement.SaleDetailedInvoiceActivity;
import com.example.doanmonhoc.model.GoodsReceivedNote;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class ImportManagementAdapter extends BaseAdapter {
    private Context context;
    private List<GoodsReceivedNote> list;

    public ImportManagementAdapter(Context context, List<GoodsReceivedNote> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public static class ViewHolder{
        TextView txtHD, txtDate, txtTotalAmount;
        CardView cardView;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.import_management_item, parent, false);

            holder = new ViewHolder();
            holder.txtHD = convertView.findViewById(R.id.tvMaHD);
            holder.txtDate = convertView.findViewById(R.id.tvDate);
            holder.txtTotalAmount = convertView.findViewById(R.id.tvTotalAmount);
            holder.cardView = convertView.findViewById(R.id.cardView);
            convertView.setTag(holder);
        } else{
            holder = (ViewHolder) convertView.getTag();
        }

        GoodsReceivedNote goodsReceivedNote = list.get(position);
        holder.txtHD.setText(goodsReceivedNote.getGrnKey());

        Timestamp timestamp = goodsReceivedNote.getCreatedAt();
        // timestamp to String
        String timestampString = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US).format(timestamp);
        holder.txtDate.setText(timestampString);
        holder.txtTotalAmount.setText(String.valueOf(goodsReceivedNote.getTotalAmount()));

        // truyền
        holder.cardView.setOnClickListener(v -> {
            Intent intent = new Intent(context, ImportBillActivity.class);
            intent.putExtra("grnId", goodsReceivedNote.getId());
            intent.putExtra("grnKey", goodsReceivedNote.getGrnKey());
            intent.putExtra("staffId", goodsReceivedNote.getStaffid());
            intent.putExtra("createAt", goodsReceivedNote.getCreatedAt().getTime());
            intent.putExtra("totalAmount", goodsReceivedNote.getTotalAmount());
            intent.putExtra("note", goodsReceivedNote.getNote());
            context.startActivity(intent);
        });

        return convertView;
    }
}
