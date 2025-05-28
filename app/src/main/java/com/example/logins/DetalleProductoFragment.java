package com.example.logins;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
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
    private static final String PREFS_FAVORITOS = "prefs_favoritos";

    private Productos producto;

    private TextView tvNombre, tvDescripcion, tvPrecio;
    private Spinner spinnerTallas;
    private Button btnAgregarCesta;
    private ViewPager2 viewPagerFotos;
    private ImageButton btnFavorito;

    private String userId;  // ID usuario desde SharedPreferences

    private SharedPreferences sharedPreferences;

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
        if (getArguments() != null) {
            producto = (Productos) getArguments().getSerializable(ARG_PRODUCTO);
        }

        SharedPreferences userPrefs = requireContext().getSharedPreferences("usuario_prefs", Context.MODE_PRIVATE);
        long idUsuarioGuardado = userPrefs.getLong("id_usuario", -1);
        if (idUsuarioGuardado != -1) {
            userId = String.valueOf(idUsuarioGuardado);
        } else {
            userId = "usuario_desconocido";
        }

        sharedPreferences = requireContext().getSharedPreferences(PREFS_FAVORITOS, Context.MODE_PRIVATE);
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

            List<FotoProducto> fotos = producto.getFotos();
            if (fotos != null && !fotos.isEmpty()) {
                FotoCarruselAdapter adapter = new FotoCarruselAdapter(requireContext(), fotos);
                viewPagerFotos.setAdapter(adapter);
            } else {
                viewPagerFotos.setVisibility(View.GONE);
            }

            ArrayAdapter<String> adapterTallas = new ArrayAdapter<>(
                    requireContext(),
                    android.R.layout.simple_spinner_item,
                    new String[]{"S", "M", "L", "XL"}
            );
            adapterTallas.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerTallas.setAdapter(adapterTallas);

            // Cargar el estado favorito desde la API y actualizar icono
            cargarEstadoFavorito();
        }

        btnAgregarCesta.setOnClickListener(v -> {
            String tallaSeleccionada = spinnerTallas.getSelectedItem().toString();

            // Enviar cantidad = 1 (o la que quieras)
            CarritoApi carritoApi = RetrofitClient.getRetrofitInstance().create(CarritoApi.class);
            Call<String> call = carritoApi.agregarAlCarrito(Long.parseLong(userId), producto.getId(), 1);

            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(getContext(),
                                "Añadido a la cesta: " + producto.getNombre() + " (Cantidad: 1)",
                                Toast.LENGTH_SHORT).show();
                    } else {
                        // Mostrar detalle del error para depurar
                        try {
                            String errorBody = response.errorBody().string();
                            Toast.makeText(getContext(),
                                    "Error " + response.code() + ": " + errorBody,
                                    Toast.LENGTH_LONG).show();
                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(getContext(),
                                    "Error " + response.code() + ": No se pudo leer el error",
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    Toast.makeText(getContext(), "Error de conexión", Toast.LENGTH_SHORT).show();
                }
            });
        });




        btnFavorito.setOnClickListener(v -> {
            toggleFavorito();
        });

        return view;
    }

    private void cargarEstadoFavorito() {
        FavoritosApi api = RetrofitClient.getRetrofitInstance().create(FavoritosApi.class);
        Call<List<Productos>> call = api.obtenerFavoritos(Long.parseLong(userId));

        call.enqueue(new Callback<List<Productos>>() {
            @Override
            public void onResponse(Call<List<Productos>> call, Response<List<Productos>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    boolean estaFavorito = false;
                    List<Productos> favoritos = response.body();
                    for (Productos p : favoritos) {
                        if (p.getId() == producto.getId()) {
                            estaFavorito = true;
                            break;
                        }
                    }
                    actualizarIconoFavorito(estaFavorito);
                } else {
                    actualizarIconoFavorito(false);
                }
            }

            @Override
            public void onFailure(Call<List<Productos>> call, Throwable t) {
                actualizarIconoFavorito(false);
            }
        });
    }

    private void toggleFavorito() {
        FavoritosApi api = RetrofitClient.getRetrofitInstance().create(FavoritosApi.class);

        boolean esFavoritoAhora = btnFavorito.getTag() != null && (boolean) btnFavorito.getTag();

        if (esFavoritoAhora) {
            // Quitar favorito
            Call<String> call = api.eliminarFavorito(Long.parseLong(userId), producto.getId());
            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    if (response.isSuccessful()) {
                        actualizarIconoFavorito(false);
                        Toast.makeText(getContext(), "Producto eliminado de favoritos", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getContext(), "No se pudo eliminar favorito", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    Toast.makeText(getContext(), "Error de conexión", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            // Añadir favorito
            Call<String> call = api.agregarFavorito(Long.parseLong(userId), producto.getId());
            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    if (response.isSuccessful()) {
                        actualizarIconoFavorito(true);
                        Toast.makeText(getContext(), "Producto añadido a favoritos", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getContext(), "No se pudo añadir favorito", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    Toast.makeText(getContext(), "Error de conexión", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void actualizarIconoFavorito(boolean esFavorito) {
        if (esFavorito) {
            btnFavorito.setImageResource(R.drawable.ic_favorite); // corazón relleno
            btnFavorito.setTag(true);
        } else {
            btnFavorito.setImageResource(R.drawable.ic_favorite_border); // corazón borde
            btnFavorito.setTag(false);
        }
    }
}
