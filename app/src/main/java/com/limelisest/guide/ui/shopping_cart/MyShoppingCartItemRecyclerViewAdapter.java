package com.limelisest.guide.ui.shopping_cart;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.limelisest.guide.MainActivity;
import com.limelisest.guide.R;
import com.limelisest.guide.placeholder.PlaceholderContent;
import com.limelisest.guide.placeholder.PlaceholderContent.PlaceholderItem;
import com.limelisest.guide.databinding.FragmentShoppingCartItemBinding;

import java.util.ArrayList;
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
        holder.mNameView.setText(holder.mItem.name);
        holder.mPriceView.setText(String.format("%.2f ￥", Float.parseFloat(holder.mItem.price)) );
        holder.mAllPriceView.setText(String.format("%.2f ￥",Float.parseFloat(holder.mItem.price)*Integer.parseInt(holder.mItem.num)));
        holder.mNumView.setText("X"+holder.mItem.num);
        holder.mAddItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String v_num=PlaceholderContent.ShoppingCarITEMS.get(position).num;
                v_num=PlaceholderContent.ShoppingCarITEMS.get(position).num=String.valueOf(Integer.parseInt(v_num)+1);
                holder.mNumView.setText("X"+v_num);
                holder.mAllPriceView.setText(String.format("%.2f ￥",Float.parseFloat(holder.mItem.price)*Integer.parseInt(holder.mItem.num)));

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
                            PlaceholderContent.ShoppingCarITEMS.remove(position);
                            notifyDataSetChanged();
                        }
                    });
                    builder.setNeutralButton("取消",null);
                    builder.show();
                }
                else{
                    v_num=PlaceholderContent.ShoppingCarITEMS.get(position).num=String.valueOf(Integer.parseInt(v_num)-1);
                    holder.mNumView.setText("X"+v_num);
                }
                holder.mAllPriceView.setText(String.format("%.2f ￥",Float.parseFloat(holder.mItem.price)*Integer.parseInt(holder.mItem.num)));


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
        public PlaceholderItem mItem;

        public ViewHolder(FragmentShoppingCartItemBinding binding) {
            super(binding.getRoot());
            mNameView = binding.itemName;
            mPriceView = binding.itemPrice;
            mNumView = binding.itemNum;
            mAllPriceView=binding.itemAllPrice;
            mAddItemButton = binding.buttonAddItem;
            mReduceItemButton = binding.buttonReduceItem;

        }

        @Override
        public String toString() {
            return super.toString() + " '" + mNameView.getText() + "'";
        }
    }
}