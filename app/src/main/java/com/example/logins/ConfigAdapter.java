package com.example.logins;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ConfigAdapter extends RecyclerView.Adapter<ConfigAdapter.ViewHolder> {

    private final List<OpcionConfig> opciones;
    private final OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public ConfigAdapter(List<OpcionConfig> opciones, OnItemClickListener listener) {
        this.opciones = opciones;
        this.listener = listener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView icono;
        TextView titulo;
        Switch switchAppearance; // Aquí agregamos el Switch

        public ViewHolder(View itemView, OnItemClickListener listener) {
            super(itemView);
            icono = itemView.findViewById(R.id.icono);
            titulo = itemView.findViewById(R.id.titulo);
            switchAppearance = itemView.findViewById(R.id.switch_appearance); // Inicializamos el Switch
            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onItemClick(getAdapterPosition());
                }
            });
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_menu_config, parent, false);
        return new ViewHolder(v, listener);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        OpcionConfig opcion = opciones.get(position);
        holder.titulo.setText(opcion.titulo);
        holder.icono.setImageResource(opcion.icono);

        // Si la opción es "Apariencia", mostrar el Switch
        if (position == 2) { // 2 es el índice de "Apariencia"
            holder.switchAppearance.setVisibility(View.VISIBLE);
            // Si el Switch está activado, cambia el tema de la app
            holder.switchAppearance.setChecked(isDarkMode(holder.itemView.getContext()));
            holder.switchAppearance.setOnCheckedChangeListener((buttonView, isChecked) -> {
                // Cambiar el tema según el estado del Switch
                toggleDarkMode(isChecked, holder.itemView.getContext());
            });
        } else {
            holder.switchAppearance.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return opciones.size();
    }

    private boolean isDarkMode(Context context) {
        // Verifica si el modo oscuro está activado
        int nightModeFlags = context.getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        return nightModeFlags == Configuration.UI_MODE_NIGHT_YES;
    }

    private void toggleDarkMode(boolean isEnabled, Context context) {
        // Cambiar entre modo oscuro y modo claro
        int mode = isEnabled ? AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO;
        AppCompatDelegate.setDefaultNightMode(mode);  // Cambia el tema global
        ((Activity) context).recreate();  // Vuelve a recrear la actividad para aplicar el cambio
    }
}
