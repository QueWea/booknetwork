package com.quewea.booknetwork.aplication_menu_ui.home;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
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

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.quewea.booknetwork.Adapter;
import com.quewea.booknetwork.Book;
import com.quewea.booknetwork.R;
import com.quewea.booknetwork.book_management_ui.bookDetails.BookDetailsFragment;
import com.quewea.booknetwork.book_management_ui.bookManage.BookManageFragment;

import java.util.ArrayList;

public class HomeFragment extends Fragment {
    RecyclerView recyclerView;
    Adapter adapter;
    private FirebaseFirestore db;
    ProgressDialog progressDialog;

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

        db = FirebaseFirestore.getInstance();
        progressDialog = new ProgressDialog(getContext());
        Bundle delivery = new Bundle();

        recyclerView = root.findViewById(R.id.rvPublications);
        recyclerView.setLayoutManager(new LinearLayoutManager(root.getContext()));

        obtenerBooks(root);

        adapter.setOnItemClickListener(new Adapter.OnItemClickListener() {
            @Override
            public void OnItemClick(DocumentSnapshot documentSnapshot, int position) {
                //Book book = documentSnapshot.toObject(Book.class);
                delivery.putString("id", documentSnapshot.getId());
                bookDetails(delivery);
            }
        });

        recyclerView.setAdapter(adapter);

        return root;
    }

    private void obtenerBooks(View root){
        Query query = db.collection("Books");
        FirestoreRecyclerOptions<Book> fBook = new FirestoreRecyclerOptions.Builder<Book>()
                .setQuery(query, Book.class).build();
        adapter = new Adapter(root.getContext(), fBook);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    private void bookDetails(Bundle delivery){
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Fragment bookDetails = new BookDetailsFragment();
        bookDetails.setArguments(delivery);
        fragmentTransaction.add(R.id.nav_host_fragment_book_management, bookDetails).commit();
    }

}