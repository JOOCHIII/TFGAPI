package com.example.logins;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Fragment_profile_config extends Fragment {

    private EditText editTextNombre, editTextCorreo, editTextTelefono;
    private EditText editTextNuevaContrasena, editTextConfirmarContrasena;
    private Button btnGuardarCambios;

    private long idUsuario;
    private String nombreUsuario;

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

        SharedPreferences preferences = getActivity().getSharedPreferences("usuario_prefs", getActivity().MODE_PRIVATE);
        idUsuario = preferences.getLong("id_usuario", -1);
        nombreUsuario = preferences.getString("nombre_usuario", null);

        if (idUsuario == -1 || nombreUsuario == null) {
            Toast.makeText(getContext(), "Error al obtener usuario", Toast.LENGTH_SHORT).show();
            return view;
        }

        cargarDatosUsuario(nombreUsuario);

        btnGuardarCambios.setOnClickListener(v -> guardarCambios());

        return view;
    }

    private void cargarDatosUsuario(String nombreUsuario) {
        UsuarioApi usuarioApi = RetrofitClient.getRetrofitInstance().create(UsuarioApi.class);

        Call<Usuario> call = usuarioApi.getUsuarioPorNombre(nombreUsuario);

        call.enqueue(new Callback<Usuario>() {
            @Override
            public void onResponse(Call<Usuario> call, Response<Usuario> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Usuario usuario = response.body();
                    editTextNombre.setText(usuario.getUsuario());
                    editTextCorreo.setText(usuario.getCorreo());
                    editTextTelefono.setText(usuario.getTelefono());
                } else {
                    Toast.makeText(getContext(), "No se pudieron cargar los datos", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Usuario> call, Throwable t) {
                Log.e("API", "Error: " + t.getMessage());
                Toast.makeText(getContext(), "Error al conectar con el servidor", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void guardarCambios() {
        String nombre = editTextNombre.getText().toString().trim();
        String correo = editTextCorreo.getText().toString().trim();
        String telefono = editTextTelefono.getText().toString().trim();
        String nuevaContrasena = editTextNuevaContrasena.getText().toString().trim();
        String confirmarContrasena = editTextConfirmarContrasena.getText().toString().trim();

        if (TextUtils.isEmpty(nombre) || TextUtils.isEmpty(correo) || TextUtils.isEmpty(telefono)) {
            Toast.makeText(getContext(), "Por favor completa todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!TextUtils.isEmpty(nuevaContrasena) && !nuevaContrasena.equals(confirmarContrasena)) {
            Toast.makeText(getContext(), "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
            return;
        }

        UsuarioApi usuarioApi = RetrofitClient.getRetrofitInstance().create(UsuarioApi.class);

        Usuario usuarioActualizado = new Usuario();
        usuarioActualizado.setId((int) idUsuario);
        usuarioActualizado.setUsuario(nombre);
        usuarioActualizado.setCorreo(correo);
        usuarioActualizado.setTelefono(telefono);

        if (!TextUtils.isEmpty(nuevaContrasena)) {
            usuarioActualizado.setContrasena(nuevaContrasena);
        }

        Call<Void> call = usuarioApi.actualizarUsuario(idUsuario, usuarioActualizado);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getContext(), "Datos actualizados correctamente", Toast.LENGTH_SHORT).show();
                    editTextNuevaContrasena.setText("");
                    editTextConfirmarContrasena.setText("");

                    // Actualizar nombre en SharedPreferences si cambió
                    if (!nombre.equals(nombreUsuario)) {
                        SharedPreferences preferences = getActivity().getSharedPreferences("usuario_prefs", getActivity().MODE_PRIVATE);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putString("nombre_usuario", nombre);
                        editor.apply();
                        nombreUsuario = nombre;
                    }

                } else {
                    Toast.makeText(getContext(), "Error al actualizar los datos", Toast.LENGTH_SHORT).show();
                    Log.e("API_ERROR", "Código: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(getContext(), "Error en la conexión", Toast.LENGTH_SHORT).show();
                Log.e("API_ERROR", t.getMessage());
            }
        });
    }
}
