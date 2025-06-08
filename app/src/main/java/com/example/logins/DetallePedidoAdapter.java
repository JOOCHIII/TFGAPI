package com.example.logins;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.logins.ItemDetalleDTO;

import java.util.List;

public class DetallePedidoAdapter extends RecyclerView.Adapter<DetallePedidoAdapter.ViewHolder> {

    private List<ItemDetalleDTO> detalleList;

    public DetallePedidoAdapter(List<ItemDetalleDTO> detalleList) {
        this.detalleList = detalleList;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvNombreProducto, tvCantidad, tvTalla, tvPrecio;
        ImageView ivProducto;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNombreProducto = itemView.findViewById(R.id.tvNombreProducto);
            tvCantidad = itemView.findViewById(R.id.tvCantidad);
            tvTalla = itemView.findViewById(R.id.tvTalla);
            tvPrecio = itemView.findViewById(R.id.tvPrecio);
            ivProducto = itemView.findViewById(R.id.ivProducto);
        }
    }

    @NonNull
    @Override
    public DetallePedidoAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_detalle_pedido, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DetallePedidoAdapter.ViewHolder holder, int position) {
        ItemDetalleDTO item = detalleList.get(position);

        holder.tvNombreProducto.setText(item.getNombreProducto());
        holder.tvCantidad.setText("Cantidad: " + item.getCantidad());
        holder.tvTalla.setText("Talla: " + item.getTalla());
        holder.tvPrecio.setText("Precio: " + String.format("%.2f €", item.getPrecioUnitario()));
        Glide.with(holder.itemView.getContext())
                .load(item.getImagenUrl()) // Ajusta el getter según tu DTO
                .placeholder(R.drawable.ic_placeholder) // imagen mientras carga
                .error(R.drawable.ic_error) // imagen si falla
                .into(holder.ivProducto);
    }

    @Override
    public int getItemCount() {
        return detalleList.size();
    }
}
