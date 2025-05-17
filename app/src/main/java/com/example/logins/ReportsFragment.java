package com.example.logins;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReportsFragment extends Fragment {

    private EditText edtAsunto, edtDescripcion;
    private Button btnEnviarReporte;

    private long idUsuario = -1; // valor inicial inválido

    private ReportesNotisApi reportesNotisApi;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_reports, container, false);

        edtAsunto = view.findViewById(R.id.edtAsunto);
        edtDescripcion = view.findViewById(R.id.edtDescripcion);
        btnEnviarReporte = view.findViewById(R.id.btnEnviarReporte);

        // Leer idUsuario desde SharedPreferences
        SharedPreferences preferences = requireActivity().getSharedPreferences("usuario_prefs", getActivity().MODE_PRIVATE);
        idUsuario = preferences.getLong("id_usuario", -1);

        if (idUsuario == -1) {
            Toast.makeText(getContext(), "Error: usuario no identificado", Toast.LENGTH_LONG).show();
            btnEnviarReporte.setEnabled(false);
        }

        reportesNotisApi = RetrofitClient.getReportesNotisApi();

        btnEnviarReporte.setOnClickListener(v -> enviarReporte());

        return view;
    }

    private void enviarReporte() {
        String asunto = edtAsunto.getText().toString().trim();
        String descripcion = edtDescripcion.getText().toString().trim();

        if (asunto.isEmpty()) {
            edtAsunto.setError("El asunto es obligatorio");
            edtAsunto.requestFocus();
            return;
        }

        if (descripcion.isEmpty()) {
            edtDescripcion.setError("La descripción es obligatoria");
            edtDescripcion.requestFocus();
            return;
        }

        ReporteDTO reporte = new ReporteDTO();
        reporte.setIdUsuario(idUsuario);
        reporte.setAsunto(asunto);
        reporte.setDescripcion(descripcion);

        Call<String> call = reportesNotisApi.crearReporte(reporte);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getContext(), "Reporte enviado correctamente", Toast.LENGTH_LONG).show();
                    limpiarFormulario();
                } else {
                    Toast.makeText(getContext(), "Error: " + response.message(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(getContext(), "Error de red: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void limpiarFormulario() {
        edtAsunto.setText("");
        edtDescripcion.setText("");
    }
}
