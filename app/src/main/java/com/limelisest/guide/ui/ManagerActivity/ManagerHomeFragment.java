package com.limelisest.guide.ui.ManagerActivity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;

import androidx.navigation.fragment.NavHostFragment;

import com.limelisest.guide.ManagerActivity;
import com.limelisest.guide.R;
import com.limelisest.guide.databinding.FragmentManagerHomeBinding;
import com.limelisest.guide.placeholder.LoginContent;

public class ManagerHomeFragment extends Fragment {

    private @NonNull FragmentManagerHomeBinding binding;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentManagerHomeBinding.inflate(inflater, container, false);
        binding.textviewFirst.setText("你好，"+ LoginContent.LoginUser);
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.buttonFirst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(ManagerHomeFragment.this)
                        .navigate(R.id.action_HomeFragment_to_ItemFragment);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}