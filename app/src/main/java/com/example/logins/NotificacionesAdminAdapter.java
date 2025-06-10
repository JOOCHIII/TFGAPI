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

public class NotificacionesAdminAdapter extends RecyclerView.Adapter<NotificacionesAdminAdapter.ViewHolder> {

    private List<NotificacionPedidoAdmin> notificaciones;

    public NotificacionesAdminAdapter(List<NotificacionPedidoAdmin> notificaciones) {
        this.notificaciones = notificaciones;
    }

    public void setNotificaciones(List<NotificacionPedidoAdmin> notificaciones) {
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
        NotificacionPedidoAdmin noti = notificaciones.get(position);
        holder.mensaje.setText(noti.getMensaje());

        String fechaOriginal = noti.getFecha();
        String fechaFormateada;
        try {
            SimpleDateFormat formatoEntrada = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault());
            Date fecha = formatoEntrada.parse(fechaOriginal);

            SimpleDateFormat formatoSalida = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault());
            fechaFormateada = formatoSalida.format(fecha);
        } catch (Exception e) {
            fechaFormateada = fechaOriginal;
        }

        holder.fecha.setText(fechaFormateada);
    }

    @Override
    public int getItemCount() {
        return notificaciones != null ? notificaciones.size() : 0;
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
