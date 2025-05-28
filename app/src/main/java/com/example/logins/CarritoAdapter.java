package com.example.logins;

import android.annotation.SuppressLint;
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

import com.squareup.picasso.Picasso;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CarritoAdapter extends RecyclerView.Adapter<CarritoAdapter.CarritoViewHolder> {

    public interface OnCantidadChangeListener {
        void onCantidadChange();
    }

    private Context context;
    private List<Carrito> cartItems;
    private OnCantidadChangeListener listener;

    public CarritoAdapter(Context context, List<Carrito> cartItems, OnCantidadChangeListener listener) {
        this.context = context;
        this.cartItems = cartItems;
        this.listener = listener;
    }

    @NonNull
    @Override
    public CarritoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_carrito, parent, false);
        return new CarritoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CarritoViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Carrito item = cartItems.get(position);
        Productos producto = item.getProducto();

        holder.tvNombre.setText(producto.getNombre());
        holder.tvPrecioUnitario.setText(String.format("%.2f €", producto.getPrecio()));
        holder.tvCantidad.setText(String.valueOf(item.getCantidad()));
        holder.tvSubtotal.setText(String.format("%.2f €", producto.getPrecio() * item.getCantidad()));

        // Cargar imagen
        if (producto.getFotos() != null && !producto.getFotos().isEmpty()) {
            String urlFoto = producto.getFotos().get(0).getUrlFoto();
            if (urlFoto != null && !urlFoto.isEmpty()) {
                Picasso.get()
                        .load(urlFoto)
                        .placeholder(R.drawable.ic_placeholder)
                        .error(R.drawable.ic_error)
                        .into(holder.ivFoto);
            } else {
                holder.ivFoto.setImageResource(R.drawable.ic_placeholder);
            }
        } else {
            holder.ivFoto.setImageResource(R.drawable.ic_placeholder);
        }

        // Botón para aumentar cantidad
        holder.btnSumar.setOnClickListener(v -> {
            item.setCantidad(item.getCantidad() + 1);
            actualizarCantidadEnServidor(item.getCantidad(), producto.getId());
            notifyItemChanged(position);
            listener.onCantidadChange();
        });

        // Botón para disminuir cantidad
        holder.btnRestar.setOnClickListener(v -> {
            if (item.getCantidad() > 1) {
                item.setCantidad(item.getCantidad() - 1);
                actualizarCantidadEnServidor(item.getCantidad(), producto.getId());
                notifyItemChanged(position);
                listener.onCantidadChange();
            }
        });

        // Botón eliminar
        holder.btnEliminar.setOnClickListener(v -> {
            SharedPreferences preferences = context.getSharedPreferences("usuario_prefs", Context.MODE_PRIVATE);
            long idUsuario = preferences.getLong("id_usuario", -1);
            long idProducto = producto.getId();

            if (idUsuario != -1) {
                CarritoApi api = RetrofitClient.getRetrofitInstance().create(CarritoApi.class);
                Call<Void> call = api.eliminarDelCarrito(idUsuario, idProducto);

                call.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if (response.isSuccessful()) {
                            cartItems.remove(position);
                            notifyItemRemoved(position);
                            notifyItemRangeChanged(position, cartItems.size());
                            listener.onCantidadChange();
                            Toast.makeText(context, "Producto eliminado del carrito", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(context, "No se pudo eliminar el producto", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Toast.makeText(context, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                Toast.makeText(context, "ID de usuario no disponible", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return cartItems.size();
    }

    static class CarritoViewHolder extends RecyclerView.ViewHolder {
        ImageView ivFoto;
        TextView tvNombre, tvPrecioUnitario, tvCantidad, tvSubtotal;
        ImageButton btnSumar, btnRestar, btnEliminar;

        public CarritoViewHolder(@NonNull View itemView) {
            super(itemView);
            ivFoto = itemView.findViewById(R.id.ivFoto);
            tvNombre = itemView.findViewById(R.id.tvNombre);
            tvPrecioUnitario = itemView.findViewById(R.id.tvPrecioUnitario);
            tvCantidad = itemView.findViewById(R.id.tvCantidad);
            tvSubtotal = itemView.findViewById(R.id.tvSubtotal);
            btnSumar = itemView.findViewById(R.id.btnSumar);
            btnRestar = itemView.findViewById(R.id.btnRestar);
            btnEliminar = itemView.findViewById(R.id.btnEliminar);
        }
    }

    private void actualizarCantidadEnServidor(int nuevaCantidad, long idProducto) {
        SharedPreferences preferences = context.getSharedPreferences("usuario_prefs", Context.MODE_PRIVATE);
        long idUsuario = preferences.getLong("id_usuario", -1);

        if (idUsuario != -1) {
            CarritoApi api = RetrofitClient.getRetrofitInstance().create(CarritoApi.class);
            Call<Void> call = api.actualizarCantidad(idUsuario, idProducto, nuevaCantidad);

            call.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if (!response.isSuccessful()) {
                        Toast.makeText(context, "No se pudo actualizar la cantidad", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    Toast.makeText(context, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
