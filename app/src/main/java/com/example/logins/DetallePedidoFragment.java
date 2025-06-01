package com.example.logins;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetallePedidoFragment extends Fragment {

    private static final String ARG_ID_PEDIDO = "id_pedido";
    private Long idPedido;

    private TextView tvFecha, tvEstado, tvTotal;
    private RecyclerView recyclerProductos;
    private ProductoPedidoAdapter adapter;
    private List<ProductoPedidoDTO> productos = new ArrayList<>();

    public static DetallePedidoFragment newInstance(Long idPedido) {
        DetallePedidoFragment fragment = new DetallePedidoFragment();
        Bundle args = new Bundle();
        args.putLong(ARG_ID_PEDIDO, idPedido);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            idPedido = getArguments().getLong(ARG_ID_PEDIDO);
        }
    }

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.pedidos_fragment, container, false);

        tvFecha = view.findViewById(R.id.tvFecha);
        tvEstado = view.findViewById(R.id.tvEstado);
        tvTotal = view.findViewById(R.id.tvTotal);
        recyclerProductos = view.findViewById(R.id.recyclerProductosPedido);

        adapter = new ProductoPedidoAdapter(productos, getContext());
        recyclerProductos.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerProductos.setAdapter(adapter);

        cargarDetallePedido();

        return view;
    }

    private void cargarDetallePedido() {
        PedidoApi api = RetrofitClient.getRetrofitInstance().create(PedidoApi.class);
        Call<DetallePedidoResponse> call = api.obtenerDetallesDePedido(idPedido);

        call.enqueue(new Callback<DetallePedidoResponse>() {
            @Override
            public void onResponse(Call<DetallePedidoResponse> call, Response<DetallePedidoResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    DetallePedidoResponse detalle = response.body();
                    tvFecha.setText("Fecha: " + detalle.getFecha());
                    tvEstado.setText("Estado: " + detalle.getEstado());
                    tvTotal.setText("Total: " + detalle.getTotal() + " €");

                    productos.clear();

                    if (detalle.getProductos() != null) {
                        productos.addAll(detalle.getProductos());
                    } else {
                        Toast.makeText(getContext(), "Este pedido no contiene productos", Toast.LENGTH_SHORT).show();
                    }

                    adapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(getContext(), "No se pudo cargar el detalle del pedido", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<DetallePedidoResponse> call, Throwable t) {
                Toast.makeText(getContext(), "Error al cargar detalles", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
