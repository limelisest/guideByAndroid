package com.limelisest.guide.ui.MainActivity.buy_list;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.limelisest.guide.R;
import com.limelisest.guide.databinding.FragmentBuyListBinding;
import com.limelisest.guide.placeholder.PlaceholderContent;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * A fragment representing a list of Items.
 */
public class ItemFragment extends Fragment {
    public static String item_class="所有商品";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_buy_list, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.list);
        Spinner SpinnerItemClass=view.findViewById(R.id.spinner_item_class);
        // 为Spinner添加项目
        ArrayAdapter<String> spinnerDataAdapter = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_spinner_dropdown_item, PlaceholderContent.ItemClass);
        spinnerDataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        SpinnerItemClass.setAdapter(spinnerDataAdapter);
        // 设定切换列表
        SpinnerItemClass.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                try {
                    item_class=PlaceholderContent.ItemClass.get(SpinnerItemClass.getSelectedItemPosition());
                    PlaceholderContent.ITEMS = PlaceholderContent.db.GetItemList(item_class);
                    // 为列表添加项目
                    recyclerView.setAdapter(new MyItemRecyclerViewAdapter(PlaceholderContent.ITEMS));
                    spinnerDataAdapter.notifyDataSetChanged();
                } catch (SQLException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        return view;
    }
}