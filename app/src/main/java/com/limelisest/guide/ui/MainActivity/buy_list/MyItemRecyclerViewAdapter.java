package com.limelisest.guide.ui.MainActivity.buy_list;

import android.annotation.SuppressLint;
import android.media.Image;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.limelisest.guide.databinding.FragmentBuyItemBinding;
import com.limelisest.guide.placeholder.LoginContent;
import com.limelisest.guide.placeholder.PlaceholderContent;
import com.limelisest.guide.placeholder.PlaceholderContent.PlaceholderItem;


import java.sql.SQLException;
import java.util.List;
import java.util.Objects;

/**
 * {@link RecyclerView.Adapter} that can display a {@link PlaceholderItem}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MyItemRecyclerViewAdapter extends RecyclerView.Adapter<MyItemRecyclerViewAdapter.ViewHolder> {

    private final List<PlaceholderItem> mValues;

    public MyItemRecyclerViewAdapter(List<PlaceholderItem> items) {
        mValues = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new ViewHolder(FragmentBuyItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));

    }

    @SuppressLint({"DefaultLocale", "SetTextI18n"})
    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        String v_id=mValues.get(position).id;
        String v_name=mValues.get(position).name;
        String v_price=mValues.get(position).price;
        int v_num_stock=Integer.parseInt(mValues.get(position).num_stock);
        holder.mItem = mValues.get(position);
        holder.mNameView.setText(v_name);
        holder.mPriceView.setText(String.format("%.2f ￥", Float.parseFloat(v_price)) );
        holder.mNumView.setText("库存："+v_num_stock+"件");
        // 读取头图
        try {
            holder.mImageView.setImageBitmap(PlaceholderContent.db.GetItemPicture(holder.mItem.id));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (v_num_stock <= 0){
            holder.mAddButton.setEnabled(false);
            holder.mAddButton.setText("库存不足");
        }
        holder.mAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (LoginContent.LoginUser==null){
                    Toast.makeText(view.getContext(),"请先登录",Toast.LENGTH_SHORT).show();
                }else {
                    PlaceholderItem item =new PlaceholderItem(v_id,v_name,v_price,"1",String.valueOf(v_num_stock));
                    // 购物车数量查重
                    boolean flag=true;
                    for(int i=0;i <PlaceholderContent.ShoppingCarITEMS.size();i++){
                        PlaceholderItem f_item=PlaceholderContent.ShoppingCarITEMS.get(i);
                        if(Objects.equals(f_item.id, v_id)){
                            if(Integer.parseInt(f_item.num) >= v_num_stock){
                                Toast.makeText(view.getContext(), String.format("物品%s已经达到库存上限", v_name),Toast.LENGTH_SHORT).show();
                            }else {
                                try {
                                    f_item.num=String.valueOf(Integer.parseInt(f_item.num)+1);
                                    int status=PlaceholderContent.db.SetShoppingCarItem(v_id,f_item.num);
                                    if(status == 0){
                                        Toast.makeText(view.getContext(), String.format("物品%s数量为%s", v_name,f_item.num),Toast.LENGTH_SHORT).show();
                                    }
                                } catch (SQLException e) {
                                    e.printStackTrace();
                                }
                            }
                            flag=false;
                            break;
                        }
                    }
                    if (flag){
                        int status= 0;
                        try {
                            status = PlaceholderContent.db.SetShoppingCarItem(v_id,"1");
                            PlaceholderContent.ShoppingCarITEMS.add(item);
                            if(status == 0){
                                Toast.makeText(view.getContext(), String.format("物品%s添加到购物车", v_name),Toast.LENGTH_SHORT).show();
                            }
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }


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
        public PlaceholderItem mItem;
        public final Button mAddButton;
        public final ImageView mImageView;
        public ViewHolder(FragmentBuyItemBinding binding) {
            super(binding.getRoot());
            mNameView = binding.itemName;
            mPriceView = binding.itemPrice;
            mNumView = binding.itemNum;
            mAddButton=binding.buttonAddItem;
            mImageView=binding.imageView;
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mNameView.getText() + "'";
        }
    }
}