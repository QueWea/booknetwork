package com.quewea.booknetwork.aplication_menu_ui.updateUser;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.quewea.booknetwork.R;

public class UpdateUserFragment extends Fragment {

    private UpdateUserViewModel updateUserViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        updateUserViewModel =
                new ViewModelProvider(this).get(UpdateUserViewModel.class);
        View root = inflater.inflate(R.layout.fragment_update_user, container, false);
        final TextView textView = root.findViewById(R.id.text_update_user);
        updateUserViewModel.resourceTexts(getContext());
        updateUserViewModel.getTitle().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }
}