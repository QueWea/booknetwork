package com.quewea.booknetwork.book_management_ui.bookManage;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.quewea.booknetwork.Book;
import com.quewea.booknetwork.R;
import com.quewea.booknetwork.aplication_menu_ui.myPublications.MyPublicationsFragment;
import com.quewea.booknetwork.book_management_ui.updatePublication.UpdatePublicationFragment;

public class BookManageFragment extends Fragment {
    private String idBook = "";
    private EditText titulo, autor, editorial, yearE, isbn, paginas, trato, lenguaje, sinopsis, condicion;
    private Button btnUpdate, btnDelete;
    private ProgressDialog progressDialog;
    private FirebaseFirestore db;

    private BookManageViewModel bookManageViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        bookManageViewModel = new ViewModelProvider(this).get(BookManageViewModel.class);
        View root = inflater.inflate(R.layout.fragment_book_manage, container, false);
        final TextView textView = root.findViewById(R.id.txt_book_manage);
        bookManageViewModel.resourceTexts(getContext());
        bookManageViewModel.getTitle().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });

        getDelivery();
        inicializarObjetos(root);
        getBook(idBook);

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getContext(), "Borrando...", Toast.LENGTH_SHORT).show();
                Bundle delivery = new Bundle();
                delivery.putString("id", idBook);
                bookUpdate(delivery);
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Borrando...", Toast.LENGTH_SHORT).show();
                delete();
            }
        });

        return root;
    }

    private void delete(){
        db.collection("Books").document(idBook).delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(getContext(), "Borrado exitosamente", Toast.LENGTH_SHORT).show();
                showMyPublications();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(), "Error al borrar el libro", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getDelivery(){
        Bundle delivery = getArguments();
        if (delivery == null){
            Toast.makeText(getContext(), "Error al recibir datos", Toast.LENGTH_SHORT).show();
        } else {
            idBook = delivery.getString("id");
        }
    }

    private void inicializarObjetos(View root) {
        titulo = (EditText) root.findViewById(R.id.book_details_view_text_title);
        autor = (EditText) root.findViewById(R.id.update_publication_view_text_autor);
        editorial = (EditText) root.findViewById(R.id.update_publication_view_text_owner);
        yearE = (EditText) root.findViewById(R.id.update_publication_view_text_num_year_edition);
        isbn = (EditText) root.findViewById(R.id.update_publication_view_text_num_isbn);
        paginas = (EditText) root.findViewById(R.id.update_publication_view_text_num_pages);
        trato = (EditText) root.findViewById(R.id.update_publication_view_text_type_deal);
        lenguaje = (EditText) root.findViewById(R.id.update_publication_view_text_languaje);
        sinopsis = (EditText) root.findViewById(R.id.update_publication_view_text_synopsis);
        condicion = (EditText) root.findViewById(R.id.update_publication_view_text_book_condition);
        btnUpdate = (Button) root.findViewById(R.id.update_publication_btn_update);
        btnDelete = (Button) root.findViewById(R.id.update_publication_btn_delete);

        db = FirebaseFirestore.getInstance();
        progressDialog = new ProgressDialog(getContext());
    }

    private void getBook(String idBook){
        progressDialog.setMessage("Cargando información...");
        progressDialog.show();

        db.collection("Books").document(idBook).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                titulo.setText(documentSnapshot.get("title").toString());
                autor.setText(documentSnapshot.get("author").toString());
                editorial.setText(documentSnapshot.get("editorial").toString());
                yearE.setText(documentSnapshot.get("year").toString());
                isbn.setText(documentSnapshot.get("isbn").toString());
                paginas.setText(documentSnapshot.get("pages").toString());
                trato.setText(documentSnapshot.get("deal").toString());
                lenguaje.setText(documentSnapshot.get("language").toString());
                sinopsis.setText(documentSnapshot.get("synopsis").toString());
                condicion.setText(documentSnapshot.get("condition").toString());

                progressDialog.dismiss();
            }
        });
    }

    private void bookUpdate(Bundle delivery){
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Fragment bookUpdate = new UpdatePublicationFragment();
        bookUpdate.setArguments(delivery);
        fragmentTransaction.add(R.id.nav_host_fragment_book_management, bookUpdate).commit();
    }

    private void showMyPublications(){
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Fragment myp = new MyPublicationsFragment();
        fragmentTransaction.replace(R.id.nav_host_fragment_book_management, myp).commit();
    }
}