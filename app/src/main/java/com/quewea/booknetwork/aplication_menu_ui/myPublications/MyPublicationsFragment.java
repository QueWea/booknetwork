package com.quewea.booknetwork.aplication_menu_ui.myPublications;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.quewea.booknetwork.Adapter;
import com.quewea.booknetwork.R;
import com.quewea.booknetwork.book_management_ui.bookManage.BookManageFragment;
import com.quewea.booknetwork.login_register_ui.register.RegisterFragment;

import java.util.ArrayList;

public class MyPublicationsFragment extends Fragment {
    RecyclerView recyclerView;
    Adapter adapter;
    ArrayList<String> books;

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

        books = new ArrayList<>();
        books.add("CIEN AÑOS DE SOLEDAD");
        books.add("REBELION EN LA GRANJA");
        books.add("EL SEÑOR DE LAS MOSCAS");
        books.add("EL ARTE DE LA GUERRA");

        recyclerView = root.findViewById(R.id.rvMyPublications);
        recyclerView.setLayoutManager(new LinearLayoutManager(root.getContext()));
        adapter = new Adapter(root.getContext(), books);

        adapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "TITULO: "
                        +books.get(recyclerView.getChildAdapterPosition(v)),Toast.LENGTH_SHORT).show();
                bookManage();
            }
        });

        recyclerView.setAdapter(adapter);

        return root;
    }

    private void bookManage(){
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Fragment bookManage = new BookManageFragment();
        fragmentTransaction.add(R.id.drawer_layout, bookManage).commit();
    }
}