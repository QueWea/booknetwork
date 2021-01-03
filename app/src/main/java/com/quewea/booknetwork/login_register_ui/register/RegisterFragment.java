package com.quewea.booknetwork.login_register_ui.register;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.quewea.booknetwork.R;
import com.quewea.booknetwork.book_management;
import com.quewea.booknetwork.login_register_ui.login.LoginFragment;

import org.json.JSONObject;

public class RegisterFragment extends Fragment implements Response.Listener<JSONObject>, Response.ErrorListener {
    private Button btnRegister;
    private TextView txtLogin;
    EditText username, firstName, lastName, password;
    ProgressDialog progreso;
    RequestQueue request;
    JsonObjectRequest jsonObjectRequest;

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

        username = (EditText) root.findViewById(R.id.login_edit_text_user);
        firstName = (EditText) root.findViewById(R.id.register_edit_text_firt_name);
        lastName = (EditText) root.findViewById(R.id.register_edit_text_last_name);
        password = (EditText) root.findViewById(R.id.login_edit_text_pass);
        request = Volley.newRequestQueue(getContext());

        btnRegister = (Button) root.findViewById(R.id.btn_register);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cargarWebService();
                //register();
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

    public void cargarWebService(){
        progreso = new ProgressDialog(getContext());
        progreso.setMessage("Cargando...");
        progreso.show();

        String url = "http://192.168.100.42/wsBookNetwork/wsJSONRegisterUser.php"
                +"?username="+username.getText().toString()
                +"&firstName="+firstName.getText().toString()
                +"&lastName="+lastName.getText().toString()
                +"&password="+password.getText().toString();
        url = url.replace(" ","%20");
        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, this, this);
        request.add(jsonObjectRequest);
    }

    @Override
    public void onResponse(JSONObject response) {
        progreso.hide();
        Toast.makeText(getContext(), "Insercion correcta",Toast.LENGTH_SHORT).show();
        username.setText("");
        firstName.setText("");
        lastName.setText("");
        password.setText("");
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        progreso.hide();
        Toast.makeText(getContext(), "No se pudo registrar "+error.toString(), Toast.LENGTH_SHORT).show();
        //Log.i("ERROR",error.toString());
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