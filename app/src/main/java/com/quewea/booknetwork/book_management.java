package com.quewea.booknetwork;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;
import com.quewea.booknetwork.aplication_menu_ui.home.HomeFragment;
import com.quewea.booknetwork.aplication_menu_ui.myPublications.MyPublicationsFragment;
import com.quewea.booknetwork.aplication_menu_ui.newPublication.NewPublicationFragment;
import com.quewea.booknetwork.aplication_menu_ui.updateUser.UpdateUserFragment;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class book_management extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_management);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.menu_nav);
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_my_publications, R.id.nav_new_publication, R.id.nav_update_user, R.id.nav_log_out)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_book_management);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        navigationView.bringToFront();
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                switch(item.getItemId()) {
                    case R.id.nav_home:
                        Fragment home = new HomeFragment();
                        fragmentTransaction.replace(R.id.nav_host_fragment_book_management, home).commit();
                        break;
                    case R.id.nav_my_publications:
                        Fragment myP = new MyPublicationsFragment();
                        fragmentTransaction.replace(R.id.nav_host_fragment_book_management, myP).commit();
                        //Navigation.findNavController(navigationView).navigate(R.id.nav_my_publications);
                        break;
                    case R.id.nav_new_publication:
                        Fragment newP = new NewPublicationFragment();
                        fragmentTransaction.replace(R.id.nav_host_fragment_book_management, newP).commit();
                        break;
                    case R.id.nav_update_user:
                        Fragment updateUser = new UpdateUserFragment();
                        fragmentTransaction.replace(R.id.nav_host_fragment_book_management, updateUser).commit();
                        break;
                    case R.id.nav_log_out:
                        //Toast.makeText(getApplicationContext(),"Cerrar sesion",Toast.LENGTH_SHORT).show();
                        logout(navigationView);
                        return true;
                }
                item.setChecked(true);
                drawer.closeDrawers();
                return true;
            }
        });
    }

    public void logout(View view) {
        Intent logout = new Intent(this, login_register.class);
        startActivity(logout);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_book_management);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
}