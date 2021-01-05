package com.quewea.booknetwork.book_management_ui.updatePublication;

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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.quewea.booknetwork.R;
import com.quewea.booknetwork.aplication_menu_ui.myPublications.MyPublicationsFragment;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class UpdatePublicationFragment extends Fragment {
    private UpdatePublicationViewModel updatePublicationViewModel;
    private EditText titulo, autor, editorial, yearE, isbn, paginas, trato, lenguaje, sinopsis, condicion;
    private Button btnUpdate, btnImg;
    private ImageView imgBook;
    private String url;
    private StorageReference storage;
    private ProgressDialog progressDialog;
    private FirebaseFirestore db;
    private String idBook = "";
    private static final int GALLERY_INTENT = 1;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        updatePublicationViewModel =
                new ViewModelProvider(this).get(UpdatePublicationViewModel.class);
        View root = inflater.inflate(R.layout.fragment_update_publication, container, false);
        final TextView textView = root.findViewById(R.id.txt_update_publication);
        updatePublicationViewModel.resourceTexts(getContext());
        updatePublicationViewModel.getTitle().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });

        getDelivery();
        inicializar(root);
        getBook(idBook);

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getContext(), "Hoal", Toast.LENGTH_SHORT).show();
                ocultarTeclado();
                update();
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

    private void showMyPublications(){
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Fragment myp = new MyPublicationsFragment();
        fragmentTransaction.replace(R.id.nav_host_fragment_book_management, myp).commit();
    }

    private void getDelivery(){
        Bundle delivery = getArguments();
        if (delivery == null){
            Toast.makeText(getContext(), "Error al recibir datos", Toast.LENGTH_SHORT).show();
        } else {
            idBook = delivery.getString("id");
        }
    }

    private void update(){
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
            progressDialog.setMessage("Actualizando...");
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
            b.put("img", imgUrl);
            db.collection("Books").document(idBook)
                    .set(b).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Toast.makeText(getContext(), "Actualización exitosa", Toast.LENGTH_SHORT).show();
                    showMyPublications();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getContext(), "Error al actualizar\nIntente de nuevo", Toast.LENGTH_SHORT).show();
                }
            });
            progressDialog.dismiss();
        }
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

    private void inicializar(View root){
        db = FirebaseFirestore.getInstance();
        progressDialog = new ProgressDialog(getContext());
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
        btnUpdate = (Button) root.findViewById(R.id.update_publication_btn_delete);
        btnImg = (Button) root.findViewById(R.id.brnImg);
        imgBook = (ImageView) root.findViewById(R.id.new_publication_img_view);
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
                Glide.with(getContext()).load(documentSnapshot.get("img")).fitCenter().centerCrop().into(imgBook);
                url = documentSnapshot.get("img").toString();
                progressDialog.dismiss();
            }
        });
    }

    private void ocultarTeclado(){
        View v = getActivity().getCurrentFocus();
        if (v != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
        }
    }
}