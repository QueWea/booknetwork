package com.quewea.booknetwork.login_register_ui.login;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
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
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.quewea.booknetwork.R;
import com.quewea.booknetwork.book_management;
import com.quewea.booknetwork.login_register_ui.register.RegisterFragment;

public class LoginFragment extends Fragment {
    private Button btnLogin;
    private TextView txtRegister;
    private EditText username, pass;
    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;
    private LoginViewModel loginViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        loginViewModel =
                new ViewModelProvider(this).get(LoginViewModel.class);
        View root = inflater.inflate(R.layout.fragment_login, container, false);
        final TextView textView = root.findViewById(R.id.text_title_login);
        loginViewModel.resourceTexts(getContext());
        loginViewModel.getTitle().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });

        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(getContext());
        username = (EditText) root.findViewById(R.id.login_edit_text_user);
        pass = (EditText) root.findViewById(R.id.login_edit_text_pass);
        btnLogin = (Button) root.findViewById(R.id.btn_register);
        txtRegister = (TextView) root.findViewById(R.id.txt_view_new_user_register_here);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ocultarTeclado();
                login();
            }
        });

        txtRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();
            }
        });

        return root;
    }

    private void login() {
        final String email = username.getText().toString().trim();
        String password = pass.getText().toString().trim();

        if (!validacionCampo(email, username) ||
                !validacionCampo(password, pass))
            return;
        else {
            progressDialog.setMessage("Iniciando sesión...");
            progressDialog.show();

            //Iniciar sesión
            firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    //checking if success
                    if (task.isSuccessful()) {
                        int pos = email.indexOf("@");
                        String user = email.substring(0, pos);
                        Toast.makeText(getContext(), "Bienvenido " + user, Toast.LENGTH_LONG).show();

                        SharedPreferences preferences = getActivity().getSharedPreferences("user", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putString("username", email);
                        editor.commit();

                        Intent login = new Intent(getActivity(), book_management.class);
                        startActivity(login);
                    } else
                        Toast.makeText(getContext(), "No se pudo iniciar sesión\nVerifique las credenciales", Toast.LENGTH_LONG).show();
                    progressDialog.dismiss();
                }
            });
        }
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
            Toast.makeText(getContext(),"Debe llenar todos los campos",Toast.LENGTH_LONG).show();
            return false;
        } else
            return true;
    }

    private void register(){
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Fragment register = new RegisterFragment();
        fragmentTransaction.add(R.id.drawer_layout_login, register).commit();
    }
}