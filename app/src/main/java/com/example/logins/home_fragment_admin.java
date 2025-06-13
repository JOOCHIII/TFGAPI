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

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class home_fragment_admin extends Fragment {

    private TextView tvTotalProducts, tvDraftProducts;

    public home_fragment_admin() {}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home_admin, container, false);

        // Inicializar TextViews
        tvTotalProducts = view.findViewById(R.id.tvTotalProducts);
        tvDraftProducts = view.findViewById(R.id.tvDraftProducts);

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

        cargarContadores(); // Llamar a Retrofit para actualizar los contadores

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
}
