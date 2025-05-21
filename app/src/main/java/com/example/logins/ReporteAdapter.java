package com.example.logins;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ReporteAdapter extends RecyclerView.Adapter<ReporteAdapter.ReporteViewHolder> {

    private List<ReporteDTO> listaReportes;

    public ReporteAdapter(List<ReporteDTO> listaReportes) {
        this.listaReportes = listaReportes;
    }

    public void actualizarLista(List<ReporteDTO> nuevaLista) {
        this.listaReportes = nuevaLista;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ReporteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View vista = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_2, parent, false);
        return new ReporteViewHolder(vista);
    }

    @Override
    public void onBindViewHolder(@NonNull ReporteViewHolder holder, int position) {
        ReporteDTO reporte = listaReportes.get(position);
        holder.titulo.setText("Asunto: " + reporte.getAsunto());
        holder.descripcion.setText("Descripción: " + reporte.getDescripcion());
    }

    @Override
    public int getItemCount() {
        return listaReportes != null ? listaReportes.size() : 0;
    }

    public static class ReporteViewHolder extends RecyclerView.ViewHolder {
        TextView titulo, descripcion;

        public ReporteViewHolder(@NonNull View itemView) {
            super(itemView);
            titulo = itemView.findViewById(android.R.id.text1);
            descripcion = itemView.findViewById(android.R.id.text2);
        }
    }
}
