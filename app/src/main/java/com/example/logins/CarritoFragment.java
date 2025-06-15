package com.example.logins;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log; // <-- Importante
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CarritoFragment extends Fragment implements CarritoAdapter.OnCantidadChangeListener {

    private static final String ARG_ID_USUARIO = "id_usuario";

    private RecyclerView recyclerView;
    private TextView tvTotal;
    private Button btnTramitarPedido;

    private List<CarritoDTO> carrito;
    private CarritoAdapter adapter;

    private long idUsuario;

    public CarritoFragment() {
        // Constructor vacío requerido
    }

    public static CarritoFragment newInstance(long idUsuario) {
        CarritoFragment fragment = new CarritoFragment();
        Bundle args = new Bundle();
        args.putLong(ARG_ID_USUARIO, idUsuario);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Obtener ID de usuario desde argumentos o SharedPreferences como respaldo
        if (getArguments() != null) {
            idUsuario = getArguments().getLong(ARG_ID_USUARIO, -1);
        }
        if (idUsuario == -1) {
            SharedPreferences prefs = requireContext().getSharedPreferences("usuario_prefs", Context.MODE_PRIVATE);
            idUsuario = prefs.getLong("id_usuario", -1);
        }

        Log.d("CarritoFragment", "ID de usuario: " + idUsuario);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_carrito, container, false);

        recyclerView = view.findViewById(R.id.recyclerViewCarrito);
        tvTotal = view.findViewById(R.id.tvTotal);
        btnTramitarPedido = view.findViewById(R.id.btnTramitarPedido);

        carrito = new ArrayList<>();
        adapter = new CarritoAdapter(requireContext(), carrito, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerView.setAdapter(adapter);

        cargarCarritoDesdeApi();

        btnTramitarPedido.setOnClickListener(v -> tramitarPedido());

        return view;
    }

    private void cargarCarritoDesdeApi() {
        CarritoApi apiService = RetrofitClient.getRetrofitInstance().create(CarritoApi.class);
        Call<List<CarritoDTO>> call = apiService.obtenerCarritoUsuario(idUsuario);

        call.enqueue(new Callback<List<CarritoDTO>>() {
            @Override
            public void onResponse(Call<List<CarritoDTO>> call, Response<List<CarritoDTO>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    carrito.clear();
                    carrito.addAll(response.body());
                    adapter.notifyDataSetChanged();
                    actualizarTotal();
                } else {
                    Toast.makeText(getContext(), "Error al cargar el carrito", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<CarritoDTO>> call, Throwable t) {
                Toast.makeText(getContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void actualizarTotal() {
        double total = 0;
        for (CarritoDTO item : carrito) {
            if (item != null) {
                total += item.getSubtotal();
            }
        }
        tvTotal.setText(String.format("Total: %.2f €", total));
    }

    @Override
    public void onCantidadChange() {
        actualizarTotal();
    }

    private void tramitarPedido() {
        if (idUsuario == -1) {
            Toast.makeText(getContext(), "ID de usuario no válido", Toast.LENGTH_SHORT).show();
            return;
        }

        PedidoApi pedidoApi = RetrofitClient.getRetrofitInstance().create(PedidoApi.class);
        Call<Void> call = pedidoApi.tramitarPedido(idUsuario);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getContext(), "Pedido tramitado con éxito", Toast.LENGTH_SHORT).show();
                    carrito.clear();
                    adapter.notifyDataSetChanged();
                    actualizarTotal();
                } else {
                    Log.e("CarritoFragment", "Error al tramitar pedido. Código: " + response.code());
                    Log.e("CarritoFragment", "Mensaje: " + response.message());
                    Toast.makeText(getContext(), "Error al tramitar pedido. Código: " + response.code(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e("CarritoFragment", "Fallo en la petición: " + t.getMessage());
                Toast.makeText(getContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
