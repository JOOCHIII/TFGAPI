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

public class FotosPagerAdapter extends RecyclerView.Adapter<FotosPagerAdapter.FotoViewHolder> {

    private List<String> listaUrls;
    private Context context;

    public FotosPagerAdapter(Context context, List<String> listaUrls) {
        this.context = context;
        this.listaUrls = listaUrls;
    }

    @NonNull
    @Override
    public FotoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_foto, parent, false);
        return new FotoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FotoViewHolder holder, int position) {
        String url = listaUrls.get(position);
        Glide.with(context)
                .load(url)
                .centerCrop()
                .into(holder.imageViewFoto);
    }

    @Override
    public int getItemCount() {
        return listaUrls.size();
    }

    public static class FotoViewHolder extends RecyclerView.ViewHolder {
        ImageView imageViewFoto;

        public FotoViewHolder(@NonNull View itemView) {
            super(itemView);
            imageViewFoto = itemView.findViewById(R.id.imageViewFoto);
        }
    }
}
