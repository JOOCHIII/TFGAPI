package com.example.logins;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GestionPedidosAdmin extends Fragment {

    RecyclerView recyclerView;
    PedidoAdminAdapter adapter;
    AdministradorApi administradorApi;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gestion_pedidos_admin, container, false);

        recyclerView = view.findViewById(R.id.recyclerPedidosAdmin);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        administradorApi = RetrofitClient.getRetrofitInstance().create(AdministradorApi.class);
        cargarPedidos();

        return view;
    }

    private void cargarPedidos() {
        Call<List<PedidoAdmin>> call = administradorApi.getPedidosAdmin();
        call.enqueue(new Callback<List<PedidoAdmin>>() {
            @Override
            public void onResponse(Call<List<PedidoAdmin>> call, Response<List<PedidoAdmin>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<PedidoAdmin> pedidos = response.body();
                    Log.d("Pedidos", "Cantidad: " + pedidos.size());
                    adapter = new PedidoAdminAdapter(pedidos, new PedidoAdminAdapter.OnItemClickListener() {
                        @Override
                        public void onEstadoChange(PedidoAdmin pedido, String nuevoEstado) {
                            actualizarEstadoPedido(pedido, nuevoEstado);
                        }

                        @Override
                        public void onVerDetalles(PedidoAdmin pedido) {
                            Toast.makeText(getContext(), "Pedido #" + pedido.getId() + "\n" +
                                    "Usuario: " + pedido.getId_usuario() + "\n" +
                                    "Total: $" + pedido.getTotal(), Toast.LENGTH_LONG).show();
                        }
                    });
                    recyclerView.setAdapter(adapter);
                } else {
                    Toast.makeText(getContext(), "Error en la respuesta", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<PedidoAdmin>> call, Throwable t) {
                Log.e("Pedidos", "Fallo: " + t.getMessage());
                Toast.makeText(getContext(), "Error al cargar pedidos", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void actualizarEstadoPedido(PedidoAdmin pedido, String nuevoEstado) {
        Call<Void> call = administradorApi.actualizarEstadoPedidoAdmin((int) pedido.getId(), nuevoEstado);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getContext(), "Estado actualizado", Toast.LENGTH_SHORT).show();
                    pedido.setEstado(nuevoEstado);
                    adapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(getContext(), "Error al actualizar", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(getContext(), "Fallo: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
