package com.quewea.booknetwork;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class login_register extends AppCompatActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_register);

        findViewById(R.id.btn_login).setOnClickListener(this);
        findViewById(R.id.txt_register_here).setOnClickListener(this);
    }

    private void login(View view) {
        Intent login = new Intent(this, book_management.class);
        startActivity(login);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.btn_login:
                login(v);
            case R.id.txt_register_here:
                login(v);
        }
    }
}