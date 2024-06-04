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
import com.example.doanmonhoc.model.Brand;

import java.util.ArrayList;
import java.util.List;

public class BrandAutoCompleteAdapter extends ArrayAdapter<Brand> {

    List<Brand> brandList;

    public BrandAutoCompleteAdapter(@NonNull Context context, int resource, @NonNull List<Brand> objects) {
        super(context, resource, objects);
        brandList = new ArrayList<>(objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.dropdown_item, parent, false);
        }

        Brand brand = getItem(position);

        TextView textBrandName = convertView.findViewById(R.id.textItemName);
        textBrandName.setText(brand.getBrandName());

        return convertView;
    }

    @NonNull
    @Override
    public Filter getFilter() {
        /**
         * Hàm kiểm tra các đối tượng có chứ kí tự
         * được nhập trong AutoCompleteTextView,
         * sau đó lưu vào một mảng result,
         * cuối cùng gán vào ResultFiler.
         **/
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                /**
                 * constraint trả về kí tự nhập trong AutoCompleteTextView.
                 * Nếu constraint rỗng, trả về hết danh sách (đang lưu trong brandList).
                 * Nếu khác rỗng, trả về các item có kí tự trùng.
                 **/

                List<Brand> brandSuggestionList = new ArrayList<>();

                if (constraint == null || constraint.length() == 0) {
                    brandSuggestionList.addAll(brandList);
                } else {
                    String filter = constraint.toString().toLowerCase().trim();
                    for (Brand brand : brandList) {
                        if (brand.getBrandName().toLowerCase().contains(filter)) {
                            brandSuggestionList.add(brand);
                        }
                    }
                }

                FilterResults filterResults = new FilterResults();
                // Gán mảng kết quả vào filterResult
                filterResults.values = brandSuggestionList;
                // Gán số lượng item tìm thấy
                filterResults.count = brandSuggestionList.size();

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
                clear();                                        // Xóa dữ liệu đang hiển thị, để hiển thị các item mới.
                addAll((List<Brand>) results.values);           // Gán dữ liệu để chuẩn bị hiển thị.
                notifyDataSetInvalidated();                     // Thông báo dữ liệu đã được thay đổi, và sẵn sàng để hiện thị.
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
                return ((Brand) resultValue).getBrandName();
            }
        };
    }
}
