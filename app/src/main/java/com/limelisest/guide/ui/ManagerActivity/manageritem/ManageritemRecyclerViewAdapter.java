package com.limelisest.guide.ui.ManagerActivity.manageritem;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import com.limelisest.guide.placeholder.PlaceholderContent.PlaceholderItem;
import com.limelisest.guide.databinding.FragmentManageritemItemBinding;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link PlaceholderItem}.
 * TODO: Replace the implementation with code for your data type.
 */
public class ManageritemRecyclerViewAdapter extends RecyclerView.Adapter<ManageritemRecyclerViewAdapter.ViewHolder> {

    private final List<PlaceholderItem> mValues;

    public ManageritemRecyclerViewAdapter(List<PlaceholderItem> items) {
        mValues = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new ViewHolder(FragmentManageritemItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));

    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mIdView.setText(mValues.get(position).id);
        holder.mNameView.setText(mValues.get(position).name);
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView mIdView;
        public final TextView mNameView;
        public PlaceholderItem mItem;

        public ViewHolder(FragmentManageritemItemBinding binding) {
            super(binding.getRoot());
            mIdView=binding.itemId;
            mNameView = binding.itemName;
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mNameView.getText() + "'";
        }
    }
}