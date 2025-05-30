package com.example.logins;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CarritoAdapter extends RecyclerView.Adapter<CarritoAdapter.ViewHolder> {

    public interface OnCantidadChangeListener {
        void onCantidadChange();
    }

    private Context context;
    private List<CarritoDTO> carritoList;
    private OnCantidadChangeListener listener;
    private CarritoApi apiService;
    private long idUsuario;

    public CarritoAdapter(Context context, List<CarritoDTO> carritoList, OnCantidadChangeListener listener) {
        this.context = context;
        this.carritoList = carritoList;
        this.listener = listener;

        // Obtener idUsuario desde SharedPreferences
        SharedPreferences preferences = context.getSharedPreferences("usuario_prefs", Context.MODE_PRIVATE);
        this.idUsuario = preferences.getLong("id_usuario", -1);

        // Inicializar Retrofit y API
        apiService = RetrofitClient.getRetrofitInstance().create(CarritoApi.class);
    }

    @NonNull
    @Override
    public CarritoAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_carrito, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CarritoAdapter.ViewHolder holder, int position) {
        CarritoDTO item = carritoList.get(position);

        holder.tvNombre.setText(item.getNombre());
        holder.tvTalla.setText("Talla: " + item.getTalla());
        holder.tvCantidad.setText(String.valueOf(item.getCantidad()));
        holder.tvPrecio.setText(String.format("Precio: %.2f €", item.getPrecio()));
        double subtotal = item.getCantidad() * item.getPrecio();
        holder.tvSubtotal.setText(String.format("Subtotal: %.2f €", subtotal));

        Glide.with(context)
                .load(item.getImagenUrl())
                .placeholder(R.drawable.ic_placeholder)
                .error(R.drawable.ic_error)
                .into(holder.ivProducto);

        holder.btnSumar.setOnClickListener(v -> {
            int nuevaCantidad = item.getCantidad() + 1;
            actualizarCantidadEnAPI(item, nuevaCantidad, position);
        });

        holder.btnRestar.setOnClickListener(v -> {
            int nuevaCantidad = item.getCantidad() - 1;
            if (nuevaCantidad > 0) {
                actualizarCantidadEnAPI(item, nuevaCantidad, position);
            } else {
                Toast.makeText(context, "La cantidad no puede ser menor que 1", Toast.LENGTH_SHORT).show();
            }
        });

        holder.btnEliminar.setOnClickListener(v -> eliminarProductoEnAPI(item, position));
    }

    @Override
    public int getItemCount() {
        return carritoList.size();
    }

    private void actualizarCantidadEnAPI(CarritoDTO item, int nuevaCantidad, int position) {
        Call<String> call = apiService.actualizarCantidad(idUsuario, item.getIdProducto(), item.getTalla(), nuevaCantidad);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    item.setCantidad(nuevaCantidad);
                    notifyItemChanged(position);
                    listener.onCantidadChange();
                    Toast.makeText(context, "Cantidad actualizada", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "Error al actualizar cantidad", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(context, "Error de red: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void eliminarProductoEnAPI(CarritoDTO item, int position) {
        Call<String> call = apiService.eliminarDelCarrito(idUsuario, item.getIdProducto(), item.getTalla());
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    carritoList.remove(position);
                    notifyItemRemoved(position);
                    listener.onCantidadChange();
                    Toast.makeText(context, "Producto eliminado del carrito", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "Error al eliminar producto", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(context, "Error de red: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivProducto;
        TextView tvNombre, tvTalla, tvCantidad, tvPrecio, tvSubtotal;
        ImageButton btnSumar, btnRestar, btnEliminar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivProducto = itemView.findViewById(R.id.ivFotoProducto);
            tvNombre = itemView.findViewById(R.id.tvNombreProducto);
            tvTalla = itemView.findViewById(R.id.tvTallaProducto);
            tvCantidad = itemView.findViewById(R.id.tvCantidadProducto);
            tvPrecio = itemView.findViewById(R.id.tvPrecioUnitarioProducto);
            tvSubtotal = itemView.findViewById(R.id.tvSubtotalProducto);
            btnSumar = itemView.findViewById(R.id.btnSumarProducto);
            btnRestar = itemView.findViewById(R.id.btnRestarProducto);
            btnEliminar = itemView.findViewById(R.id.btnEliminarProducto);
        }
    }
}
