package com.limelisest.guide.ui.ManagerActivity.manageritem;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.limelisest.guide.R;
import com.limelisest.guide.placeholder.PlaceholderContent;

/**
 * A fragment representing a list of Items.
 */
public class ManagerItemFragment extends Fragment {
    public static String item_id;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_manageritem_list, container, false);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.list);
        recyclerView.setAdapter(new ManagerItemRecyclerViewAdapter(PlaceholderContent.ITEMS));

        FloatingActionButton floatingActionButton=view.findViewById(R.id.floatingActionButton);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent=new Intent(view.getContext(), ItemInfoActivity.class);
//                startActivity(intent);
            }
        });
        return view;
    }
}