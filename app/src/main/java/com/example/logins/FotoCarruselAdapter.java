package com.example.logins;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class FotoCarruselAdapter extends RecyclerView.Adapter<FotoCarruselAdapter.FotoViewHolder> {

    private List<FotoProducto> fotos;
    private Context context;

    public FotoCarruselAdapter(Context context, List<FotoProducto> fotos) {
        this.context = context;
        this.fotos = fotos;
    }

    @NonNull
    @Override
    public FotoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View vista = LayoutInflater.from(context).inflate(R.layout.item_foto_carrusel, parent, false);
        return new FotoViewHolder(vista);
    }

    @Override
    public void onBindViewHolder(@NonNull FotoViewHolder holder, int position) {
        Glide.with(context)
                .load(fotos.get(position).getUrlFoto())
                .into(holder.imgFoto);
    }

    @Override
    public int getItemCount() {
        return fotos.size();
    }

    static class FotoViewHolder extends RecyclerView.ViewHolder {
        ImageView imgFoto;

        FotoViewHolder(@NonNull View itemView) {
            super(itemView);
            imgFoto = itemView.findViewById(R.id.imgCarrusel);
        }
    }
}
