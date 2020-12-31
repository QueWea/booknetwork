package com.quewea.booknetwork;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.quewea.booknetwork.aplication_menu_ui.home.HomeFragment;
import com.quewea.booknetwork.login_register_ui.login.LoginFragment;
import com.quewea.booknetwork.login_register_ui.register.RegisterFragment;

public class login_register extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_register);

        DrawerLayout drawer = findViewById(R.id.drawer_layout_login);
        showLogin();
        //findViewById(R.id.btn_login).setOnClickListener(this);
        //findViewById(R.id.txt_register_here).setOnClickListener(this);
    }

    private void login() {
        Intent login = new Intent(this, book_management.class);
        startActivity(login);
    }

    private void showLogin(){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Fragment login = new LoginFragment();
        fragmentTransaction.add(R.id.drawer_layout_login, login).commit();
    }

    private void register(){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Fragment register = new RegisterFragment();
        fragmentTransaction.add(R.id.drawer_layout_login, register).commit();
    }

    public void onClickListener(View v) {
        switch(v.getId()) {
            case R.id.btn_login:
                login();
            case R.id.txt_view_new_user_register_here:
                register();
        }
    }
}