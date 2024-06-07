package com.example.doanmonhoc.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.doanmonhoc.R;
import com.example.doanmonhoc.model.ProductGroup;

import java.util.ArrayList;
import java.util.List;

public class TypeAutoCompleteAdapter extends ArrayAdapter<ProductGroup> {

    private List<ProductGroup> productGroupList;

    public TypeAutoCompleteAdapter(@NonNull Context context, int resource, @NonNull List<ProductGroup> objects) {
        super(context, resource, objects);
        productGroupList = new ArrayList<>(objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.dropdown_item, parent, false);
        }
        ProductGroup productGroup = getItem(position);

        TextView textProductName = convertView.findViewById(R.id.textItemName);
        textProductName.setText(productGroup.getProductGroupName());

        return convertView;
    }

    @NonNull
    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults filterResults = new FilterResults();
                List<ProductGroup> productGroupsSuggestionList = new ArrayList<>();

                if (constraint == null || constraint.length() == 0) {
                    productGroupsSuggestionList.addAll(productGroupList);
                } else {
                    String filter = constraint.toString().toLowerCase().trim();
                    for (ProductGroup item : productGroupList) {
                        String productGroupName = item.getProductGroupName().toLowerCase();
                        if (productGroupName.contains(filter)) {
                            productGroupsSuggestionList.add(item);
                        }
                    }
                }

                filterResults.values = productGroupsSuggestionList;
                filterResults.count = productGroupsSuggestionList.size();

                return filterResults;
            }

            /**
             * Kịch bản gọi hàm:
             * Khi người dùng nhập từ khóa vào AutoCompleteTextView.
             * `performFiltering` lọc dữ liệu dựa vào từ khóa người dùng nhập vào, sau đó trả kết quả.
             * `publishResults` sẽ được gọi với các dữ liệu đã lọc từ `performFiltering`.
             *  Dữ liệu cũ bị xóa đi, vào cập nhật danh sách kết quả mới.
             *  Giao diện được làm mới, và hiển thị dữ liệu vừa được cập nhật.
             **/
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                clear();
//                if (results != null || results.count > 0) {
                    addAll((List<ProductGroup>) results.values);
//                }
                notifyDataSetInvalidated();
            }

            /**
             * Kịch bản gọi hàm:
             * Khi người dùng nhập từ khóa vào AutoCompleteTextView.
             * Từ khóa sẽ được so sánh, sau đó hiện thị ra một danh sách gợi ý.
             * Người dùng nhấn chọn một trọng các các gợi ý.
             * `convertResultToString` sẽ được gọi với đối số là đối tượng được gọi.
             * Phương thức trên trả về String, sau đó hiển thị trên AutoCompleteTextView.
             **/
            @Override
            public CharSequence convertResultToString(Object resultValue) {
                return ((ProductGroup) resultValue).getProductGroupName();
            }
        };
    }
}
