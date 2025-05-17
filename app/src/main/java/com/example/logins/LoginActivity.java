package com.example.logins;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class LoginActivity extends AppCompatActivity {
    EditText usuario, contrasena;
    TextView textviewRegistro, textviewAdmin;
    Button botoningresar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        usuario = findViewById(R.id.usuario);
        contrasena = findViewById(R.id.contrasena);
        textviewRegistro = findViewById(R.id.textviewRegistro);
        textviewAdmin = findViewById(R.id.textviewAdmin);
        botoningresar = findViewById(R.id.botoniniciarsesion);

        ExecutorService executorService = Executors.newSingleThreadExecutor();

        botoningresar.setOnClickListener(v -> executorService.execute(this::login));

        textviewRegistro.setOnClickListener(v -> {
            Intent registro = new Intent(getApplicationContext(), RegistroActivity.class);
            startActivity(registro);
        });

        textviewAdmin.setOnClickListener(v -> {
            Intent admin = new Intent(getApplicationContext(), LoginAdminActivity.class);
            startActivity(admin);
        });
    }

    private void login() {
        String usuarioInput = usuario.getText().toString().trim();
        String contrasenaInput = contrasena.getText().toString().trim();
        String origenApp = "tienda";

        if (usuarioInput.isEmpty()) {
            runOnUiThread(() -> Toast.makeText(this, "El campo usuario está vacío", Toast.LENGTH_SHORT).show());
            return;
        }

        if (contrasenaInput.isEmpty()) {
            runOnUiThread(() -> Toast.makeText(this, "El campo contraseña está vacío", Toast.LENGTH_SHORT).show());
            return;
        }

        runOnUiThread(() -> {
            ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this);
            progressDialog.setMessage("Iniciando sesión...");
            progressDialog.setCancelable(false);
            progressDialog.show();

            UsuarioApi usuarioApi = RetrofitClient.getRetrofitInstance().create(UsuarioApi.class);
            Call<LoginResponse> call = usuarioApi.login(usuarioInput, contrasenaInput, origenApp);

            call.enqueue(new Callback<LoginResponse>() {
                @Override
                public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                    progressDialog.dismiss();

                    if (response.isSuccessful() && response.body() != null) {
                        LoginResponse loginResponse = response.body();
                        // AÑADE ESTOS LOGS PARA VER QUÉ LLEGA
                        Log.d("LOGIN_DEBUG", "ID recibido: " + loginResponse.getId());
                        Log.d("LOGIN_DEBUG", "Usuario recibido: " + loginResponse.getUsuario());
                        Log.d("LOGIN_DEBUG", "Mensaje recibido: " + loginResponse.getMensaje());


                        switch (loginResponse.getMensaje()) {
                            case "ACCESO_CONCEDIDO":
                                Toast.makeText(LoginActivity.this, "Acceso exitoso", Toast.LENGTH_SHORT).show();

                                // Guardar en SharedPreferences
                                SharedPreferences preferences = getSharedPreferences("usuario_prefs", MODE_PRIVATE);
                                SharedPreferences.Editor editor = preferences.edit();
                                editor.putLong("id_usuario", loginResponse.getId());
                                editor.putString("nombre_usuario", loginResponse.getUsuario());
                                editor.apply();

                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(intent);
                                break;

                            case "CONTRASENA_INCORRECTA":
                                Toast.makeText(LoginActivity.this, "Contraseña incorrecta", Toast.LENGTH_SHORT).show();
                                break;

                            case "USUARIO_NO_EXISTE":
                                Toast.makeText(LoginActivity.this, "El usuario no existe", Toast.LENGTH_SHORT).show();
                                break;

                            case "ACCESO_DENEGADO_ORIGEN_APP":
                                Toast.makeText(LoginActivity.this, "Acceso denegado desde esta app", Toast.LENGTH_SHORT).show();
                                break;

                            default:
                                Toast.makeText(LoginActivity.this, "Error desconocido", Toast.LENGTH_SHORT).show();
                                break;
                        }
                    } else {
                        Toast.makeText(LoginActivity.this, "Error en la respuesta del servidor", Toast.LENGTH_SHORT).show();
                    }
                }


                @Override
                public void onFailure(Call<LoginResponse> call, Throwable t) {
                    progressDialog.dismiss();
                    Toast.makeText(LoginActivity.this, "Error de conexión: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
        usuario.setText("");
        contrasena.setText("");
    }
}
