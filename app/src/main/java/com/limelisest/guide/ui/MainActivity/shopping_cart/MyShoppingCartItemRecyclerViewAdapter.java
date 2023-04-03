package com.limelisest.guide.ui.MainActivity.shopping_cart;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.limelisest.guide.placeholder.PlaceholderContent;
import com.limelisest.guide.placeholder.PlaceholderContent.PlaceholderItem;
import com.limelisest.guide.databinding.FragmentShoppingCartItemBinding;

import java.sql.SQLException;
import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link PlaceholderItem}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MyShoppingCartItemRecyclerViewAdapter extends RecyclerView.Adapter<MyShoppingCartItemRecyclerViewAdapter.ViewHolder> {

    private final List<PlaceholderItem> mValues;

    public MyShoppingCartItemRecyclerViewAdapter(List<PlaceholderItem> items) {
        mValues = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new ViewHolder(FragmentShoppingCartItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));

    }

    @SuppressLint({"DefaultLocale", "SetTextI18n"})
    @Override
    public void onBindViewHolder(final ViewHolder holder, @SuppressLint("RecyclerView") int position) {

        holder.mItem = mValues.get(position);
        int num_stock=Integer.parseInt(holder.mItem.num_stock);
        holder.mNameView.setText(holder.mItem.name);
        holder.mPriceView.setText(String.format("%.2f ￥", Float.parseFloat(holder.mItem.price)) );
        holder.mAllPriceView.setText(String.format("%.2f ￥",Float.parseFloat(holder.mItem.price)*Integer.parseInt(holder.mItem.num)));
        holder.mNumView.setText("X"+holder.mItem.num);
        // 读取头图
        try {
            holder.mImageView.setImageBitmap(PlaceholderContent.db.GetItemPicture(holder.mItem.id));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        holder.mAddItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String v_num=PlaceholderContent.ShoppingCarITEMS.get(position).num;
                if (Integer.parseInt(v_num)>=num_stock){
                    Toast.makeText(view.getContext(), String.format("物品%s已经达到库存上限", holder.mItem.name), Toast.LENGTH_SHORT).show();
                }else {
                    try {
                        v_num=PlaceholderContent.ShoppingCarITEMS.get(position).num=String.valueOf(Integer.parseInt(v_num)+1);
                        int status=PlaceholderContent.db.SetShoppingCarItem(holder.mItem.id,v_num);
                        if(status == 0){
                            holder.mNumView.setText("X"+v_num);
                            holder.mAllPriceView.setText(String.format("%.2f ￥",Float.parseFloat(holder.mItem.price)*Integer.parseInt(holder.mItem.num)));
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }

                }
            }
        });
        holder.mReduceItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                String v_num=PlaceholderContent.ShoppingCarITEMS.get(position).num;
                if (Integer.parseInt(v_num)<=1){
                    builder.setTitle("移出商品");
                    builder.setMessage("你确定要移出商品吗？");
                    builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @SuppressLint("NotifyDataSetChanged")
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            try {
                                PlaceholderContent.ShoppingCarITEMS.remove(position);
                                int status = PlaceholderContent.db.SetShoppingCarItem(holder.mItem.id,"0");
                                if(status == 0){
                                    notifyDataSetChanged();
                                }
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }

                        }
                    });
                    builder.setNeutralButton("取消",null);
                    builder.show();
                }
                else{
                    try {
                        v_num=PlaceholderContent.ShoppingCarITEMS.get(position).num=String.valueOf(Integer.parseInt(v_num)-1);
                        int status=PlaceholderContent.db.SetShoppingCarItem(holder.mItem.id,v_num);
                        if(status == 0){
                            holder.mNumView.setText("X"+v_num);
                            holder.mAllPriceView.setText(String.format("%.2f ￥",Float.parseFloat(holder.mItem.price)*Integer.parseInt(holder.mItem.num)));
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }



            }
        });

    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView mNameView;
        public final TextView mPriceView;
        public final TextView mNumView;
        public final TextView mAllPriceView;
        public final Button mAddItemButton;
        public final Button mReduceItemButton;
        public final ImageView mImageView;
        public PlaceholderItem mItem;

        public ViewHolder(FragmentShoppingCartItemBinding binding) {
            super(binding.getRoot());
            mNameView = binding.itemName;
            mPriceView = binding.itemPrice;
            mNumView = binding.itemNum;
            mAllPriceView=binding.itemAllPrice;
            mAddItemButton = binding.buttonAddItem;
            mReduceItemButton = binding.buttonReduceItem;
            mImageView=binding.imageView;

        }

        @Override
        public String toString() {
            return super.toString() + " '" + mNameView.getText() + "'";
        }
    }
}