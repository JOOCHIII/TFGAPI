package com.example.logins;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationsFragment extends Fragment {

    private RecyclerView recyclerView;
    private NotificacionesAdapter adapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ReportesNotisApi apiService;
    private long idUsuario;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_notifications, container, false);

        recyclerView = view.findViewById(R.id.recyclerNotificaciones);
        swipeRefreshLayout = view.findViewById(R.id.swipeRefresh);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new NotificacionesAdapter(new ArrayList<>());
        recyclerView.setAdapter(adapter);

        SharedPreferences preferences = requireActivity().getSharedPreferences("usuario_prefs", Context.MODE_PRIVATE);
        idUsuario = preferences.getLong("id_usuario", 0);

        apiService = RetrofitClient.getRetrofitInstance().create(ReportesNotisApi.class);

        cargarNotificaciones();

        swipeRefreshLayout.setOnRefreshListener(this::cargarNotificaciones);

        return view;
    }

    private void cargarNotificaciones() {
        swipeRefreshLayout.setRefreshing(true);

        apiService.getNotificacionesUsuario(idUsuario, "tienda").enqueue(new Callback<List<Notificacion>>() {
            @Override
            public void onResponse(Call<List<Notificacion>> call, Response<List<Notificacion>> response) {
                swipeRefreshLayout.setRefreshing(false);
                if (response.isSuccessful() && response.body() != null) {
                    List<Notificacion> notificaciones = response.body();
                    adapter.setNotificaciones(notificaciones);

                    for (Notificacion noti : notificaciones) {
                        marcarNotificacionLeida(noti.getId());
                    }
                } else {
                    Toast.makeText(getContext(), "Error al cargar notificaciones", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Notificacion>> call, Throwable t) {
                swipeRefreshLayout.setRefreshing(false);
                Toast.makeText(getContext(), "Error de conexión", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void marcarNotificacionLeida(int idNotificacion) {
        apiService.marcarNotificacionLeida(idNotificacion).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    Log.d("NOTI", "Notificación " + idNotificacion + " marcada como leída");
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.e("NOTI", "Error marcando notificación", t);
            }
        });
    }
}
