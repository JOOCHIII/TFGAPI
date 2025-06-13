package com.example.logins;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
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

public class MisPedidosFragment extends Fragment {

    private RecyclerView recyclerPedidos;
    private SwipeRefreshLayout swipeRefreshLayout;
    private PedidoAdapter adapter;
    private List<Pedido> listaPedidos = new ArrayList<>();
    private Long idUsuario;

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.pedidos_fragment, container, false);

        recyclerPedidos = view.findViewById(R.id.recyclerPedidos);
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);

        // Obtener idUsuario desde SharedPreferences
        SharedPreferences userPrefs = requireContext().getSharedPreferences("usuario_prefs", Context.MODE_PRIVATE);
        idUsuario = userPrefs.getLong("id_usuario", -1);
        if (idUsuario == -1) {
            Toast.makeText(getContext(), "Usuario no identificado", Toast.LENGTH_SHORT).show();
            // Aquí puedes decidir si cierras el fragment o navegas a login
        }

        // Crear adapter con listener para clicks en pedido
        adapter = new PedidoAdapter(listaPedidos, getContext(), pedido -> {
            // Navegar al detalle del pedido
            DetallePedidoFragment detalleFragment = DetallePedidoFragment.newInstance(pedido.getId());
            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.content_frame, detalleFragment)
                    .addToBackStack(null) // <-- Esto es lo que permite volver atrás
                    .commit();

        });

        recyclerPedidos.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerPedidos.setAdapter(adapter);

        cargarPedidos();

        swipeRefreshLayout.setOnRefreshListener(this::cargarPedidos);

        return view;
    }

    private void cargarPedidos() {
        if (idUsuario == -1) return; // Evitar llamadas si no hay usuario

        PedidoApi api = RetrofitClient.getRetrofitInstance().create(PedidoApi.class);
        Call<List<Pedido>> call = api.obtenerPedidosPorUsuario(idUsuario);

        call.enqueue(new Callback<List<Pedido>>() {
            @Override
            public void onResponse(Call<List<Pedido>> call, Response<List<Pedido>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    listaPedidos.clear();
                    listaPedidos.addAll(response.body());
                    adapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(getContext(), "Error al obtener pedidos", Toast.LENGTH_SHORT).show();
                }
                swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<List<Pedido>> call, Throwable t) {
                swipeRefreshLayout.setRefreshing(false);
                Toast.makeText(getContext(), "Error al cargar pedidos", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
