package com.example.doanmonhoc.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;

import com.example.doanmonhoc.R;
import com.example.doanmonhoc.model.CartItem;
import com.example.doanmonhoc.model.Product;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.Iterator;
import java.util.List;


public class ImportCreateAdapter extends BaseAdapter {
    private Context context;
    private List<Product> list;
    private List<CartItem> cartItems;
    private int totalQuantity;
    private double totalAmount;
    private TextView tvTotalQuantity, tvTotalAmount;
    private CardView layoutBtnThanhToan;

    public ImportCreateAdapter(Context context, List<Product> list, CardView layoutBtnThanhToan, TextView tvTotalQuantity, TextView tvTotalAmount, List<CartItem> cartItems) {
        this.context = context;
        this.list = list;
        this.layoutBtnThanhToan = layoutBtnThanhToan;
        this.tvTotalQuantity = tvTotalQuantity;
        this.tvTotalAmount = tvTotalAmount;
        this.cartItems = cartItems;
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
        return list.get(position).getId();
    }

    public static class ViewHolder {
        TextView tvName, tvInventoryQuantity, tvProductKey;
        EditText editQuantity;
        ImageButton btnPlus, btnMinus, btnBigPlus;
        ShapeableImageView imageView;
        CardView thanhTangGiamSoLuong;
        int currentPosition; // Lưu vị trí hiện tại của item
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.import_create_item, parent, false);

            holder = new ViewHolder();
            holder.tvName = convertView.findViewById(R.id.tvName);
            holder.editQuantity = convertView.findViewById(R.id.tvQuantity);
            holder.tvInventoryQuantity = convertView.findViewById(R.id.tvInventoryQuantity);
            holder.tvProductKey = convertView.findViewById(R.id.tvProductKey);
            holder.btnBigPlus = convertView.findViewById(R.id.btnBigPlus);
            holder.btnPlus = convertView.findViewById(R.id.btnPlus);
            holder.btnMinus = convertView.findViewById(R.id.btnMinus);
            holder.imageView = convertView.findViewById(R.id.imageView);
            holder.thanhTangGiamSoLuong = convertView.findViewById(R.id.thanhTangGiamSoLuong);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Product product = list.get(position);
        holder.currentPosition = position; // Lưu vị trí hiện tại của item
        String imageName = product.getAvatarPath();
        int resID = context.getResources().getIdentifier(imageName, "drawable", context.getPackageName());
        holder.imageView.setImageResource(resID);

        holder.tvName.setText(product.getProductName());
        holder.tvInventoryQuantity.setText(String.valueOf(product.getInventoryQuantity()));
        holder.tvProductKey.setText(product.getProductKey());


        // Cập nhật trạng thái của thanhTangGiamSoLuong và số lượng
        updateQuantityView(holder, product);

        holder.btnBigPlus.setOnClickListener(v -> {
            int currentQuantity = Integer.parseInt(holder.editQuantity.getText().toString());
            if (currentQuantity == 0) {
                if (checkProductStock(product.getId(), 1)) {
                    holder.thanhTangGiamSoLuong.setVisibility(View.VISIBLE);
                    holder.btnBigPlus.setVisibility(View.GONE);
                    holder.editQuantity.setText(String.valueOf(1));
                    updateCart(position, 1);
                } else {
                    Toast.makeText(context, "Số lượng tồn kho không đủ", Toast.LENGTH_SHORT).show();
                }
            } else {
                if (checkProductStock(product.getId(), currentQuantity + 1)) {
                    holder.editQuantity.setText(String.valueOf(currentQuantity + 1));
                    updateCart(position, currentQuantity + 1);
                } else {
                    Toast.makeText(context, "Số lượng tồn kho không đủ", Toast.LENGTH_SHORT).show();
                }
            }
        });

        holder.btnPlus.setOnClickListener(v -> {
            int currentQuantity = Integer.parseInt(holder.editQuantity.getText().toString());
            if (checkProductStock(product.getId(), currentQuantity + 1)) {
                holder.editQuantity.setText(String.valueOf(currentQuantity + 1));
                updateCart(position, currentQuantity + 1);
            } else {
                Toast.makeText(context, "Số lượng tồn kho không đủ", Toast.LENGTH_SHORT).show();
            }
        });

        holder.btnMinus.setOnClickListener(v -> {
            int currentQuantity = Integer.parseInt(holder.editQuantity.getText().toString());
            if (currentQuantity > 0) {
                holder.editQuantity.setText(String.valueOf(currentQuantity - 1));
                updateCart(position, currentQuantity - 1);
                if (currentQuantity - 1 == 0) {
                    holder.thanhTangGiamSoLuong.setVisibility(View.GONE);
                    holder.btnBigPlus.setVisibility(View.VISIBLE);
                }
            }
        });

        // Set OnEditorActionListener to update the cart when the user presses "OK"
        holder.editQuantity.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                int enteredQuantity = Integer.parseInt(holder.editQuantity.getText().toString());
                if (checkProductStock(product.getId(), enteredQuantity)) {
                    updateCart(position, enteredQuantity);
                } else {
                    Toast.makeText(context, "Số lượng tồn kho không đủ", Toast.LENGTH_SHORT).show();
                }
                return true;
            }
            return false;
        });

        return convertView;
    }

    private void updateQuantityView(ViewHolder holder, Product product) {
        boolean isProductInCart = false;
        for (CartItem item : cartItems) {
            if (item.getProduct().getId() == product.getId()) {
                holder.editQuantity.setText(String.valueOf(item.getQuantity()));
                holder.thanhTangGiamSoLuong.setVisibility(View.VISIBLE);
                holder.btnBigPlus.setVisibility(View.GONE);
                isProductInCart = true;
                break;
            }
        }
        if (!isProductInCart) {
            holder.editQuantity.setText(String.valueOf(0));
            holder.thanhTangGiamSoLuong.setVisibility(View.GONE);
            holder.btnBigPlus.setVisibility(View.VISIBLE);
        }
    }

    private boolean checkProductStock(long productId, int requiredQuantity) {
        // Logic để kiểm tra số lượng tồn kho
        for (Product product : list) {
            if (product.getId() == productId) {
                return product.getInventoryQuantity() >= requiredQuantity;
            }
        }
        return false;
    }

    private void updateCart(int position, int quantity) {
        Product product = list.get(position);

        boolean found = false;
        Iterator<CartItem> iterator = cartItems.iterator();
        while (iterator.hasNext()) {
            CartItem item = iterator.next();
            if (item.getProduct().getId() == product.getId()) {
                if (quantity == 0) {
                    iterator.remove();
                } else {
                    item.setQuantity(quantity);
                    item.setPrice(quantity * product.getInPrice());
                }
                found = true;
                break;
            }
        }
        if (!found && quantity > 0) {
            CartItem newItem = new CartItem(product, quantity, quantity * product.getInPrice());
            cartItems.add(newItem);
        }
        updateTotalUI();
        notifyDataSetChanged(); // Cập nhật lại giao diện
    }

    private void updateTotalUI() {
        totalQuantity = 0;
        totalAmount = 0;
        for (CartItem item : cartItems) {
            totalQuantity += item.getQuantity();
            totalAmount += item.getPrice();
        }
        if (totalQuantity > 0) {
            layoutBtnThanhToan.setVisibility(View.VISIBLE);
            tvTotalQuantity.setText(String.valueOf(totalQuantity));
            tvTotalAmount.setText(String.valueOf(totalAmount));
        } else {
            layoutBtnThanhToan.setVisibility(View.GONE);
        }
    }
}
