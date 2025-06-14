package com.example.logins;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetalleProductoFragment extends Fragment {

    private static final String ARG_PRODUCTO = "producto";

    private Productos producto;

    private TextView tvNombre, tvDescripcion, tvPrecio;
    private Spinner spinnerTallas;
    private Button btnAgregarCesta;
    private ViewPager2 viewPagerFotos;
    private ImageButton btnFavorito;

    private long userId;
    private boolean esFavorito = false;

    public static DetalleProductoFragment newInstance(Productos producto) {
        DetalleProductoFragment fragment = new DetalleProductoFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PRODUCTO, producto);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true); // 👉 Activar el menú en este fragmento

        if (getArguments() != null) {
            producto = (Productos) getArguments().getSerializable(ARG_PRODUCTO);
        }

        SharedPreferences userPrefs = requireContext().getSharedPreferences("usuario_prefs", Context.MODE_PRIVATE);
        long idUsuarioGuardado = userPrefs.getLong("id_usuario", -1);
        userId = (idUsuarioGuardado != -1) ? idUsuarioGuardado : -1;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detalle_producto, container, false);

        tvNombre = view.findViewById(R.id.tvNombre);
        tvDescripcion = view.findViewById(R.id.tvDescripcion);
        tvPrecio = view.findViewById(R.id.tvPrecio);
        spinnerTallas = view.findViewById(R.id.spinnerTallas);
        btnAgregarCesta = view.findViewById(R.id.btnAgregarCesta);
        viewPagerFotos = view.findViewById(R.id.viewPagerFotos);
        btnFavorito = view.findViewById(R.id.btnFavorito);

        if (producto != null) {
            tvNombre.setText(producto.getNombre());
            tvDescripcion.setText(producto.getDescripcion());
            tvPrecio.setText(String.format("%.2f €", producto.getPrecio()));

            ArrayAdapter<String> adapterTallas = new ArrayAdapter<>(
                    requireContext(),
                    android.R.layout.simple_spinner_item,
                    new String[]{"S", "M", "L", "XL"}
            );
            adapterTallas.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerTallas.setAdapter(adapterTallas);

            List<String> listaUrlsFotos = producto.getUrlsFotos();
            if (listaUrlsFotos != null && !listaUrlsFotos.isEmpty()) {
                FotosPagerAdapter adapterFotos = new FotosPagerAdapter(requireContext(), listaUrlsFotos);
                viewPagerFotos.setAdapter(adapterFotos);
            }
        }

        btnAgregarCesta.setOnClickListener(v -> {
            String tallaSeleccionada = spinnerTallas.getSelectedItem().toString();
            CarritoApi carritoApi = RetrofitClient.getRetrofitInstance().create(CarritoApi.class);
            Call<String> call = carritoApi.agregarAlCarrito(userId, producto.getId(), tallaSeleccionada, 1);

            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(requireContext(), "Producto agregado al carrito con talla " + tallaSeleccionada, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(requireContext(), "Error al agregar al carrito", Toast.LENGTH_SHORT).show();
                        Log.e("DetalleProducto", "Error código: " + response.code());
                    }
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    Toast.makeText(requireContext(), "Error de red: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.e("DetalleProducto", "Error: ", t);
                }
            });
        });

        btnFavorito.setOnClickListener(v -> {
            if (userId == -1) {
                Toast.makeText(requireContext(), "Debes iniciar sesión para usar favoritos", Toast.LENGTH_SHORT).show();
                return;
            }
            if (esFavorito) {
                quitarDeFavoritos();
            } else {
                agregarAFavoritos();
            }
        });

        comprobarSiEsFavorito();

        return view;
    }

    private void comprobarSiEsFavorito() {
        if (userId == -1) {
            btnFavorito.setVisibility(View.GONE);
            return;
        }

        FavoritosApi favoritosApi = RetrofitClient.getRetrofitInstance().create(FavoritosApi.class);
        Call<List<Productos>> call = favoritosApi.obtenerFavoritos(userId);

        call.enqueue(new Callback<List<Productos>>() {
            @Override
            public void onResponse(Call<List<Productos>> call, Response<List<Productos>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Productos> favoritos = response.body();
                    esFavorito = false;
                    for (Productos p : favoritos) {
                        if (p.getId() == producto.getId()) {
                            esFavorito = true;
                            break;
                        }
                    }
                    actualizarIconoFavorito();
                } else {
                    Log.e("DetalleProducto", "Error al obtener favoritos: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<Productos>> call, Throwable t) {
                Log.e("DetalleProducto", "Error red al obtener favoritos", t);
            }
        });
    }

    private void agregarAFavoritos() {
        FavoritosApi favoritosApi = RetrofitClient.getRetrofitInstance().create(FavoritosApi.class);
        Call<String> call = favoritosApi.agregarFavorito(userId, producto.getId());

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    esFavorito = true;
                    actualizarIconoFavorito();
                    Toast.makeText(requireContext(), "Añadido a favoritos", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(requireContext(), "Error al añadir favorito", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(requireContext(), "Error de red al añadir favorito", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void quitarDeFavoritos() {
        FavoritosApi favoritosApi = RetrofitClient.getRetrofitInstance().create(FavoritosApi.class);
        Call<String> call = favoritosApi.eliminarFavorito(userId, producto.getId());

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    esFavorito = false;
                    actualizarIconoFavorito();
                    Toast.makeText(requireContext(), "Eliminado de favoritos", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(requireContext(), "Error al eliminar favorito", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(requireContext(), "Error de red al eliminar favorito", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void actualizarIconoFavorito() {
        if (esFavorito) {
            btnFavorito.setImageResource(R.drawable.ic_favorite);
        } else {
            btnFavorito.setImageResource(R.drawable.ic_favorite_border);
        }
    }

    // 👉 MENÚ SUPERIOR (Toolbar derecho)

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_home, menu); // Usa el mismo menú que en HomeFragment
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_cart) {
            requireActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.content_frame, new CarritoFragment())
                    .addToBackStack(null)
                    .commit();
            return true;
        } else if (id == R.id.action_favorites) {
            requireActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.content_frame, new FavoritosFragment())
                    .addToBackStack(null)
                    .commit();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
