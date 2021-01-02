package com.quewea.booknetwork.aplication_menu_ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.quewea.booknetwork.Adapter;
import com.quewea.booknetwork.R;

import java.util.ArrayList;

public class HomeFragment extends Fragment {
    RecyclerView recyclerView;
    Adapter adapter;
    ArrayList<String> books;

    private HomeViewModel homeViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        final TextView textView = root.findViewById(R.id.txt_home);
        homeViewModel.resourceTexts(getContext());
        homeViewModel.getTitle().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });

        books = new ArrayList<>();
        books.add("EL SEÑOR DE LOS ANILLOS I");
        books.add("EL SEÑOR DE LOS ANILLOS II");
        books.add("EL SEÑOR DE LOS ANILLOS III");
        books.add("EL HOBBIT");

        recyclerView = root.findViewById(R.id.rvPublications);
        recyclerView.setLayoutManager(new LinearLayoutManager(root.getContext()));
        adapter = new Adapter(root.getContext(), books);

        adapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "TITULO: "
                        +books.get(recyclerView.getChildAdapterPosition(v)),Toast.LENGTH_SHORT).show();
            }
        });

        recyclerView.setAdapter(adapter);

        return root;
    }
}