package com.example.logins;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import java.util.List;

public class FotoAdapter extends RecyclerView.Adapter<FotoAdapter.FotoViewHolder> {

    private Context context;
    private List<String> fotos;

    public FotoAdapter(Context context, List<String> fotos) {
        this.context = context;
        this.fotos = fotos;
    }

    @Override
    public FotoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_foto, parent, false);
        return new FotoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(FotoViewHolder holder, int position) {
        String url = fotos.get(position);
        Glide.with(context).load(url).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return fotos.size();
    }

    public static class FotoViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        public FotoViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageViewFoto);
        }
    }
}
