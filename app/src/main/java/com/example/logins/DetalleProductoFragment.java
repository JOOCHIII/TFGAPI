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

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            producto = (Productos) getArguments().getSerializable(ARG_PRODUCTO);
        }

        SharedPreferences userPrefs = requireContext().getSharedPreferences("usuario_prefs", Context.MODE_PRIVATE);
        long idUsuarioGuardado = userPrefs.getLong("id_usuario", -1);
        if (idUsuarioGuardado != -1) {
            userId = idUsuarioGuardado;
        } else {
            userId = -1;
        }
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

        // Aquí podrías manejar la lógica de favoritos si lo deseas

        return view;
    }

    public static DetalleProductoFragment newInstance(Productos producto) {
        DetalleProductoFragment fragment = new DetalleProductoFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PRODUCTO, producto);
        fragment.setArguments(args);
        return fragment;
    }
}
