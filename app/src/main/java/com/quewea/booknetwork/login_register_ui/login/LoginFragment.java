package com.quewea.booknetwork.login_register_ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.quewea.booknetwork.R;
import com.quewea.booknetwork.book_management;
import com.quewea.booknetwork.login_register_ui.register.RegisterFragment;

public class LoginFragment extends Fragment {
    private Button btnLogin;
    private TextView txtRegister;

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

        btnLogin = (Button) root.findViewById(R.id.btn_register);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });

        txtRegister = (TextView) root.findViewById(R.id.txt_view_new_user_register_here);
        txtRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();
            }
        });

        return root;
    }

    private void login() {
        Intent login = new Intent(getActivity(), book_management.class);
        startActivity(login);
    }

    private void register(){
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Fragment register = new RegisterFragment();
        fragmentTransaction.add(R.id.drawer_layout_login, register).commit();
    }
}