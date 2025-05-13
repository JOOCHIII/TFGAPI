package com.example.logins;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import Connection.ConnectionDB;

public class Fragment_profile_config extends Fragment {

    private EditText editTextNombre, editTextCorreo, editTextTelefono;
    private EditText editTextNuevaContrasena, editTextConfirmarContrasena;
    private Button btnGuardarCambios;

    private Connection con;
    private String usuarioOriginal;

    public Fragment_profile_config() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile_config, container, false);

        editTextNombre = view.findViewById(R.id.editTextNombre);
        editTextCorreo = view.findViewById(R.id.editTextCorreo);
        editTextTelefono = view.findViewById(R.id.editTextTelefono);
        editTextNuevaContrasena = view.findViewById(R.id.editTextNuevaContrasena);
        editTextConfirmarContrasena = view.findViewById(R.id.editTextConfirmarContrasena);
        btnGuardarCambios = view.findViewById(R.id.btnGuardarCambios);

        String nombreUsuario = getActivity().getIntent().getStringExtra("nombre_usuario");

        if (nombreUsuario != null) {
            usuarioOriginal = nombreUsuario;
            obtenerDatosUsuario();
        }

        btnGuardarCambios.setOnClickListener(v -> guardarCambios());

        return view;
    }

    private void obtenerDatosUsuario() {
        con = new ConnectionDB().connect();

        if (con != null) {
            try {
                String query = "SELECT * FROM usuarios WHERE usuario = ?";
                PreparedStatement pst = con.prepareStatement(query);
                pst.setString(1, usuarioOriginal);
                ResultSet rs = pst.executeQuery();

                if (rs.next()) {
                    editTextNombre.setText(rs.getString("usuario"));
                    editTextCorreo.setText(rs.getString("correo"));
                    editTextTelefono.setText(rs.getString("telefono"));
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private void guardarCambios() {
        String nombre = editTextNombre.getText().toString().trim();
        String correo = editTextCorreo.getText().toString().trim();
        String telefono = editTextTelefono.getText().toString().trim();
        String nuevaContrasena = editTextNuevaContrasena.getText().toString().trim();
        String confirmarContrasena = editTextConfirmarContrasena.getText().toString().trim();

        if (TextUtils.isEmpty(nombre) || TextUtils.isEmpty(correo) || TextUtils.isEmpty(telefono)) {
            Toast.makeText(getActivity(), "Por favor completa todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!TextUtils.isEmpty(nuevaContrasena) && !nuevaContrasena.equals(confirmarContrasena)) {
            Toast.makeText(getActivity(), "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
            return;
        }

        con = new ConnectionDB().connect();

        if (con != null) {
            try {
                // Verifica duplicados
                String checkQuery = "SELECT COUNT(*) FROM usuarios WHERE (correo = ? OR telefono = ? OR usuario = ?) AND usuario != ?";
                PreparedStatement checkPst = con.prepareStatement(checkQuery);
                checkPst.setString(1, correo);
                checkPst.setString(2, telefono);
                checkPst.setString(3, usuarioOriginal); // No permitimos cambiar el nombre de usuario
                checkPst.setString(4, usuarioOriginal);
                ResultSet checkRs = checkPst.executeQuery();

                if (checkRs.next() && checkRs.getInt(1) > 0) {
                    Toast.makeText(getActivity(), "Ya existe un usuario con ese correo o teléfono", Toast.LENGTH_SHORT).show();
                    return;
                }

                String query;
                PreparedStatement pst;

                if (!TextUtils.isEmpty(nuevaContrasena)) {
                    query = "UPDATE usuarios SET usuario=?, correo=?, telefono=?, contrasena=? WHERE usuario=?";
                    pst = con.prepareStatement(query);
                    pst.setString(1, nombre);
                    pst.setString(2, correo);
                    pst.setString(3, telefono);
                    pst.setString(4, nuevaContrasena);
                    pst.setString(5, usuarioOriginal);
                } else {
                    query = "UPDATE usuarios SET usuario=?, correo=?, telefono=? WHERE usuario=?";
                    pst = con.prepareStatement(query);
                    pst.setString(1, nombre);
                    pst.setString(2, correo);
                    pst.setString(3, telefono);
                    pst.setString(4, usuarioOriginal);
                }

                int result = pst.executeUpdate();

                if (result > 0) {
                    editTextNuevaContrasena.setText("");
                    editTextConfirmarContrasena.setText("");
                    Toast.makeText(getActivity(), "Datos actualizados correctamente", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), "Error al actualizar", Toast.LENGTH_SHORT).show();
                }

            } catch (SQLException e) {
                e.printStackTrace();
                Toast.makeText(getActivity(), "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }
}
