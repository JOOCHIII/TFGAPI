package com.example.logins;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.PopupMenu;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {

    private RecyclerView recyclerView;
    private ProductosAdapter adapter;
    private List<Productos> listaProductos = new ArrayList<>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true); // Para activar el menú
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        recyclerView = view.findViewById(R.id.recyclerViewProductos);
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
                    listaProductos = response.body();

                    adapter = new ProductosAdapter(listaProductos, producto -> {
                        DetalleProductoFragment fragment = DetalleProductoFragment.newInstance(producto);
                        requireActivity().getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.content_frame, fragment)
                                .addToBackStack(null)
                                .commit();
                    });

                    recyclerView.setAdapter(adapter);
                } else {
                    Toast.makeText(getContext(), "Error al cargar productos", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Productos>> call, Throwable t) {
                Toast.makeText(getContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_home, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();

        searchView.setQueryHint("Buscar productos...");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (adapter != null) {
                    adapter.getFilter().filter(query);
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (adapter != null) {
                    adapter.getFilter().filter(newText);
                }
                return true;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_cart) {
            SharedPreferences preferences = requireActivity().getSharedPreferences("usuario_prefs", getContext().MODE_PRIVATE);
            long idUsuario = preferences.getLong("id_usuario", -1);

            if (idUsuario != -1) {
                CarritoFragment fragment = CarritoFragment.newInstance(idUsuario);

                requireActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.content_frame, fragment)
                        .addToBackStack(null)
                        .commit();
            } else {
                Toast.makeText(getContext(), "ID de usuario no disponible", Toast.LENGTH_SHORT).show();
            }
            return true;

        } else if (id == R.id.action_favorites) {
            getParentFragmentManager().beginTransaction()
                    .replace(R.id.content_frame, new FavoritosFragment())
                    .addToBackStack(null)
                    .commit();
            return true;

        } else if (id == R.id.action_filter_category) {
            cargarCategoriasDesdeApi();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void cargarCategoriasDesdeApi() {
        ProductosApi apiService = RetrofitClient.getRetrofitInstance().create(ProductosApi.class);
        Call<List<String>> call = apiService.getCategorias();

        call.enqueue(new Callback<List<String>>() {
            @Override
            public void onResponse(Call<List<String>> call, Response<List<String>> response) {
                int codigo = response.code();
                List<String> categorias = response.body();

                Log.d("CATEGORIAS", "Código respuesta: " + codigo);
                Log.d("CATEGORIAS", "Cuerpo respuesta: " + categorias);

                if (response.isSuccessful()) {
                    if (categorias != null && !categorias.isEmpty()) {
                        mostrarMenuCategorias(categorias);
                    } else {
                        Toast.makeText(getContext(), "No hay categorías disponibles", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getContext(), "Error al obtener categorías: " + codigo, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<String>> call, Throwable t) {
                Toast.makeText(getContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("CATEGORIAS", "onFailure: " + t.getMessage(), t);
            }
        });
    }

    private void mostrarMenuCategorias(List<String> categorias) {
        View anchor = requireActivity().findViewById(R.id.action_filter_category);
        PopupMenu popupMenu = new PopupMenu(getContext(), anchor);

        popupMenu.getMenu().add("Todos"); // opción para mostrar todo

        for (String categoria : categorias) {
            popupMenu.getMenu().add(categoria);
        }

        popupMenu.setOnMenuItemClickListener(item -> {
            String seleccion = item.getTitle().toString();
            if (adapter != null) {
                adapter.filtrarPorCategoria(seleccion);
            }
            return true;
        });

        popupMenu.show();
    }

}
