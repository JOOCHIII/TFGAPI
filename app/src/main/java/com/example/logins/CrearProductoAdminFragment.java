package com.example.logins;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.*;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CrearProductoAdminFragment extends Fragment {

    private EditText etNombre, etDescripcion, etPrecio, etStock, etCategoria, etFotos;
    private CheckBox checkDestacado;
    private Button btnCrearProducto;
    private RecyclerView recyclerFotosPreview;

    public CrearProductoAdminFragment() {}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_crear_producto_admin, container, false);

        etNombre = view.findViewById(R.id.etNombre);
        etDescripcion = view.findViewById(R.id.etDescripcion);
        etPrecio = view.findViewById(R.id.etPrecio);
        etStock = view.findViewById(R.id.etStock);
        etCategoria = view.findViewById(R.id.etCategoria);
        etFotos = view.findViewById(R.id.etFotos);
        checkDestacado = view.findViewById(R.id.checkDestacado);
        btnCrearProducto = view.findViewById(R.id.btnCrearProducto);
        recyclerFotosPreview = view.findViewById(R.id.recyclerFotosPreview);

        recyclerFotosPreview.setLayoutManager(
                new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        );

        // Previsualización múltiple
        etFotos.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                String[] urls = s.toString().split("\\s*,\\s*");
                List<String> fotos = new ArrayList<>();
                for (String url : urls) {
                    if (!url.trim().isEmpty()) fotos.add(url.trim());
                }
                recyclerFotosPreview.setAdapter(new FotoAdapter(requireContext(), fotos));
            }
        });

        btnCrearProducto.setOnClickListener(v -> crearProducto());

        return view;
    }

    private void crearProducto() {
        String nombre = etNombre.getText().toString().trim();
        String descripcion = etDescripcion.getText().toString().trim();
        String precioStr = etPrecio.getText().toString().trim();
        String stockStr = etStock.getText().toString().trim();
        String categoria = etCategoria.getText().toString().trim();
        String fotosStr = etFotos.getText().toString().trim();
        boolean destacado = checkDestacado.isChecked();

        if (nombre.isEmpty() || descripcion.isEmpty() || precioStr.isEmpty() ||
                stockStr.isEmpty() || categoria.isEmpty() || fotosStr.isEmpty()) {
            Toast.makeText(requireContext(), "Completa todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        double precio;
        int stock;
        try {
            precio = Double.parseDouble(precioStr);
            stock = Integer.parseInt(stockStr);
        } catch (NumberFormatException e) {
            Toast.makeText(requireContext(), "Precio o stock inválido", Toast.LENGTH_SHORT).show();
            return;
        }

        List<String> fotos = Arrays.asList(fotosStr.split("\\s*,\\s*"));

        ProductoRequestDTO producto = new ProductoRequestDTO(nombre, descripcion, precio, stock, categoria, fotos, destacado);
        ProductosApi api = RetrofitClient.getRetrofitInstance().create(ProductosApi.class);
        Call<Productos> call = api.crearProducto(producto);

        call.enqueue(new Callback<Productos>() {
            @Override
            public void onResponse(@NonNull Call<Productos> call, @NonNull Response<Productos> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(requireContext(), "Producto creado correctamente", Toast.LENGTH_SHORT).show();
                    limpiarCampos();
                } else {
                    Toast.makeText(requireContext(), "Error al crear el producto", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<Productos> call, @NonNull Throwable t) {
                Toast.makeText(requireContext(), "Error de red: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void limpiarCampos() {
        etNombre.setText("");
        etDescripcion.setText("");
        etPrecio.setText("");
        etStock.setText("");
        etCategoria.setText("");
        etFotos.setText("");
        checkDestacado.setChecked(false);
        recyclerFotosPreview.setAdapter(new FotoAdapter(requireContext(), new ArrayList<>()));
    }
}
