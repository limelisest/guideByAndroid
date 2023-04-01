package com.limelisest.guide.ui.ManagerActivity.manageritem;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.navigation.fragment.NavHostFragment;

import com.limelisest.guide.R;
import com.limelisest.guide.placeholder.PlaceholderContent.PlaceholderItem;
import com.limelisest.guide.databinding.FragmentManageritemItemBinding;
import com.limelisest.guide.ui.ManagerActivity.ManagerHomeFragment;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link PlaceholderItem}.
 * TODO: Replace the implementation with code for your data type.
 */
public class ManagerItemRecyclerViewAdapter extends RecyclerView.Adapter<ManagerItemRecyclerViewAdapter.ViewHolder> {

    private final List<PlaceholderItem> mValues;

    public ManagerItemRecyclerViewAdapter(List<PlaceholderItem> items) {
        mValues = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new ViewHolder(FragmentManageritemItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));

    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        String item_id=mValues.get(position).id;
        holder.mItem = mValues.get(position);
        holder.mIdView.setText(item_id);
        holder.mNameView.setText(mValues.get(position).name);
        holder.mLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 传递点击的id
                Intent intent=new Intent(view.getContext(), ItemInfoActivity.class);
                intent.putExtra("item_id",item_id);
                intent.putExtra("flag","update");
                view.getContext().startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView mIdView;
        public final TextView mNameView;
        public final LinearLayout mLayout;
        public PlaceholderItem mItem;

        public ViewHolder(FragmentManageritemItemBinding binding) {
            super(binding.getRoot());
            mIdView=binding.itemId;
            mNameView = binding.itemName;
            mLayout=binding.layoutManageritemItem;
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mNameView.getText() + "'";
        }
    }
}