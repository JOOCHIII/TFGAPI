package com.example.logins;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
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
                    .addToBackStack(null)

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
                            .addToBackStack(null)

                            .commit();

//                } else if (id == R.id.nav_usuarios_admin) {
//                    Toast.makeText(MainActivityAdmin.this, "Gestión de usuarios", Toast.LENGTH_SHORT).show();
//                    // Aquí puedes lanzar otro fragment o activity si quieres
                } else if (id == R.id.nav_settings_admin) {
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.content_frame, new SettingsFragment_admin())
                            .addToBackStack(null)

                            .commit();
                } else if (id == R.id.mi_perfil) {
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.content_frame, new Fragment_profile_admin())
                            .addToBackStack(null)

                            .commit();
                } else if (id == R.id.gestion_pedidos_admin) {
                    getSupportFragmentManager().beginTransaction()
                            .setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right,
                                    android.R.anim.slide_in_left, android.R.anim.slide_out_right)
                            .replace(R.id.content_frame, new GestionPedidosAdmin())
                            .addToBackStack(null)
                            .commit();
                }
                else if (id == R.id.nav_admin) {
                    getSupportFragmentManager().beginTransaction()
                            .setCustomAnimations(
                                    android.R.anim.slide_in_left,  // animación para entrada del fragmento nuevo
                                    android.R.anim.slide_out_right, // animación para salida del fragmento viejo
                                    android.R.anim.slide_in_left,   // animación para entrada cuando haces popBackStack
                                    android.R.anim.slide_out_right  // animación para salida cuando haces popBackStack
                            )
                            .replace(R.id.content_frame, new RegistroAdminActivity())
                            .addToBackStack(null)
                            .commit();
                } else if (id == R.id.nav_notificaciones_admin) {
                    getSupportFragmentManager().beginTransaction()
                            .setCustomAnimations(
                                    android.R.anim.slide_in_left,
                                    android.R.anim.slide_out_right,
                                    android.R.anim.slide_in_left,
                                    android.R.anim.slide_out_right
                            )
                            .replace(R.id.content_frame, new NotificacionesFragmentAdmin())
                            .addToBackStack(null)
                            .commit();
                }
                else if (id == R.id.nav_logout_admin) {
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

        SharedPreferences preferences = getSharedPreferences("admin_prefs", MODE_PRIVATE);
        String nombreUsuarioAdmin = preferences.getString("nombre_admin", "Administrador");

        textoBienvenida_admin.setText("Bienvenido, " + nombreUsuarioAdmin);

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home_admin, menu); // ← Aquí usas tu nuevo archivo
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.nav_notificaciones_admin) {
            getSupportFragmentManager().beginTransaction()
                    .setCustomAnimations(
                            android.R.anim.slide_in_left,
                            android.R.anim.slide_out_right,
                            android.R.anim.slide_in_left,
                            android.R.anim.slide_out_right
                    )
                    .replace(R.id.content_frame, new NotificacionesFragmentAdmin())
                    .addToBackStack(null)
                    .commit();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }



    }

