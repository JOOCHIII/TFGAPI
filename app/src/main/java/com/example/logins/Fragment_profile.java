package com.example.logins;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import androidx.fragment.app.Fragment;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import Connection.ConnectionDB;

public class Fragment_profile extends Fragment {

    TextView textNombre, textCorreo, textTelefono;
    EditText nuevaContrasena, confirmarContrasena;
    Button btnCambiar;
    Connection con;
    String nombreUsuario;

    public Fragment_profile() {
        ConnectionDB instanceConnection = new ConnectionDB();
        con = instanceConnection.connect();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        textNombre = view.findViewById(R.id.textViewNombreUsuario);
        textCorreo = view.findViewById(R.id.textViewCorreoUsuario);
        textTelefono = view.findViewById(R.id.textViewTelefonoUsuario);


        nombreUsuario = getActivity().getIntent().getStringExtra("nombre_usuario");

        mostrarDatos();


        return view;
    }

    private void mostrarDatos() {
        try {
            String query = "SELECT nombrecompleto, correo, telefono FROM usuarios WHERE usuario = ?";
            PreparedStatement pst = con.prepareStatement(query);
            pst.setString(1, nombreUsuario);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                textNombre.setText(rs.getString("nombrecompleto"));
                textCorreo.setText(rs.getString("correo"));
                textTelefono.setText(rs.getString("telefono"));
            }

        } catch (Exception e) {
            Toast.makeText(getContext(), "Error al cargar datos: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }


}
