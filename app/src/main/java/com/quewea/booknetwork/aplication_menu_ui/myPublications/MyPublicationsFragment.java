package com.quewea.booknetwork.aplication_menu_ui.myPublications;

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

public class MyPublicationsFragment extends Fragment {

    private MyPublicationsViewModel myPublicationsViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        myPublicationsViewModel =
                new ViewModelProvider(this).get(MyPublicationsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_my_publications, container, false);
        final TextView textView = root.findViewById(R.id.text_my_publications);
        myPublicationsViewModel.resourceTexts(getContext());
        myPublicationsViewModel.getTitle().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }
}