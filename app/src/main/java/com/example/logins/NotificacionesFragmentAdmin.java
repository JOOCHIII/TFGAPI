package com.example.logins;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

public class NotificacionesFragmentAdmin extends Fragment {

    private RecyclerView recyclerView;
    private NotificacionesAdminAdapter adapter;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notificaciones_admin, container, false);

        recyclerView = view.findViewById(R.id.recyclerNotificaciones);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new NotificacionesAdminAdapter(new ArrayList<>());
        recyclerView.setAdapter(adapter);

        swipeRefreshLayout = view.findViewById(R.id.swipeRefresh);
        swipeRefreshLayout.setOnRefreshListener(this::cargarNotificaciones);

        cargarNotificaciones();

        return view;
    }

    private void cargarNotificaciones() {
        swipeRefreshLayout.setRefreshing(true);

        NotificacionAdminApi apiService = RetrofitClient.getRetrofitInstance().create(NotificacionAdminApi.class);
        Call<List<NotificacionPedidoAdmin>> call = apiService.getNotificacionesAdmin();

        call.enqueue(new Callback<List<NotificacionPedidoAdmin>>() {
            @Override
            public void onResponse(Call<List<NotificacionPedidoAdmin>> call, Response<List<NotificacionPedidoAdmin>> response) {
                swipeRefreshLayout.setRefreshing(false);

                if (response.isSuccessful()) {
                    List<NotificacionPedidoAdmin> lista = response.body();

                    // FILTRAR solo las no leídas
                    List<NotificacionPedidoAdmin> noLeidas = new ArrayList<>();
                    for (NotificacionPedidoAdmin noti : lista) {
                        if (!noti.isLeido()) {
                            noLeidas.add(noti);
                        }
                    }

                    adapter.setNotificaciones(noLeidas);

                    // Marcar como leídas (opcional)
                    for (NotificacionPedidoAdmin noti : noLeidas) {
                        apiService.marcarNotificacionAdminLeida(noti.getId()).enqueue(new Callback<Void>() {
                            @Override
                            public void onResponse(Call<Void> call, Response<Void> response) {
                                // OK
                            }

                            @Override
                            public void onFailure(Call<Void> call, Throwable t) {
                                Toast.makeText(getContext(), "Error al marcar como leída", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                } else {
                    Toast.makeText(getContext(), "Error al cargar notificaciones", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<NotificacionPedidoAdmin>> call, Throwable t) {
                swipeRefreshLayout.setRefreshing(false);
                Toast.makeText(getContext(), "Error de red", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
