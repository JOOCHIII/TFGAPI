package com.example.logins;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class PedidoAdapter extends RecyclerView.Adapter<PedidoAdapter.PedidoViewHolder> {

    private List<Pedido> listaPedidos;
    private Context context;
    private OnPedidoClickListener listener;

    public interface OnPedidoClickListener {
        void onPedidoClick(Pedido pedido);
    }

    // Constructor con listener para el click en pedido
    public PedidoAdapter(List<Pedido> listaPedidos, Context context, OnPedidoClickListener listener) {
        this.listaPedidos = listaPedidos;
        this.context = context;
        this.listener = listener;
    }

    @NonNull
    @Override
    public PedidoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View vista = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_pedido, parent, false);
        return new PedidoViewHolder(vista);
    }

    @Override
    public void onBindViewHolder(@NonNull PedidoViewHolder holder, int position) {
        Pedido pedido = listaPedidos.get(position);
        holder.tvPedidoId.setText("Pedido #" + pedido.getId());
        holder.tvFecha.setText("Fecha: " + pedido.getFecha().substring(0, 10)); // Solo fecha (yyyy-MM-dd)
        holder.tvEstado.setText("Estado: " + pedido.getEstado());
        holder.tvTotal.setText("Total: " + String.format("%.2f", pedido.getTotal()) + " €");

        // Manejar click en el item
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onPedidoClick(pedido);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listaPedidos.size();
    }

    static class PedidoViewHolder extends RecyclerView.ViewHolder {
        TextView tvPedidoId, tvFecha, tvEstado, tvTotal;

        public PedidoViewHolder(@NonNull View itemView) {
            super(itemView);
            tvPedidoId = itemView.findViewById(R.id.tvPedidoId);
            tvFecha = itemView.findViewById(R.id.tvFecha);
            tvEstado = itemView.findViewById(R.id.tvEstado);
            tvTotal = itemView.findViewById(R.id.tvTotal);
        }
    }
}
