package com.example.rt1.ui.friend;

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
import com.example.rt1.databinding.FragmentFriendBinding;

public class FriendFragment extends Fragment {

    private FriendViewModel friendViewModel;
    private FragmentFriendBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        friendViewModel =
                new ViewModelProvider(this).get(FriendViewModel.class);

        binding = FragmentFriendBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final Button button = root.findViewById(R.id.invite);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                systemShareTxt();
            }
        });

        return root;


    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void systemShareTxt() {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");

        intent.putExtra(Intent.EXTRA_TEXT, "我正在RT！跑步，和我一起跑步吧！");
        intent.putExtra(Intent.EXTRA_TEXT, "我正在RT！跑步，和我一起跑步吧！");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(Intent.createChooser(intent, "分享到"));
    }
}
