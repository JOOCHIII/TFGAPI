package com.example.logins;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.navigation.NavigationView;

public class MainActivityAdmin extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private MaterialToolbar toolbar;
    private ActionBarDrawerToggle toggle;
    private TextView textoBienvenida_admin;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_main);  // Asegúrate de que este layout existe

        // Referencias
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigation_view_admin);
        toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        // Toggle para abrir/cerrar el drawer desde el toolbar
        toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close
        );

        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        // Fragment por defecto
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.content_frame, new home_fragment_admin())
                    .commit();
            navigationView.setCheckedItem(R.id.nav_home_admin);
        }

        // Navegación de opciones del menú
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();

                if (id == R.id.nav_home_admin) {
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.content_frame, new home_fragment_admin())
                            .commit();
                } else if (id == R.id.nav_usuarios_admin) {
                    Toast.makeText(MainActivityAdmin.this, "Gestión de usuarios", Toast.LENGTH_SHORT).show();
                    // Aquí puedes lanzar otro fragment o activity si quieres
                } else if (id == R.id.nav_settings_admin) {
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.content_frame, new SettingsFragment_admin())
                            .commit();
                } else if (id == R.id.mi_perfil) {
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.content_frame, new Fragment_profile_admin())
                            .commit();
                } else if (id == R.id.nav_logout_admin) {
                    Toast.makeText(MainActivityAdmin.this, "Cerrando sesión...", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(MainActivityAdmin.this, LoginAdminActivity.class);
                    startActivity(intent);
                    finish();
                }

                drawerLayout.closeDrawers();
                return true;
            }
        });
        View headerView_admin = navigationView.getHeaderView(0);
        textoBienvenida_admin = headerView_admin.findViewById(R.id.textoBienvenida_admin);

        // Mostrar nombre de usuario admin en el header
        String nombreUsuarioAdmin = getIntent().getStringExtra("nombre_Admin");
        if (nombreUsuarioAdmin != null && !nombreUsuarioAdmin.isEmpty()) {
            textoBienvenida_admin.setText("Bienvenido, " + nombreUsuarioAdmin);
        }




    }
}
