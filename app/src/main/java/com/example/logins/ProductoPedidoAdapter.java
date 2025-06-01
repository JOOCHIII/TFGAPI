package com.example.logins;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class ProductoPedidoAdapter extends RecyclerView.Adapter<ProductoPedidoAdapter.ViewHolder> {

    private List<ProductoPedidoDTO> productos;
    private Context context;

    public ProductoPedidoAdapter(List<ProductoPedidoDTO> productos, Context context) {
        this.productos = productos;
        this.context = context;
    }

    @NonNull
    @Override
    public ProductoPedidoAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_producto_pedido, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductoPedidoAdapter.ViewHolder holder, int position) {
        ProductoPedidoDTO producto = productos.get(position);
        holder.tvNombre.setText(producto.getNombre());
        holder.tvCantidad.setText("Cantidad: " + producto.getCantidad());
        holder.tvPrecio.setText(String.format("Precio: %.2f €", producto.getPrecio()));

        // Cargar imagen con Glide
        Glide.with(context)
                .load(producto.getImagenUrl())
                .placeholder(R.drawable.ic_placeholder)
                .into(holder.ivProducto);
    }

    @Override
    public int getItemCount() {
        return productos.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvNombre, tvCantidad, tvPrecio;
        ImageView ivProducto;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNombre = itemView.findViewById(R.id.tvNombreProducto);
            tvCantidad = itemView.findViewById(R.id.tvCantidad);
            tvPrecio = itemView.findViewById(R.id.tvPrecio);
            ivProducto = itemView.findViewById(R.id.ivProducto);
        }
    }
}
