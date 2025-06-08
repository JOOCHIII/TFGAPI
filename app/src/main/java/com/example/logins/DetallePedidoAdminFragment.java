package com.example.logins;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetallePedidoAdminFragment extends Fragment {
    private static final String ARG_ID_PEDIDO = "idPedido";

    private long idPedido;
    private TextView tvFechaDetalle, tvEstadoDetalle, tvTotalDetalle;
    private RecyclerView recyclerDetallePedido;
    private DetallePedidoAdapter adapter;

    public static DetallePedidoAdminFragment newInstance(long idPedido) {
        DetallePedidoAdminFragment fragment = new DetallePedidoAdminFragment();
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
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detalle_pedido_admin, container, false);

        tvFechaDetalle = view.findViewById(R.id.tvFechaDetalle);
        tvEstadoDetalle = view.findViewById(R.id.tvEstadoDetalle);
        tvTotalDetalle = view.findViewById(R.id.tvTotalDetalle);
        recyclerDetallePedido = view.findViewById(R.id.recyclerDetallePedido);
        recyclerDetallePedido.setLayoutManager(new LinearLayoutManager(getContext()));

        cargarDetalles();

        return view;
    }

    private void cargarDetalles() {
        PedidoApi api = RetrofitClient.getRetrofitInstance().create(PedidoApi.class);
        Call<DetallePedidoResponse> call = api.obtenerDetallesDePedido(idPedido);
        call.enqueue(new Callback<DetallePedidoResponse>() {
            @Override
            public void onResponse(Call<DetallePedidoResponse> call, Response<DetallePedidoResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    DetallePedidoResponse detalle = response.body();
                    tvFechaDetalle.setText("Fecha: " + detalle.getFecha());
                    tvEstadoDetalle.setText("Estado: " + detalle.getEstado());
                    tvTotalDetalle.setText("Total: " + detalle.getTotal() + " €");

                    adapter = new DetallePedidoAdapter(detalle.getDetalles());
                    recyclerDetallePedido.setAdapter(adapter);
                } else {
                    Toast.makeText(getContext(), "Error al cargar detalles", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<DetallePedidoResponse> call, Throwable t) {
                Toast.makeText(getContext(), "Fallo: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
