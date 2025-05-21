package com.example.logins;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

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
        View vista = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_reporte, parent, false);
        return new ReporteViewHolder(vista);
    }

    @Override
    public void onBindViewHolder(@NonNull ReporteViewHolder holder, int position) {
        ReporteDTO reporte = listaReportes.get(position);

        holder.textAsunto.setText("Asunto: " + reporte.getAsunto());
        holder.textDescripcion.setText("Descripción: " + reporte.getDescripcion());
        holder.textEstado.setText("Estado: " + reporte.getEstado());

        // Formatear la fecha
        String fechaOriginal = reporte.getFecha(); // Ej: 2024-05-21T22:35:00
        String fechaFormateada = "";
        try {
            SimpleDateFormat formatoEntrada = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault());
            Date fecha = formatoEntrada.parse(fechaOriginal);

            SimpleDateFormat formatoSalida = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault());
            fechaFormateada = formatoSalida.format(fecha);
        } catch (Exception e) {
            fechaFormateada = fechaOriginal; // Si falla el formato, mostrar como está
        }

        holder.textFecha.setText("Fecha: " + fechaFormateada);
    }

    @Override
    public int getItemCount() {
        return listaReportes != null ? listaReportes.size() : 0;
    }

    public static class ReporteViewHolder extends RecyclerView.ViewHolder {
        TextView textAsunto, textDescripcion, textEstado, textFecha;

        public ReporteViewHolder(@NonNull View itemView) {
            super(itemView);
            textAsunto = itemView.findViewById(R.id.textAsunto);
            textDescripcion = itemView.findViewById(R.id.textDescripcion);
            textEstado = itemView.findViewById(R.id.textEstado);
            textFecha = itemView.findViewById(R.id.textFecha);
        }
    }
}
