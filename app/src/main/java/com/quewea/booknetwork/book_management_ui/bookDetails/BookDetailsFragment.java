package com.quewea.booknetwork.book_management_ui.bookDetails;

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

public class BookDetailsFragment extends Fragment {

    private BookDetailsViewModel bookDetailsViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        bookDetailsViewModel =
                new ViewModelProvider(this).get(BookDetailsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_book_details, container, false);
        final TextView textView = root.findViewById(R.id.txt_book_details);
        bookDetailsViewModel.resourceTexts(getContext());
        bookDetailsViewModel.getTitle().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }
}