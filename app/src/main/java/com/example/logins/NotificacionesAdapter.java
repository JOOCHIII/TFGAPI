package com.example.logins;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class NotificacionesAdapter extends RecyclerView.Adapter<NotificacionesAdapter.ViewHolder> {

    private List<Notificacion> notificaciones;

    public NotificacionesAdapter(List<Notificacion> notificaciones) {
        this.notificaciones = notificaciones;
    }

    public void setNotificaciones(List<Notificacion> notificaciones) {
        this.notificaciones = notificaciones;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_notificacion, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Notificacion noti = notificaciones.get(position);
        holder.mensaje.setText(noti.getMensaje());
        holder.fecha.setText(noti.getFecha());
        // Puedes añadir más campos y formato si quieres
    }

    @Override
    public int getItemCount() {
        return notificaciones.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView mensaje, fecha;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mensaje = itemView.findViewById(R.id.textMensaje);
            fecha = itemView.findViewById(R.id.textFecha);
        }
    }
}
