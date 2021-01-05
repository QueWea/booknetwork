package com.quewea.booknetwork.aplication_menu_ui.updateUser;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.quewea.booknetwork.R;

import java.util.HashMap;
import java.util.Map;

public class UpdateUserFragment extends Fragment {
    private Button btnUpdate;
    private EditText firstname, lastname, oldpass, newpass;
    private TextView username;
    private ProgressDialog progressDialog;
    private FirebaseFirestore db;
    private FirebaseAuth firebaseAuth;
    private UpdateUserViewModel updateUserViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        updateUserViewModel =
                new ViewModelProvider(this).get(UpdateUserViewModel.class);
        View root = inflater.inflate(R.layout.fragment_update_user, container, false);

        final TextView textView = root.findViewById(R.id.text_update_user);
        updateUserViewModel.resourceTexts(getContext());
        updateUserViewModel.getTitle().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });

        FirebaseApp.initializeApp(getContext());
        firebaseAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        progressDialog = new ProgressDialog(getContext());
        username = (TextView) root.findViewById(R.id.login_edit_text_user);
        firstname = (EditText) root.findViewById(R.id.register_edit_text_firt_name);
        lastname = (EditText) root.findViewById(R.id.register_edit_text_last_name);
        oldpass = (EditText) root.findViewById(R.id.login_edit_text_pass);
        newpass = (EditText) root.findViewById(R.id.update_user_edit_text_new_pass);

        btnUpdate = (Button) root.findViewById(R.id.btn_register);
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ocultarTeclado();
                updateUser();
            }
        });

        obtenerUser();

        return root;
    }

    private void updateUser(){
        String email = username.getText().toString().trim();
        String fn = firstname.getText().toString();
        String ln = lastname.getText().toString();
        String oldp  = oldpass.getText().toString().trim();
        String newp = newpass.getText().toString().trim();

        if (!validacionCampo(fn, firstname) ||
                !validacionCampo(ln, lastname)){
            return;
        } else if (!validacionCampo(oldp, oldpass)) {
            Toast.makeText(getContext(), "Ingrese su contraseña (Old password) para validar el cambio", Toast.LENGTH_SHORT).show();
            return;
        } else {
            progressDialog.setMessage("Actualizando datos...");
            progressDialog.show();

            firebaseAuth.signInWithEmailAndPassword(email, oldp).addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        Map<String, Object> u = new HashMap<>();
                        u.put("email", email);
                        u.put("firstname", fn);
                        u.put("lastname", ln);
                        db.collection("Users").document(email).set(u);
                        if (!TextUtils.isEmpty(newp)){
                            if (!updatePass(newp))
                                return;
                        }
                        Toast.makeText(getContext(), "Información actualizada", Toast.LENGTH_LONG).show();
                        oldpass.setText("");
                        newpass.setText("");
                    } else {
                        oldpass.setText("");
                        Toast.makeText(getContext(), "Error al actualizar\nVerifique la contraseña", Toast.LENGTH_LONG).show();
                    }
                    progressDialog.dismiss();
                }
            });
        }
    }

    boolean flag = false;
    private boolean updatePass(String newp){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        user.updatePassword(newp)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getContext(), "Información actualizada", Toast.LENGTH_LONG).show();
                            oldpass.setText("");
                            newpass.setText("");
                            flag = true;
                        } else {
                            if (task.getException() instanceof FirebaseAuthWeakPasswordException) {
                                Toast.makeText(getContext(), "La contraseña debe contener al menos 6 caracteres", Toast.LENGTH_LONG).show();
                            } else
                                Toast.makeText(getContext(), "Se ha producido un error\nVerifique los datos", Toast.LENGTH_LONG).show();
                            flag = false;
                        }
                    }
                });
        progressDialog.dismiss();
        return flag;
    }

    private void ocultarTeclado(){
        View v = getActivity().getCurrentFocus();
        if (v != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
        }
    }

    private boolean validacionCampo(String dato, EditText campo){
        if(TextUtils.isEmpty(dato)){
            campo.setError("Required");
            //Toast.makeText(getContext(),"Debe llenar el campo",Toast.LENGTH_LONG).show();
            oldpass.setText("");
            return false;
        } else
            return true;
    }

    private void obtenerUser(){
        progressDialog.setMessage("Cargando información...");
        progressDialog.show();
        SharedPreferences prefs = getActivity().getSharedPreferences("user", getContext().MODE_PRIVATE);
        String user = prefs.getString("username", "");

        db.collection("Users").document(user).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                username.setText(documentSnapshot.get("email").toString());
                firstname.setText(documentSnapshot.get("firstname").toString());
                lastname.setText(documentSnapshot.get("lastname").toString());
                progressDialog.dismiss();
            }
        });
    }
}