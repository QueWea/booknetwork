package com.quewea.booknetwork.book_management_ui.bookDetails;

import android.app.ProgressDialog;
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
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.quewea.booknetwork.R;

public class BookDetailsFragment extends Fragment {
    private BookDetailsViewModel bookDetailsViewModel;
    private EditText titulo, autor, editorial, yearE, isbn, paginas, trato, lenguaje, sinopsis, condicion, propietario;
    private Button btnContact;
    private String idBook = "";
    private ProgressDialog progressDialog;
    private FirebaseFirestore db;

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

        getDelivery();
        inicializarObjetos(root);
        getBook(idBook);

        btnContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Contactando...", Toast.LENGTH_SHORT).show();
            }
        });

        return root;
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
        editorial = (EditText) root.findViewById(R.id.book_details_view_text_editorial);
        yearE = (EditText) root.findViewById(R.id.update_publication_view_text_num_year_edition);
        isbn = (EditText) root.findViewById(R.id.update_publication_view_text_num_isbn);
        paginas = (EditText) root.findViewById(R.id.update_publication_view_text_num_pages);
        trato = (EditText) root.findViewById(R.id.update_publication_view_text_type_deal);
        lenguaje = (EditText) root.findViewById(R.id.update_publication_view_text_languaje);
        sinopsis = (EditText) root.findViewById(R.id.update_publication_view_text_synopsis);
        condicion = (EditText) root.findViewById(R.id.update_publication_view_text_book_condition);
        propietario = (EditText) root.findViewById(R.id.update_publication_view_text_owner);
        btnContact = (Button) root.findViewById(R.id.update_publication_btn_delete);

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
                getUser(documentSnapshot.get("username").toString());
                progressDialog.dismiss();
            }
        });
    }

    private void getUser(String username){
        db.collection("Users").document(username).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                propietario.setText(documentSnapshot.get("firstname").toString()
                        +" "+documentSnapshot.get("lastname").toString());
            }
        });
    }
}