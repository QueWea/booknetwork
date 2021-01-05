package com.quewea.booknetwork.aplication_menu_ui.myPublications;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.quewea.booknetwork.Adapter;
import com.quewea.booknetwork.Book;
import com.quewea.booknetwork.R;
import com.quewea.booknetwork.book_management_ui.bookManage.BookManageFragment;
import com.quewea.booknetwork.login_register_ui.register.RegisterFragment;

import java.util.ArrayList;

public class MyPublicationsFragment extends Fragment {
    RecyclerView recyclerView;
    Adapter adapter;
    private FirebaseFirestore db;
    ProgressDialog progressDialog;

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

        db = FirebaseFirestore.getInstance();
        progressDialog = new ProgressDialog(getContext());
        Bundle delivery = new Bundle();
        recyclerView = root.findViewById(R.id.rvMyPublications);

        recyclerView.setLayoutManager(new LinearLayoutManager(root.getContext()));
        getBooks(root);

        adapter.setOnItemClickListener(new Adapter.OnItemClickListener() {
            @Override
            public void OnItemClick(DocumentSnapshot documentSnapshot, int position) {
                //book = documentSnapshot.toObject(Book.class);
                //Toast.makeText(getContext(), "TITULO: "+documentSnapshot.getId(),Toast.LENGTH_SHORT).show();
                delivery.putString("id", documentSnapshot.getId());
                bookManage(delivery);
            }
        });

        recyclerView.setAdapter(adapter);

        return root;
    }

    private void getBooks(View root){
        SharedPreferences prefs = getActivity().getSharedPreferences("user", getContext().MODE_PRIVATE);
        String user = prefs.getString("username", "");

        Query query = db.collection("Books").whereEqualTo("username", user);
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

    private void bookManage(Bundle delivery){
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Fragment bookManage = new BookManageFragment();
        bookManage.setArguments(delivery);
        fragmentTransaction.add(R.id.nav_host_fragment_book_management, bookManage).commit();
    }
}