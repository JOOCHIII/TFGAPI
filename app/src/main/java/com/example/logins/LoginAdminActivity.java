package com.example.logins;
import com.example.logins.MainActivityAdmin;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import Connection.ConnectionDB;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class LoginAdminActivity extends AppCompatActivity {

    EditText usuarioAdmin, contrasenaAdmin;
    MaterialButton botonIniciarSesionAdmin;
    TextView iniciarComoUsuario;
    Connection con;

    public LoginAdminActivity() {
        ConnectionDB instanceConnection = new ConnectionDB();
        con = instanceConnection.connect();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_admin);

        usuarioAdmin = findViewById(R.id.usuario_admin);
        contrasenaAdmin = findViewById(R.id.contrasena_admin);
        botonIniciarSesionAdmin = findViewById(R.id.boton_iniciar_sesion_admin);
        iniciarComoUsuario = findViewById(R.id.textviewIniciousuario);

        botonIniciarSesionAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iniciarSesionAdmin();
            }
        });

        iniciarComoUsuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginAdminActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    public void iniciarSesionAdmin() {
        try {
            String usuario = usuarioAdmin.getText().toString().trim();
            String clave = contrasenaAdmin.getText().toString().trim();

            if (usuario.isEmpty() || clave.isEmpty()) {
                Toast.makeText(this, "Rellena todos los campos", Toast.LENGTH_SHORT).show();
                return;
            }

            if (con == null) {
                Toast.makeText(this, "Error de conexión", Toast.LENGTH_SHORT).show();
                return;
            }

            // Verificar si el usuario existe
            PreparedStatement pst = con.prepareStatement("SELECT * FROM Administradores WHERE usuarioAdmin = ?");
            pst.setString(1, usuario);
            ResultSet rs = pst.executeQuery();

            if (!rs.next()) {
                Toast.makeText(this, "El usuario no existe", Toast.LENGTH_SHORT).show();
                return;
            }

            // Verificar contraseña
            String contrasenaCorrecta = rs.getString("contrasenaAdmin");
            if (!clave.equals(contrasenaCorrecta)) {
                Toast.makeText(this, "Contraseña incorrecta", Toast.LENGTH_SHORT).show();
                return;
            }

            Toast.makeText(this, "Inicio de sesión exitoso como administrador", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getApplicationContext(), MainActivityAdmin.class);
            intent.putExtra("nombre_Admin", usuario);
            startActivity(intent);

            finish();

        } catch (Exception e) {
            Log.e("LOGIN_ADMIN_ERROR", e.getMessage());
        }
    }
}