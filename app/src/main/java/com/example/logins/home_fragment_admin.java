package com.example.logins;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class home_fragment_admin extends Fragment {

    private TextView tvTotalProducts, tvDraftProducts , btnManageOrders;
    private BarChart barChartVentas;

    public home_fragment_admin() {}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home_admin, container, false);

        // Inicializar TextViews
        tvTotalProducts = view.findViewById(R.id.tvTotalProducts);
        tvDraftProducts = view.findViewById(R.id.tvDraftProducts);
        barChartVentas = view.findViewById(R.id.barChartVentas);


        // Botón para ver todos los productos
        Button btnVerTodos = view.findViewById(R.id.btnViewAllProducts);
        btnVerTodos.setOnClickListener(v -> {
            Fragment fragment = new ProductosAdminFragment();
            FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.content_frame, fragment);
            transaction.addToBackStack(null);
            transaction.commit();
        });

        // Botón para añadir un producto
        Button btnAddNewProduct = view.findViewById(R.id.btnAddNewProduct);
        btnAddNewProduct.setOnClickListener(v -> {
            Fragment fragment = new CrearProductoAdminFragment();
            FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.content_frame, fragment);
            transaction.addToBackStack(null);
            transaction.commit();
        });

        Button btnManageOrders = view.findViewById(R.id.btnManageOrders);
        btnManageOrders.setOnClickListener(v -> {
            Fragment fragment = new GestionPedidosAdmin();
            FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.content_frame, fragment);
            transaction.addToBackStack(null);
            transaction.commit();
        });

        cargarContadores(); // Llamar a Retrofit para actualizar los contadores
        cargarGraficoVentasAnual();

        return view;
    }

    private void cargarContadores() {
        ProductosApi api = RetrofitClient.getRetrofitInstance().create(ProductosApi.class);

        // Total productos
        api.contarProductos().enqueue(new Callback<Long>() {
            @Override
            public void onResponse(@NonNull Call<Long> call, @NonNull Response<Long> response) {
                if (response.isSuccessful() && response.body() != null) {
                    tvTotalProducts.setText("Total Productos: " + response.body());
                }
            }

            @Override
            public void onFailure(@NonNull Call<Long> call, @NonNull Throwable t) {
                tvTotalProducts.setText("Total Productos: error");
            }
        });

        // Sin stock
        api.contarProductosSinStock().enqueue(new Callback<Long>() {
            @Override
            public void onResponse(@NonNull Call<Long> call, @NonNull Response<Long> response) {
                if (response.isSuccessful() && response.body() != null) {
                    tvDraftProducts.setText("Sin Stock: " + response.body());
                }
            }

            @Override
            public void onFailure(@NonNull Call<Long> call, @NonNull Throwable t) {
                tvDraftProducts.setText("Sin Stock: error");
            }
        });
    }
    private void cargarGraficoVentasAnual() {
        List<BarEntry> entries = new ArrayList<>();

        // Ventas de cada mes
        float[] ventasMeses = {50, 80, 30, 60, 90, 70, 40, 85, 55, 60, 75, 95};

        for (int i = 0; i < ventasMeses.length; i++) {
            entries.add(new BarEntry(i, ventasMeses[i]));
        }

        BarDataSet dataSet = new BarDataSet(entries, "Ventas por Mes");

        // Colores para cada barra (puedes añadir o cambiar colores)
        int[] colores = {
                getResources().getColor(android.R.color.holo_red_light),
                getResources().getColor(android.R.color.holo_orange_light),
                getResources().getColor(android.R.color.holo_orange_dark),
                getResources().getColor(android.R.color.holo_green_light),
                getResources().getColor(android.R.color.holo_green_dark),
                getResources().getColor(android.R.color.holo_blue_light),
                getResources().getColor(android.R.color.holo_blue_dark),
                getResources().getColor(android.R.color.holo_purple),
                getResources().getColor(android.R.color.holo_red_dark),
                getResources().getColor(android.R.color.holo_orange_light),
                getResources().getColor(android.R.color.holo_green_light),
                getResources().getColor(android.R.color.holo_blue_light)
        };

        List<Integer> listaColores = new ArrayList<>();
        for (int color : colores) {
            listaColores.add(color);
        }

        dataSet.setColors(listaColores);

        BarData data = new BarData(dataSet);
        data.setBarWidth(0.9f);

        barChartVentas.setData(data);
        barChartVentas.setFitBars(true);
        barChartVentas.getDescription().setEnabled(false);

        final String[] meses = new String[] {
                "Ene", "Feb", "Mar", "Abr", "May", "Jun",
                "Jul", "Ago", "Sep", "Oct", "Nov", "Dic"
        };

        barChartVentas.getXAxis().setValueFormatter(new IndexAxisValueFormatter(meses));
        barChartVentas.getXAxis().setGranularity(1f);
        barChartVentas.getXAxis().setGranularityEnabled(true);
        barChartVentas.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        barChartVentas.getAxisLeft().setAxisMinimum(0f);
        barChartVentas.getAxisRight().setEnabled(false);

        barChartVentas.invalidate();
    }




}
