package com.limelisest.guide.ui.ManagerActivity.manageruser;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.limelisest.guide.databinding.FragmentManageritemItemBinding;
import com.limelisest.guide.placeholder.PlaceholderContent;
import com.limelisest.guide.placeholder.PlaceholderContent.PlaceholderUser;
import com.limelisest.guide.databinding.FragmentManageruserItemBinding;
import com.limelisest.guide.ui.ManagerActivity.manageritem.ManagerItemRecyclerViewAdapter;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link PlaceholderUser}.
 * TODO: Replace the implementation with code for your data type.
 */
public class ManagerUserItemRecyclerViewAdapter extends RecyclerView.Adapter<ManagerUserItemRecyclerViewAdapter.ViewHolder> {

    private final List<PlaceholderUser> mValues;

    public ManagerUserItemRecyclerViewAdapter(List<PlaceholderContent.PlaceholderUser> items) {
        mValues = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new ViewHolder(FragmentManageruserItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));

    }

    @Override
    public void onBindViewHolder(final ManagerUserItemRecyclerViewAdapter.ViewHolder holder, int position) {
        String user_id=mValues.get(position).id;
        holder.mItem = mValues.get(position);
        holder.mIdView.setText(user_id);
        holder.mNameView.setText(mValues.get(position).user_name);
        holder.mLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(view.getContext(), "你点击了"+user_id, Toast.LENGTH_SHORT).show();
//                Intent intent=new Intent(view.getContext(), ManagerItemInfoActivity.class);
//                intent.putExtra("id",item_id);
//                view.getContext().startActivity(intent);
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
        public PlaceholderUser mItem;

        public ViewHolder(FragmentManageruserItemBinding binding) {
            super(binding.getRoot());
            mIdView=binding.userId;
            mNameView = binding.userName;
            mLayout=binding.layoutManageritemItem;
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mNameView.getText() + "'";
        }
    }
}