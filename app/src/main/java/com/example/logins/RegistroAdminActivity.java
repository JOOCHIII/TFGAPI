package com.example.logins;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegistroAdminActivity extends Fragment {

    EditText nombre, correo, telefono, usuario, clave, repetirClave;
    Button registrar;
    AdministradorApi administradorApi;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.registro_admin, container, false);

        nombre = view.findViewById(R.id.txtnombreadmin);
        correo = view.findViewById(R.id.txtcorreoadmin);
        telefono = view.findViewById(R.id.txttelefonoadmin);
        usuario = view.findViewById(R.id.txtusuarioadmin);
        clave = view.findViewById(R.id.txtclaveadmin);
        repetirClave = view.findViewById(R.id.txtrepetirclaveadmin);
        registrar = view.findViewById(R.id.btnregistraradmin);

        administradorApi = RetrofitClient.getRetrofitInstance().create(AdministradorApi.class);

        registrar.setOnClickListener(v -> registrarAdmin());

        return view;
    }

    private void registrarAdmin() {
        String nombreStr = nombre.getText().toString().trim();
        String correoStr = correo.getText().toString().trim();
        String telefonoStr = telefono.getText().toString().trim();
        String usuarioStr = usuario.getText().toString().trim();
        String password = clave.getText().toString().trim();
        String confirmPassword = repetirClave.getText().toString().trim();

        if (nombreStr.isEmpty() || correoStr.isEmpty() || telefonoStr.isEmpty() ||
                usuarioStr.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            Toast.makeText(getActivity(), "Por favor, rellena todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!password.equals(confirmPassword)) {
            Toast.makeText(getActivity(), "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
            return;
        }

        Call<String> call = administradorApi.registrarAdmin(
                nombreStr,
                correoStr,
                telefonoStr,
                usuarioStr,
                password,
                "tienda"
        );

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Toast.makeText(getActivity(), response.body(), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), "Error en el registro", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(getActivity(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                // Solo hacer popBackStack para volver al fragmento anterior
                requireActivity().getSupportFragmentManager().popBackStack();
            }
        });

    }
}
