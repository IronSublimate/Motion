package com.example.rt1.ui.myself;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.rt1.R;
import com.example.rt1.databinding.FragmentMyselfBinding;
import com.example.rt1.ui.RecordActivity;

public class MyselfFragment extends Fragment {

    private MyselfViewModel myselfViewModel;
    private FragmentMyselfBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        myselfViewModel =
                new ViewModelProvider(this).get(MyselfViewModel.class);

        binding = FragmentMyselfBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final Button button = root.findViewById(R.id.record);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), RecordActivity.class);
                startActivity(intent);
            }
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}