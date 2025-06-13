package com.example.logins;


import android.annotation.SuppressLint;
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

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductosAdminFragment extends Fragment {

    private RecyclerView recyclerView;
    private ProductosAdminAdapter adapter;

    @SuppressLint("MissingInflatedId")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_productos_admin, container, false);
        recyclerView = view.findViewById(R.id.recyclerViewProductosAdmin);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        cargarProductos();
        return view;
    }

    private void cargarProductos() {
        ProductosApi apiService = RetrofitClient.getRetrofitInstance().create(ProductosApi.class);
        Call<List<Productos>> call = apiService.getProductos();

        call.enqueue(new Callback<List<Productos>>() {
            @Override
            public void onResponse(Call<List<Productos>> call, Response<List<Productos>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Productos> productos = response.body();
                    adapter = new ProductosAdminAdapter(productos);
                    recyclerView.setAdapter(adapter);
                } else {
                    Toast.makeText(getContext(), "Error al obtener productos", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Productos>> call, Throwable t) {
                Toast.makeText(getContext(), "Error de red: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
