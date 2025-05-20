package com.example.logins;

import android.app.AlertDialog;
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
import com.example.logins.LoginResponse;
import com.example.logins.R;
import com.google.android.material.button.MaterialButton;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.view.LayoutInflater;
import android.view.View;

public class LoginAdminActivity extends AppCompatActivity {

    private EditText usuarioAdmin, contrasenaAdmin;
    private MaterialButton botonIniciarSesionAdmin;
    private TextView iniciarComoUsuario;
    private AdministradorApi administradorApi;
    private AlertDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_admin); // Asegúrate de tener este layout

        usuarioAdmin = findViewById(R.id.usuario_admin);
        contrasenaAdmin = findViewById(R.id.contrasena_admin);
        botonIniciarSesionAdmin = findViewById(R.id.boton_iniciar_sesion_admin);
        iniciarComoUsuario = findViewById(R.id.textviewIniciousuario);

        administradorApi = RetrofitClient.getRetrofitInstance().create(AdministradorApi.class);

        botonIniciarSesionAdmin.setOnClickListener(v -> iniciarSesionAdmin());

        iniciarComoUsuario.setOnClickListener(v -> {
            startActivity(new Intent(LoginAdminActivity.this, LoginActivity.class));
            finish();
        });
    }

    private void showProgressDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(LoginAdminActivity.this);
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_progress, null);
        builder.setView(view);
        builder.setCancelable(false);
        progressDialog = builder.create();
        progressDialog.show();
    }

    private void hideProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    public void iniciarSesionAdmin() {
        String usuario = usuarioAdmin.getText().toString().trim();
        String clave = contrasenaAdmin.getText().toString().trim();
        String origenApp = "tienda";

        if (usuario.isEmpty() && clave.isEmpty()) {
            Toast.makeText(this, "Por favor, introduce el usuario y la contraseña", Toast.LENGTH_SHORT).show();
            return;
        }

        if (usuario.isEmpty()) {
            Toast.makeText(this, "El campo usuario está vacío", Toast.LENGTH_SHORT).show();
            return;
        }

        if (clave.isEmpty()) {
            Toast.makeText(this, "El campo contraseña está vacío", Toast.LENGTH_SHORT).show();
            return;
        }

        ProgressDialog progressDialog = new ProgressDialog(LoginAdminActivity.this);
        progressDialog.setMessage("Iniciando sesión...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        Call<LoginResponse> call = administradorApi.loginAdmin(usuario, clave, origenApp);
        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                progressDialog.dismiss();

                if (response.isSuccessful() && response.body() != null) {
                    LoginResponse loginResponse = response.body();

                    if ("ACCESO_CONCEDIDO".equals(loginResponse.getMensaje())) {
                        Toast.makeText(LoginAdminActivity.this, "Inicio de sesión exitoso", Toast.LENGTH_SHORT).show();

                        SharedPreferences prefs = getSharedPreferences("admin_prefs", MODE_PRIVATE);
                        SharedPreferences.Editor editor = prefs.edit();
                        editor.putString("nombre_admin", loginResponse.getUsuario());
                        editor.putLong("id_admin", loginResponse.getId()); // ← ESTA LÍNEA
                        editor.apply();


                        Intent intent = new Intent(LoginAdminActivity.this, MainActivityAdmin.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(LoginAdminActivity.this, "Error: " + loginResponse.getMensaje(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(LoginAdminActivity.this, "Error en la respuesta del servidor", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(LoginAdminActivity.this, "Error de red: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}