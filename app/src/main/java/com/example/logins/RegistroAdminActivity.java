/*package com.example.logins;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegistroAdminActivity extends AppCompatActivity {

    EditText nombre, correo, telefono, usuario, clave, repetirClave;
    Button registrar;
    AdministradorApi administradorApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registro_admin); // Asegúrate de que este layout existe

        // Enlazar vistas con el layout
        nombre = findViewById(R.id.txtnombreadmin);
        correo = findViewById(R.id.txtcorreoadmin);
        telefono = findViewById(R.id.txttelefonoadmin);
        usuario = findViewById(R.id.txtusuarioadmin);
        clave = findViewById(R.id.txtclaveadmin);
        repetirClave = findViewById(R.id.txtrepetirclaveadmin);
        registrar = findViewById(R.id.btnregistraradmin);

        // Crear instancia de la API
        administradorApi = RetrofitClient.getRetrofitInstance().create(AdministradorApi.class);

        // Evento para el botón de registrar
        registrar.setOnClickListener(v -> registrarAdmin());
    }

    private void registrarAdmin() {
        String nombreStr = nombre.getText().toString().trim();
        String correoStr = correo.getText().toString().trim();
        String telefonoStr = telefono.getText().toString().trim();
        String usuarioStr = usuario.getText().toString().trim();
        String password = clave.getText().toString().trim();
        String confirmPassword = repetirClave.getText().toString().trim();

        // Validaciones
        if (nombreStr.isEmpty() || correoStr.isEmpty() || telefonoStr.isEmpty() ||
                usuarioStr.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            Toast.makeText(this, "Por favor, rellena todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!password.equals(confirmPassword)) {
            Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
            return;
        }

        // Llamada a la API
        Call<String> call = administradorApi.registrarAdmin(
                nombreStr,
                correoStr,
                telefonoStr,
                usuarioStr,
                password,
                "tienda" // origen_app
        );

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Toast.makeText(RegistroAdminActivity.this, response.body(), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(RegistroAdminActivity.this, "Error en el registro", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(RegistroAdminActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
*/