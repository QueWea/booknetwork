package com.quewea.booknetwork.login_register_ui.register;

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
import com.quewea.booknetwork.login_register_ui.login.LoginFragment;

public class RegisterFragment extends Fragment {
    private Button btnRegister;
    private TextView txtLogin;

    private RegisterViewModel registerViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        registerViewModel =
                new ViewModelProvider(this).get(RegisterViewModel.class);
        View root = inflater.inflate(R.layout.fragment_register, container, false);
        final TextView textView = root.findViewById(R.id.text_title_register);
        registerViewModel.resourceTexts(getContext());
        registerViewModel.getTitle().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });

        btnRegister = (Button) root.findViewById(R.id.btn_register);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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

    private void register() {
        Intent login = new Intent(getActivity(), book_management.class);
        startActivity(login);
    }

    private void showLogin(){
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Fragment login = new LoginFragment();
        fragmentTransaction.add(R.id.drawer_layout_login, login).commit();
    }
}