package com.quewea.booknetwork.login_register_ui.register;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
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
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthEmailException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.quewea.booknetwork.R;
import com.quewea.booknetwork.User;
import com.quewea.booknetwork.book_management;
import com.quewea.booknetwork.login_register_ui.login.LoginFragment;

import org.json.JSONObject;

import java.util.UUID;
import java.util.concurrent.Executor;

import static androidx.core.content.ContextCompat.getSystemService;

public class RegisterFragment extends Fragment {
    private Button btnRegister;
    private TextView txtLogin;
    private EditText username, firstName, lastName, pass;
    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;  //Declaracion objeto firebaseAuth
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

    private RegisterViewModel registerViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        registerViewModel =
                new ViewModelProvider(this).get(RegisterViewModel.class);
        View root = inflater.inflate(R.layout.fragment_register, container, false);

        inicializarFirebase();

        final TextView textView = root.findViewById(R.id.text_title_register);
        registerViewModel.resourceTexts(getContext());
        registerViewModel.getTitle().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });

        username = (EditText) root.findViewById(R.id.login_edit_text_user);
        firstName = (EditText) root.findViewById(R.id.register_edit_text_firt_name);
        lastName = (EditText) root.findViewById(R.id.register_edit_text_last_name);
        pass = (EditText) root.findViewById(R.id.login_edit_text_pass);

        btnRegister = (Button) root.findViewById(R.id.btn_register);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ocultarTeclado();
                register();
            }
        });

        txtLogin = (TextView) root.findViewById(R.id.txt_log_in);
        txtLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLogin();
            }
        });

        return root;
    }

    private void inicializarFirebase() {
        FirebaseApp.initializeApp(getContext());
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(getContext());
    }

    private void ocultarTeclado(){
        View v = getActivity().getCurrentFocus();
        if (v != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
        }
    }

    private void register() {
        //Obtención de datos
        String email = username.getText().toString().trim();
        String password  = pass.getText().toString().trim();
        String fn = firstName.getText().toString();
        String ln = lastName.getText().toString();

        User u = new User();
        u.setId(UUID.randomUUID().toString());
        u.setEmail(email);
        u.setFisrtname(fn);
        u.setLastname(ln);

        if (!validacionCampo(email, username) ||
                !validacionCampo(fn, firstName) ||
                !validacionCampo(ln, lastName) ||
                !validacionCampo(password, pass))
            return;
        else {

            progressDialog.setMessage("Realizando registro en linea...");
            progressDialog.show();

            //Creación de usuario
            firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        databaseReference.child("User").child(u.getId()).setValue(u);
                        Toast.makeText(getContext(), "Se ha registrado el usuario con el email: " + username.getText()+"\nYa puedes iniciar sesión", Toast.LENGTH_LONG).show();
                        showLogin();
                    } else {
                        if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                            Toast.makeText(getContext(), "El usuario " + username.getText() + " ya existe", Toast.LENGTH_LONG).show();
                        }else if (task.getException() instanceof FirebaseAuthWeakPasswordException) {
                            Toast.makeText(getContext(), "La contraseña debe tener al menos 6 caracteres", Toast.LENGTH_LONG).show();
                        } else
                            Toast.makeText(getContext(), "Se ha producido un error\nVerifique los datos", Toast.LENGTH_LONG).show();
                    }
                    progressDialog.dismiss();
                }
            });
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

    private void showLogin(){
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Fragment login = new LoginFragment();
        fragmentTransaction.add(R.id.drawer_layout_login, login).commit();
    }

}