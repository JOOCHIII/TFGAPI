package com.example.logins;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegistroActivity extends AppCompatActivity {

    EditText nombreapellidos, email, telefono, usuario, clave, confirmaclave;
    Button registrar;
    TextView ingresar;
    UsuarioApi usuarioApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registroactivity);

        nombreapellidos = findViewById(R.id.txtnomapellidos);
        email = findViewById(R.id.txtemail);
        telefono = findViewById(R.id.txttelefono);
        usuario = findViewById(R.id.txtusuario);
        clave = findViewById(R.id.txtclave);
        confirmaclave = findViewById(R.id.txtconfirmaclave);
        registrar = findViewById(R.id.btnregistrar);
        ingresar = findViewById(R.id.lbliniciarsesion);

        usuarioApi = RetrofitClient.getRetrofitInstance().create(UsuarioApi.class);

        registrar.setOnClickListener(view -> registrarUsuario());

        ingresar.setOnClickListener(view -> {
            Intent ingresar = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(ingresar);
            finish();
        });
    }

    private void registrarUsuario() {
        String nombreCompleto = nombreapellidos.getText().toString().trim();
        String correoInput = email.getText().toString().trim();
        String telefonoInput = telefono.getText().toString().trim();
        String usuarioInput = usuario.getText().toString().trim();
        String password = clave.getText().toString();
        String confirmPassword = confirmaclave.getText().toString();

        // Validaciones de campos vacíos
        if (nombreCompleto.isEmpty() || correoInput.isEmpty() || telefonoInput.isEmpty()
                || usuarioInput.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            showToast("Por favor, rellena todos los campos");
            return;
        }

        // Validaciones específicas
        if (usuarioInput.length() < 3) {
            showToast("El usuario debe tener al menos 3 caracteres");
            return;
        }

        if (!correoInput.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            showToast("El correo debe ser válido");
            return;
        }

        if (!telefonoInput.matches("\\d{9}")) {
            showToast("El teléfono debe tener 9 dígitos numéricos");
            return;
        }

        if (!password.equals(confirmPassword)) {
            showToast("Las contraseñas no coinciden");
            return;
        }

        // Puedes activar estas validaciones avanzadas si quieres reforzar aún más

        if (password.length() < 8 || !password.matches(".*[A-Z].*")
                || !password.matches(".*[a-z].*") || !password.matches(".*\\d.*")
                || !password.matches(".*[!@#$%^&*()_+=<>?/{}~\\-].*") || password.contains(" ")) {
            showToast("La contraseña debe tener al menos 8 caracteres, incluir mayúsculas, minúsculas, números y un carácter especial, y no contener espacios");
            return;
        }

        String origenApp = "tienda";

        Call<String> call = usuarioApi.registrarUsuario(
                nombreCompleto,
                correoInput,
                telefonoInput,
                usuarioInput,
                password,
                origenApp
        );

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    String mensaje = response.body();
                    Toast.makeText(RegistroActivity.this, mensaje, Toast.LENGTH_SHORT).show();
                    if ("Usuario registrado correctamente".equals(mensaje)) {
                        limpiarCampos();
                    }
                } else {
                    Toast.makeText(RegistroActivity.this, "Error en el servidor", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(RegistroActivity.this, "Error de conexión: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showToast(String mensaje) {
        runOnUiThread(() -> Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show());
    }

    private void limpiarCampos() {
        nombreapellidos.setText("");
        email.setText("");
        telefono.setText("");
        usuario.setText("");
        clave.setText("");
        confirmaclave.setText("");
    }
}
