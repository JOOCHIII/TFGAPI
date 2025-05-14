package com.example.logins;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.logins.AdministradorApi;
import com.example.logins.RetrofitClient;
import com.google.android.material.button.MaterialButton;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginAdminActivity extends AppCompatActivity {

    private EditText usuarioAdmin, contrasenaAdmin;
    private MaterialButton botonIniciarSesionAdmin;
    private TextView iniciarComoUsuario;
    private AdministradorApi administradorApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_admin); // Asegúrate de tener este layout

        // Inicializar vistas
        usuarioAdmin = findViewById(R.id.usuario_admin);
        contrasenaAdmin = findViewById(R.id.contrasena_admin);
        botonIniciarSesionAdmin = findViewById(R.id.boton_iniciar_sesion_admin);
        iniciarComoUsuario = findViewById(R.id.textviewIniciousuario);

        // Instanciar Retrofit
        administradorApi = RetrofitClient.getRetrofitInstance().create(AdministradorApi.class);

        // Click en botón iniciar sesión
        botonIniciarSesionAdmin.setOnClickListener(v -> iniciarSesionAdmin());

        // Click en texto "Iniciar como usuario"
        iniciarComoUsuario.setOnClickListener(v -> {
            startActivity(new Intent(LoginAdminActivity.this, LoginActivity.class));
            finish();
        });
    }

    private void iniciarSesionAdmin() {
        String usuario = usuarioAdmin.getText().toString().trim();
        String clave = contrasenaAdmin.getText().toString().trim();
        String origenApp = "tienda";

        if (usuario.isEmpty() || clave.isEmpty()) {
            Toast.makeText(this, "Rellena todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Iniciando sesión...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        // Llamada a la API
        Call<String> call = administradorApi.loginAdmin(usuario, clave, origenApp);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                progressDialog.dismiss();
                if (response.isSuccessful() && response.body() != null) {
                    String resultado = response.body();

                    switch (resultado) {
                        case "ACCESO_CONCEDIDO":
                            Toast.makeText(LoginAdminActivity.this, "Inicio de sesión exitoso como administrador", Toast.LENGTH_SHORT).show();

                            // Guardar en preferencias si quieres
                            SharedPreferences prefs = getSharedPreferences("MisPreferencias", MODE_PRIVATE);
                            SharedPreferences.Editor editor = prefs.edit();
                            editor.putString("nombre_Admin", usuario);
                            editor.apply();

                            Intent intent = new Intent(LoginAdminActivity.this, MainActivityAdmin.class);
                            intent.putExtra("nombre_Admin", usuario);
                            startActivity(intent);
                            finish();
                            break;

                        case "USUARIO_NO_ENCONTRADO":
                            Toast.makeText(LoginAdminActivity.this, "El usuario no existe", Toast.LENGTH_SHORT).show();
                            break;

                        case "CONTRASENA_INCORRECTA":
                            Toast.makeText(LoginAdminActivity.this, "Contraseña incorrecta", Toast.LENGTH_SHORT).show();
                            break;

                        default:
                            Toast.makeText(LoginAdminActivity.this, "Error: " + resultado, Toast.LENGTH_SHORT).show();
                            break;
                    }

                } else {
                    Toast.makeText(LoginAdminActivity.this, "Error de respuesta del servidor", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(LoginAdminActivity.this, "Error de conexión: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("LOGIN_API_ERROR", t.getMessage(), t);
            }
        });
    }
}
