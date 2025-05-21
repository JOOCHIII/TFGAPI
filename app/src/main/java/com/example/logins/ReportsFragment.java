package com.example.logins;

import static java.security.AccessController.getContext;

import android.content.Context;
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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReportsFragment extends Fragment {

    private EditText edtAsunto, edtDescripcion;
    private Button btnEnviarReporte;
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView rvReportes;

    private long idUsuario = -1;
    private ReportesNotisApi reportesNotisApi;
    private ReporteAdapter reporteAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_reports, container, false);

        edtAsunto = view.findViewById(R.id.edtAsunto);
        edtDescripcion = view.findViewById(R.id.edtDescripcion);
        btnEnviarReporte = view.findViewById(R.id.btnEnviarReporte);
        swipeRefreshLayout = view.findViewById(R.id.swipeRefresh);
        rvReportes = view.findViewById(R.id.rvReportes);

        // Obtener idUsuario desde SharedPreferences
        SharedPreferences preferences = requireActivity().getSharedPreferences("usuario_prefs", Context.MODE_PRIVATE);
        idUsuario = preferences.getLong("id_usuario", -1);

        if (idUsuario == -1) {
            Toast.makeText(getContext(), "Error: usuario no identificado", Toast.LENGTH_LONG).show();
            btnEnviarReporte.setEnabled(false);
        }

        reportesNotisApi = RetrofitClient.getReportesNotisApi();

        rvReportes.setLayoutManager(new LinearLayoutManager(getContext()));
        reporteAdapter = new ReporteAdapter(new ArrayList<>());
        rvReportes.setAdapter(reporteAdapter);

        btnEnviarReporte.setOnClickListener(v -> enviarReporte());

        swipeRefreshLayout.setOnRefreshListener(this::cargarReportes);

        cargarReportes();

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
                    cargarReportes();
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

    private void cargarReportes() {
        swipeRefreshLayout.setRefreshing(true);

        Call<List<ReporteDTO>> call = reportesNotisApi.obtenerReportesPorUsuario(idUsuario);
        call.enqueue(new Callback<List<ReporteDTO>>() {
            @Override
            public void onResponse(Call<List<ReporteDTO>> call, Response<List<ReporteDTO>> response) {
                swipeRefreshLayout.setRefreshing(false);
                if (response.isSuccessful() && response.body() != null) {
                    reporteAdapter.actualizarLista(response.body());
                } else {
                    Toast.makeText(getContext(), "Error al cargar reportes", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<ReporteDTO>> call, Throwable t) {
                swipeRefreshLayout.setRefreshing(false);
                Toast.makeText(getContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
