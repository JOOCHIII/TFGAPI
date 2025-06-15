package com.example.logins;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetallePedidoFragment extends Fragment {

    private static final String ARG_ID_PEDIDO = "idPedido";
    private Long idPedido;

    private TextView tvFecha, tvEstado, tvTotal;
    private RecyclerView recyclerView;
    private ProductoPedidoAdapter adapter;

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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detalle_pedido, container, false);

        tvFecha = view.findViewById(R.id.tvFechaDetalle);
        tvEstado = view.findViewById(R.id.tvEstadoDetalle);
        tvTotal = view.findViewById(R.id.tvTotalDetalle);
        recyclerView = view.findViewById(R.id.recyclerDetallePedido);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        obtenerDetallePedido();

        return view;
    }

    private void obtenerDetallePedido() {
        PedidoApi api = RetrofitClient.getRetrofitInstance().create(PedidoApi.class);
        api.obtenerDetallesDePedido(idPedido).enqueue(new Callback<DetallePedidoResponse>() {
            @Override
            public void onResponse(Call<DetallePedidoResponse> call, Response<DetallePedidoResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    DetallePedidoResponse detalle = response.body();
                    tvFecha.setText("Fecha: " + detalle.getFecha().substring(0, 16).replace("T", " "));
                    tvEstado.setText("Estado: " + detalle.getEstado());
                    tvTotal.setText(String.format("Total: %.2f €", detalle.getTotal()));

                    adapter = new ProductoPedidoAdapter(detalle.getDetalles(), getContext());
                    recyclerView.setAdapter(adapter);
                } else {
                    Toast.makeText(getContext(), "Error al cargar detalle", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<DetallePedidoResponse> call, Throwable t) {
                Toast.makeText(getContext(), "Error de red al obtener detalle", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
