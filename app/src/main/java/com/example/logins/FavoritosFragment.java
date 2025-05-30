package com.example.logins;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FavoritosFragment extends Fragment {

    private RecyclerView recyclerView;
    private TextView textoSinFavoritos;
    private ProductosAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.favoritosfragment, container, false);

        recyclerView = view.findViewById(R.id.recyclerViewFavoritos);
        textoSinFavoritos = view.findViewById(R.id.textoSinFavoritos);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        cargarFavoritos();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        cargarFavoritos();
    }

    private void cargarFavoritos() {
        SharedPreferences preferences = requireContext().getSharedPreferences("usuario_prefs", Context.MODE_PRIVATE);
        long idUsuario = preferences.getLong("id_usuario", -1);

        if (idUsuario == -1) {
            textoSinFavoritos.setText("Usuario no autenticado");
            textoSinFavoritos.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
            return;
        }

        FavoritosApi api = RetrofitClient.getRetrofitInstance().create(FavoritosApi.class);
        Call<List<Productos>> call = api.obtenerFavoritos(idUsuario);

        call.enqueue(new Callback<List<Productos>>() {
            @Override
            public void onResponse(Call<List<Productos>> call, Response<List<Productos>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Productos> productosFavoritos = response.body();

                    if (productosFavoritos.isEmpty()) {
                        textoSinFavoritos.setText("No tienes favoritos");
                        textoSinFavoritos.setVisibility(View.VISIBLE);
                        recyclerView.setVisibility(View.GONE);
                    } else {
                        adapter = new ProductosAdapter(productosFavoritos, producto -> {
                            DetalleProductoFragment fragment = DetalleProductoFragment.newInstance(producto);
                            requireActivity().getSupportFragmentManager()
                                    .beginTransaction()
                                    .replace(R.id.content_frame, fragment)
                                    .addToBackStack(null)
                                    .commit();
                        });
                        recyclerView.setAdapter(adapter);
                        textoSinFavoritos.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.VISIBLE);
                    }
                } else {
                    textoSinFavoritos.setText("No tienes favoritos");
                    textoSinFavoritos.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<List<Productos>> call, Throwable t) {
                textoSinFavoritos.setText("Error: " + t.getMessage());
                textoSinFavoritos.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
            }
        });
    }
}
