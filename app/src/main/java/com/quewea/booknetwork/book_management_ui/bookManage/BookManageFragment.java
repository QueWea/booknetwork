package com.quewea.booknetwork.book_management_ui.bookManage;

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

public class BookManageFragment extends Fragment {

    private BookManageViewModel bookManageViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        bookManageViewModel =
                new ViewModelProvider(this).get(BookManageViewModel.class);
        View root = inflater.inflate(R.layout.fragment_book_manage, container, false);
        final TextView textView = root.findViewById(R.id.text_home);
        bookManageViewModel.resourceTexts(getContext());
        bookManageViewModel.getTitle().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }
}