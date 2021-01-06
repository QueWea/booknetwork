package com.quewea.booknetwork.aplication_menu_ui.newPublication;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.quewea.booknetwork.R;
import com.quewea.booknetwork.aplication_menu_ui.myPublications.MyPublicationsFragment;

import java.util.HashMap;
import java.util.Map;

public class NewPublicationFragment extends Fragment {
    private EditText titulo, autor, editorial, yearE, isbn, paginas, trato, lenguaje, sinopsis, condicion;
    private Button btnPub, btnImg;
    private ImageView imgBook;
    private ProgressDialog progressDialog;
    private FirebaseFirestore db;
    private StorageReference storage;
    private String url;
    private static final int GALLERY_INTENT = 1;

    private NewPublicationViewModel newPublicationViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        newPublicationViewModel =
                new ViewModelProvider(this).get(NewPublicationViewModel.class);
        View root = inflater.inflate(R.layout.fragment_new_publication, container, false);
        final TextView textView = root.findViewById(R.id.text_new_publication);
        newPublicationViewModel.resourceTexts(getContext());
        newPublicationViewModel.getTitle().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });

        inicializar(root);

        btnPub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ocultarTeclado();
                publicar();
            }
        });

        btnImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_PICK);
                i.setType("image/*");
                startActivityForResult(i, GALLERY_INTENT);
            }
        });

        return root;
    }

    private void ocultarTeclado(){
        View v = getActivity().getCurrentFocus();
        if (v != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_INTENT && resultCode == getActivity().RESULT_OK){
            Uri uri = data.getData();
            StorageReference filePath = storage.child("booksImages").child(uri.getLastPathSegment());
            filePath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Task<Uri> imgDownload = taskSnapshot.getStorage().getDownloadUrl();
                    imgDownload.addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            url = uri.toString();
                            Toast.makeText(getContext(), "Foto cargada correctamente", Toast.LENGTH_SHORT);
                            Log.i("URL ",url);
                            Glide.with(getContext()).load(url).fitCenter().centerCrop().into(imgBook);
                        }
                    });
                }
            });
        }
    }

    private void inicializar(View root){
        FirebaseApp.initializeApp(getContext());
        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance().getReference();
        progressDialog = new ProgressDialog(getContext());
        titulo = (EditText) root.findViewById(R.id.new_publication_edit_text_title);
        autor = (EditText) root.findViewById(R.id.new_publication_edit_text_autor);
        editorial = (EditText) root.findViewById(R.id.new_publication_edit_text_editorial);
        yearE = (EditText) root.findViewById(R.id.new_publication_edit_text_num_year_edition);
        isbn = (EditText) root.findViewById(R.id.new_publication_edit_text_num_isbn);
        paginas = (EditText) root.findViewById(R.id.new_publication_edit_text_num_pages);
        trato = (EditText) root.findViewById(R.id.new_publication_edit_text_type_deal);
        lenguaje = (EditText) root.findViewById(R.id.new_publication_edit_text_languaje);
        sinopsis = (EditText) root.findViewById(R.id.new_publication_edit_text_synopsis);
        condicion = (EditText) root.findViewById(R.id.new_publication_edit_text_book_condition);
        btnPub = (Button) root.findViewById(R.id.new_publication_btn_publish);
        btnImg = (Button) root.findViewById(R.id.brnImg);
        imgBook = (ImageView) root.findViewById(R.id.new_publication_img_view);
    }

    private void publicar(){
        String title = titulo.getText().toString();
        String author = autor.getText().toString();
        String edit = editorial.getText().toString();
        String year = yearE.getText().toString();
        String isbnCode = isbn.getText().toString();
        String pages = paginas.getText().toString();
        String deal = trato.getText().toString();
        String language = lenguaje.getText().toString();
        String synopsis = sinopsis.getText().toString();
        String condition = condicion.getText().toString();
        String imgUrl = url;
        SharedPreferences prefs = getActivity().getSharedPreferences("user", getContext().MODE_PRIVATE);
        String user = prefs.getString("username", "");

        if (!validacionCampo(title, titulo) ||
                !validacionCampo(author, autor) ||
                !validacionCampo(edit, editorial) ||
                !validacionCampo(year, yearE) ||
                !validacionCampo(isbnCode, isbn) ||
                !validacionCampo(pages, paginas) ||
                !validacionCampo(deal, trato) ||
                !validacionCampo(language, lenguaje) ||
                !validacionCampo(synopsis, sinopsis) ||
                !validacionCampo(condition, condicion)||
                !validacionCampoImg(imgUrl))
            return;
        else {
            progressDialog.setMessage("Publicando...");
            progressDialog.show();

            Map<String, Object> b = new HashMap<>();
            b.put("title", title);
            b.put("author", author);
            b.put("editorial", edit);
            b.put("year", year);
            b.put("isbn", isbnCode);
            b.put("pages", pages);
            b.put("deal", deal);
            b.put("language", language);
            b.put("synopsis", synopsis);
            b.put("condition", condition);
            b.put("username", user);
            b.put("img", url);
            db.collection("Books")
                    .add(b)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(getContext(), "Publicacion exitosa", Toast.LENGTH_SHORT).show();
                            showMyPublications();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getContext(), "Error al publicar", Toast.LENGTH_SHORT).show();
                }
            });
            progressDialog.dismiss();
        }
    }

    private void showMyPublications(){
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Fragment myp = new MyPublicationsFragment();
        fragmentTransaction.replace(R.id.nav_host_fragment_book_management, myp).commit();
    }

    private boolean validacionCampo(String dato, EditText campo){
        if(TextUtils.isEmpty(dato)){
            campo.setError("Required");
            Toast.makeText(getContext(),"Debe llenar todos los campos",Toast.LENGTH_LONG).show();
            return false;
        } else
            return true;
    }

    private boolean validacionCampoImg(String dato){
        if(TextUtils.isEmpty(dato)){
            Toast.makeText(getContext(),"Debe seleccionar una imagen",Toast.LENGTH_LONG).show();
            return false;
        } else
            return true;
    }

}