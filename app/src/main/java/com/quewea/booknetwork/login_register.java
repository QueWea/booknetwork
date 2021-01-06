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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

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

    }

    private void showLogin(){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Fragment login = new LoginFragment();
        fragmentTransaction.add(R.id.drawer_layout_login, login).commit();
    }

    @Override
    public void onBackPressed() {

    }

}