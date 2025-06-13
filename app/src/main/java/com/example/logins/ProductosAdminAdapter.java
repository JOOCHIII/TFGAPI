package com.example.logins;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class ProductosAdminAdapter extends RecyclerView.Adapter<ProductosAdminAdapter.ProductoAdminViewHolder> {

    private List<Productos> productosList;

    public ProductosAdminAdapter(List<Productos> productosList) {
        this.productosList = productosList;
    }

    @NonNull
    @Override
    public ProductoAdminViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_producto_admin, parent, false);
        return new ProductoAdminViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductoAdminViewHolder holder, int position) {
        Productos producto = productosList.get(position);
        holder.tvNombre.setText(producto.getNombre());
        holder.tvDescripcion.setText(producto.getDescripcion());
        holder.tvPrecio.setText(String.format("%.2f €", producto.getPrecio()));
        holder.tvStock.setText("Stock: " + producto.getStock());

        if (producto.getFotos() != null && !producto.getFotos().isEmpty()) {
            Glide.with(holder.itemView.getContext())
                    .load(producto.getFotos().get(0).getUrlFoto())
                    .into(holder.imgProducto);
        } else {
            holder.imgProducto.setImageResource(R.drawable.placeholder);
        }
    }

    @Override
    public int getItemCount() {
        return productosList.size();
    }

    static class ProductoAdminViewHolder extends RecyclerView.ViewHolder {
        TextView tvNombre, tvDescripcion, tvPrecio, tvStock;
        ImageView imgProducto;

        public ProductoAdminViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNombre = itemView.findViewById(R.id.tvNombre);
            tvDescripcion = itemView.findViewById(R.id.tvDescripcion);
            tvPrecio = itemView.findViewById(R.id.tvPrecio);
            tvStock = itemView.findViewById(R.id.tvStock);
            imgProducto = itemView.findViewById(R.id.imgProducto);
        }
    }
}
