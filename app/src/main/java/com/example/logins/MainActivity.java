package com.example.logins;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private MaterialToolbar toolbar;
    private ActionBarDrawerToggle toggle;
    private TextView textoBienvenida;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigation_view);
        toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close
        );

        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.content_frame, new HomeFragment())
                    .commit();
            navigationView.setCheckedItem(R.id.nav_home);
        }

        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.nav_home) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.content_frame, new HomeFragment())
                        .commit();
            } else if (id == R.id.nav_profile) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.content_frame, new Fragment_profile())
                        .commit();
            } else if (id == R.id.nav_settings) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.content_frame, new SettingsFragment())
                        .commit();
            } else if (id == R.id.nav_reports) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.content_frame, new ReportsFragment())
                        .commit();
            } else if (id == R.id.nav_notifications) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.content_frame, new NotificationsFragment())
                        .commit();
            } else if (id == R.id.nav_logout) {
                Toast.makeText(MainActivity.this, "Cerrando sesión...", Toast.LENGTH_SHORT).show();

                // Limpiar SharedPreferences al cerrar sesión
                getSharedPreferences("usuario_prefs", MODE_PRIVATE).edit().clear().apply();

                finish();
            }

            drawerLayout.closeDrawers();
            return true;
        });

        // Mostrar nombre de usuario en el header, leyendo SharedPreferences
        View headerView = navigationView.getHeaderView(0);
        textoBienvenida = headerView.findViewById(R.id.textoBienvenida);

        SharedPreferences preferences = getSharedPreferences("usuario_prefs", MODE_PRIVATE);
        long idUsuario = preferences.getLong("id_usuario", -1);
        String nombreUsuario = preferences.getString("nombre_usuario", "Usuario");

        Log.d("MAIN_DEBUG", "ID cargado en MainActivity: " + idUsuario);
        Toast.makeText(this, "Tu ID es: " + idUsuario, Toast.LENGTH_SHORT).show();

        textoBienvenida.setText("Bienvenido, " + nombreUsuario);
    }
}


