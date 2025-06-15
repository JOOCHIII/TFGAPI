package com.example.logins;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class PedidoAdminAdapter extends RecyclerView.Adapter<PedidoAdminAdapter.ViewHolder> {

    public interface OnItemClickListener {
        void onEstadoChange(PedidoAdmin pedido, String nuevoEstado);
        void onVerDetalles(PedidoAdmin pedido);
    }

    private List<PedidoAdmin> pedidos;
    private OnItemClickListener listener;

    public PedidoAdminAdapter(List<PedidoAdmin> pedidos, OnItemClickListener listener) {
        this.pedidos = pedidos;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pedido_admin, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        PedidoAdmin pedido = pedidos.get(position);
        holder.bind(pedido, listener);
    }

    @Override
    public int getItemCount() {
        return pedidos.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvId, tvFecha, tvTotal, tvUsuario;
        Spinner spEstado;
        Button btnActualizar, btnVerDetalles;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvId = itemView.findViewById(R.id.tvPedidoIdAdmin);
            tvFecha = itemView.findViewById(R.id.tvPedidoFechaAdmin);
            tvTotal = itemView.findViewById(R.id.tvPedidoTotalAdmin);
            tvUsuario = itemView.findViewById(R.id.tvPedidoUsuarioAdmin);
            spEstado = itemView.findViewById(R.id.spinnerEstadoAdmin);
            btnActualizar = itemView.findViewById(R.id.btnActualizarEstadoAdmin);
            btnVerDetalles = itemView.findViewById(R.id.btnVerDetallesAdmin);
        }

        public void bind(PedidoAdmin pedido, OnItemClickListener listener) {
            tvId.setText("Pedido #" + pedido.getId());
            tvFecha.setText("Fecha: " + pedido.getFecha().substring(0, 16).replace("T", " "));
            tvTotal.setText("Total: " + pedido.getTotal()+"€");
            tvUsuario.setText("Usuario: " + pedido.getNombreUsuario());

            String[] estados = {"Pendiente", "En proceso", "Enviado", "Entregado", "Cancelado"};
            ArrayAdapter<String> adapter = new ArrayAdapter<>(itemView.getContext(),
                    android.R.layout.simple_spinner_item, estados);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spEstado.setAdapter(adapter);

            int selectedIndex = 0;
            for (int i = 0; i < estados.length; i++) {
                if (estados[i].equalsIgnoreCase(pedido.getEstado())) {
                    selectedIndex = i;
                    break;
                }
            }
            spEstado.setSelection(selectedIndex);

            btnActualizar.setOnClickListener(v -> {
                String nuevoEstado = spEstado.getSelectedItem().toString();
                if (!nuevoEstado.equalsIgnoreCase(pedido.getEstado())) {
                    listener.onEstadoChange(pedido, nuevoEstado);
                }
            });

            btnVerDetalles.setOnClickListener(v -> listener.onVerDetalles(pedido));
        }
    }
}
