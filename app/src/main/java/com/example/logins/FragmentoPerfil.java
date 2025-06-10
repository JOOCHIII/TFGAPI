package com.example.logins;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.logins.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentoPerfil extends Fragment {

    EditText etNombre, etTelefono, etCorreo, etNuevaContrasena, etRepetirContrasena;
    Button btnCambiarContrasena;

    public FragmentoPerfil() {
        // Constructor vacío
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        etNombre = view.findViewById(R.id.et_nombre_usuario);
        etTelefono = view.findViewById(R.id.et_telefono_usuario);
        etCorreo = view.findViewById(R.id.et_correo_usuario);
        etNuevaContrasena = view.findViewById(R.id.et_nueva_contrasena_usuario);
        etRepetirContrasena = view.findViewById(R.id.et_repetir_contrasena_usuario);
        btnCambiarContrasena = view.findViewById(R.id.btn_cambiar_contrasena_usuario);


        cargarDatosUsuario();

        btnCambiarContrasena.setOnClickListener(v -> {
            Toast.makeText(getContext(), "Funcionalidad cambiar contraseña pendiente", Toast.LENGTH_SHORT).show();
        });

        return view;
    }

    private void cargarDatosUsuario() {
        SharedPreferences prefs = getActivity().getSharedPreferences("usuario_prefs", getActivity().MODE_PRIVATE);
        String usuario = prefs.getString("nombre_usuario", null);

        if (usuario == null) {
            Toast.makeText(getContext(), "Usuario no encontrado en preferencias", Toast.LENGTH_SHORT).show();
            return;
        }

        UsuarioApi usuarioApi = RetrofitClient.getRetrofitInstance().create(UsuarioApi.class);

        Call<Usuario> call = usuarioApi.getUsuarioPorNombre(usuario);

        call.enqueue(new Callback<Usuario>() {
            @Override
            public void onResponse(Call<Usuario> call, Response<Usuario> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Usuario usuarioDatos = response.body();

                    etNombre.setText(usuarioDatos.getNombreCompleto());
                    etTelefono.setText(usuarioDatos.getTelefono());
                    etCorreo.setText(usuarioDatos.getCorreo());

                } else {
                    Toast.makeText(getContext(), "Error cargando datos del usuario", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Usuario> call, Throwable t) {
                Log.e("API_ERROR", t.getMessage());
                Toast.makeText(getContext(), "Error conectando con el servidor", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
