package com.example.logins;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import Connection.ConnectionDB;

public class Fragment_profile_admin extends Fragment {

    private TextView textViewNombreAdmin, textViewCorreoAdmin, textViewTelefonoAdmin;
    private EditText editTextNuevaContrasena, editTextConfirmarContrasena;
    private Button btnCambiarContrasena;
    private Connection con;
    private String usuarioAdmin;

    public Fragment_profile_admin() {
        // Constructor vacío
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile_admin, container, false);

        // Inicializar vistas
        textViewNombreAdmin = view.findViewById(R.id.textViewNombreAdmin);
        textViewCorreoAdmin = view.findViewById(R.id.textViewCorreoAdmin);
        textViewTelefonoAdmin = view.findViewById(R.id.textViewTelefonoAdmin);


        // Obtener nombre de usuario admin desde el Intent
        usuarioAdmin = getActivity().getIntent().getStringExtra("nombre_Admin");

        if (usuarioAdmin != null) {
            obtenerDatosAdmin();
        }

        // Manejar la acción de cambiar la contraseña

        return view;
    }

    private void obtenerDatosAdmin() {
        // Conectar a la base de datos
        ConnectionDB instanceConnection = new ConnectionDB();
        con = instanceConnection.connect();

        if (con != null) {
            try {
                // Consultar los datos del administrador
                String query = "SELECT * FROM Administradores WHERE usuarioAdmin = ?";
                PreparedStatement pst = con.prepareStatement(query);
                pst.setString(1, usuarioAdmin);
                ResultSet rs = pst.executeQuery();

                if (rs.next()) {
                    // Mostrar datos del administrador
                    textViewNombreAdmin.setText(rs.getString("nombrecompletoAdmin"));
                    textViewCorreoAdmin.setText(rs.getString("correoAdmin"));
                    textViewTelefonoAdmin.setText(rs.getString("telefonoAdmin"));
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }


}
