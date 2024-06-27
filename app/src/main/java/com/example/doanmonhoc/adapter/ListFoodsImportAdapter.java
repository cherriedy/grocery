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
import android.widget.Toast;

import com.example.doanmonhoc.R;
import com.example.doanmonhoc.activity.CreateImportProduct.ConfirmImportActivity;
import com.example.doanmonhoc.model.ImportItem;
import com.example.doanmonhoc.model.Product;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ListFoodsImportAdapter extends BaseAdapter {
    private Context context;
    private List<Product> productList;
    private List<ImportItem> importItems;

    public ListFoodsImportAdapter(Context context, List<Product> productList, List<ImportItem> importItems) {
        this.context = context;
        this.productList = productList;
        this.importItems = importItems;
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
        TextView productName, productKey, price, quantity, soluonght;
        ImageButton btnAddProduct;
        int currentPosition;
        ImageView image;
        Button btnMinus, btnPlus;
        View thanhTangGiamSoLuong; // Đổi từ CardView sang View
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.import_create_item, parent, false);
            holder = new ViewHolder();
            holder.image = convertView.findViewById(R.id.myShapeableImageView);
            holder.productName = convertView.findViewById(R.id.item_productname);
            holder.quantity = convertView.findViewById(R.id.item_quantity);
            holder.productKey = convertView.findViewById(R.id.item_productKey);
            holder.btnAddProduct = convertView.findViewById(R.id.btn_add);
            holder.price = convertView.findViewById(R.id.price);
            holder.btnMinus = convertView.findViewById(R.id.btn_minus);
            holder.btnPlus = convertView.findViewById(R.id.btn_plus);
            holder.soluonght = convertView.findViewById(R.id.item_quantity1);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Product product = productList.get(position);
        holder.soluonght.setText(String.valueOf(product.getActualQuantity()));
        holder.currentPosition = position; // Lưu vị trí hiện tại của item
        String imageName = product.getAvatarPath();
        int resID = context.getResources().getIdentifier(imageName, "drawable", context.getPackageName());
        holder.image.setImageResource(resID);

        holder.productName.setText(product.getProductName());
        float discountedPrice = product.getOutPrice() * (1 - product.getDiscount());
        holder.price.setText(String.valueOf(discountedPrice));

        // Cập nhật số lượng hiển thị
        updateQuantityView(holder, product);

        // Xử lý sự kiện khi nhấn nút cộng
        holder.btnPlus.setOnClickListener(v -> {
            if (checkProductStock(product.getId(), 1)) {
                updateCart(position, 1);
                // Chuyển sang ConfirmImportActivity với sản phẩm và số lượng 1
                navigateToConfirmImportActivity(product, 1);
            } else {
                Toast.makeText(context, "Số lượng tồn kho không đủ", Toast.LENGTH_SHORT).show();
            }
        });

        // Xử lý sự kiện khi nhấn nút trừ
        holder.btnMinus.setOnClickListener(v -> {
            if (checkProductStock(product.getId(), -1)) {
                updateCart(position, -1);
            } else {
                Toast.makeText(context, "Không thể giảm số lượng dưới 0", Toast.LENGTH_SHORT).show();
            }
        });

        return convertView;
    }

    // Phương thức để chuyển sang ConfirmImportActivity
    private void navigateToConfirmImportActivity(Product product, int quantity) {
        Intent intent = new Intent(context, ConfirmImportActivity.class);

        // Tạo mới một ImportItem từ sản phẩm và số lượng đã chọn
        ImportItem newItem = new ImportItem(product, quantity, quantity * product.getOutPrice());

        // Thêm newItem vào danh sách importItems
        importItems.add(newItem);

        // Gửi chỉ một importItem mới thêm sang ConfirmImportActivity
        ArrayList<ImportItem> singleItemList = new ArrayList<>();
        singleItemList.add(newItem);
        intent.putExtra("importItems", singleItemList);

        context.startActivity(intent);
    }

    private void updateQuantityView(ViewHolder holder, Product product) {
        for (ImportItem item : importItems) {
            if (item.getProduct().getId() == product.getId()) {
                holder.quantity.setText(String.valueOf(item.getQuantity()));
                holder.thanhTangGiamSoLuong.setVisibility(View.VISIBLE);
                return;
            }
        }
        holder.quantity.setText("0");
        holder.thanhTangGiamSoLuong.setVisibility(View.INVISIBLE);
    }

    private boolean checkProductStock(long productId, int requiredQuantity) {
        // Logic để kiểm tra số lượng tồn kho
        for (Product product : productList) {
            if (product.getId() == productId) {
                return product.getInventoryQuantity() >= requiredQuantity;
            }
        }
        return false;
    }

    private void updateCart(int position, int quantity) {
        Product product = productList.get(position);
        float discountedPrice = product.getOutPrice() * (1 - product.getDiscount());

        boolean found = false;
        Iterator<ImportItem> iterator = importItems.iterator();
        while (iterator.hasNext()) {
            ImportItem item = iterator.next();
            if (item.getProduct().getId() == product.getId()) {
                item.setQuantity(quantity);
                item.setPrice(quantity * discountedPrice);
                found = true;
                break;
            }
        }
        if (!found && quantity > 0) {
            ImportItem newItem = new ImportItem(product, quantity, quantity * discountedPrice);
            importItems.add(newItem);
        }
        updateTotalUI();
        notifyDataSetChanged(); // Cập nhật lại giao diện
    }

    private int totalQuantity;
    private double totalAmount;

    private void updateTotalUI() {
        totalQuantity = 0;
        totalAmount = 0;
        for (ImportItem item : importItems) {
            totalQuantity += item.getQuantity();
            totalAmount += item.getPrice();
        }
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
        updateTotalUI(); // Cập nhật lại tổng số lượng và tổng tiền sau khi dữ liệu thay đổi
    }
}
