package com.example.logins;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
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

    private List<Carrito> carrito;  // Lista de productos en carrito

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
        if (getArguments() != null) {
            idUsuario = getArguments().getLong(ARG_ID_USUARIO);
        }
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

        btnTramitarPedido.setOnClickListener(v -> {
            Toast.makeText(getContext(), "Funcionalidad de tramitar pedido pendiente", Toast.LENGTH_SHORT).show();
        });

        return view;
    }

    private void cargarCarritoDesdeApi() {
        CarritoApi apiService = RetrofitClient.getRetrofitInstance().create(CarritoApi.class);

        Call<List<Carrito>> call = apiService.getCarritoPorUsuario(idUsuario);

        call.enqueue(new Callback<List<Carrito>>() {
            @Override
            public void onResponse(Call<List<Carrito>> call, Response<List<Carrito>> response) {
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
            public void onFailure(Call<List<Carrito>> call, Throwable t) {
                Toast.makeText(getContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void actualizarTotal() {
        double total = 0;
        for (Carrito item : carrito) {
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
}
