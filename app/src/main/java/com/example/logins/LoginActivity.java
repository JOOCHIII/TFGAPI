package com.example.logins;

import Connection.ConnectionDB;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class LoginActivity extends AppCompatActivity {
    EditText usuario, contrasena;
    TextView textviewRegistro, textviewAdmin;
    Button botoningresar;
    Connection con;

 /*   public LoginActivity() {
        ConnectionDB instanceConnection = new ConnectionDB();
        con = instanceConnection.connect();
        aaaa
    }*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        usuario = (EditText) findViewById(R.id.usuario);
        contrasena = (EditText) findViewById(R.id.contrasena);
        textviewRegistro = (TextView) findViewById(R.id.textviewRegistro);
        textviewAdmin = (TextView) findViewById(R.id.textviewAdmin);
        botoningresar = (Button) findViewById(R.id.botoniniciarsesion);

        // Crear un ExecutorService para manejar el hilo en segundo plano
        ExecutorService executorService = Executors.newSingleThreadExecutor();

        botoningresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                executorService.execute(new Runnable() {
                    @Override
                    public void run() {
                        login();
                    }
                });
            }
        });

        textviewRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent registro = new Intent(getApplicationContext(), RegistroActivity.class);
                startActivity(registro);
            }
        });

        textviewAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent admin = new Intent(getApplicationContext(), LoginAdminActivity.class);
                startActivity(admin);
            }
        });
    }

    // Método de login, ahora reemplazado sin AsyncTask
    private void login() {
        // Obtener valores introducidos por el usuario
        String usuarioInput = usuario.getText().toString().trim();
        String contrasenaInput = contrasena.getText().toString().trim();
        String origenApp = "tienda";  // Definir el origen de la app

        // Crear instancia de la API
        UsuarioApi usuarioApi = RetrofitClient.getRetrofitInstance().create(UsuarioApi.class);

        // Llamada a la API para hacer login
        Call<String> call = usuarioApi.login(usuarioInput, contrasenaInput, origenApp);

        // Ejecutar la llamada de forma asíncrona
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful() && response.body() != null) {
                    String resultado = response.body();

                    // Evaluar la respuesta del servidor
                    switch (resultado) {
                        case "ACCESO_CONCEDIDO":
                            Toast.makeText(LoginActivity.this, "Acceso exitoso", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            intent.putExtra("nombre_usuario", usuarioInput);
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
                            Toast.makeText(LoginActivity.this, "Respuesta desconocida: " + resultado, Toast.LENGTH_SHORT).show();
                            break;
                    }
                } else {
                    Toast.makeText(LoginActivity.this, "Error en la respuesta del servidor", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(LoginActivity.this, "Error de conexión: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    protected void onResume() {
        super.onResume();

        // Limpiar los campos al volver al login
        usuario.setText("");
        contrasena.setText("");
    }


}
