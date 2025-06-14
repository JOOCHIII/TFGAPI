package com.example.logins;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
// otros imports...

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class ProductosAdapter extends RecyclerView.Adapter<ProductosAdapter.ProductoViewHolder> implements Filterable {

    private List<Productos> productosList;       // Lista actual (filtrada)
    private List<Productos> productosListFull;   // Lista completa (original)
    private OnItemClickListener listener;

    private String filtroTextoActual = "";       // Texto de búsqueda actual
    private String categoriaSeleccionada = "Todos"; // Categoría actual

    public interface OnItemClickListener {
        void onItemClick(Productos producto);
    }

    public ProductosAdapter(List<Productos> productosList, OnItemClickListener listener) {
        this.productosList = new ArrayList<>(productosList);
        this.productosListFull = new ArrayList<>(productosList);
        this.listener = listener;
    }

    @NonNull
    @Override
    public ProductoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_producto, parent, false);
        return new ProductoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductoViewHolder holder, int position) {
        Productos producto = productosList.get(position);
        holder.tvNombre.setText(producto.getNombre());
        holder.tvDescripcion.setText(producto.getDescripcion());
        holder.tvPrecio.setText(String.format("%.2f €", producto.getPrecio()));

        if (producto.getFotos() != null && !producto.getFotos().isEmpty()) {
            Glide.with(holder.itemView.getContext())
                    .load(producto.getFotos().get(0).getUrlFoto())
                    .into(holder.imgProducto);
        } else {
            holder.imgProducto.setImageResource(R.drawable.placeholder);
        }

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(producto);
            }
        });
    }

    @Override
    public int getItemCount() {
        return productosList.size();
    }

    @Override
    public Filter getFilter() {
        return filtroProductos;
    }

    private Filter filtroProductos = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            filtroTextoActual = constraint != null ? constraint.toString().toLowerCase().trim() : "";

            List<Productos> listaFiltrada = new ArrayList<>();

            for (Productos producto : productosListFull) {
                boolean coincideTexto = producto.getNombre().toLowerCase().contains(filtroTextoActual);
                boolean coincideCategoria = categoriaSeleccionada.equals("Todos") ||
                        producto.getCategoria().equalsIgnoreCase(categoriaSeleccionada);

                if (coincideTexto && coincideCategoria) {
                    listaFiltrada.add(producto);
                }
            }

            FilterResults resultados = new FilterResults();
            resultados.values = listaFiltrada;
            return resultados;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            productosList.clear();
            productosList.addAll((List<Productos>) results.values);
            notifyDataSetChanged();
        }
    };

    // Método para filtrar por categoría desde el fragmento
    public void filtrarPorCategoria(String categoria) {
        this.categoriaSeleccionada = categoria;
        getFilter().filter(filtroTextoActual); // Reaplica el filtro completo
    }

    static class ProductoViewHolder extends RecyclerView.ViewHolder {
        TextView tvNombre, tvDescripcion, tvPrecio;
        ImageView imgProducto;

        public ProductoViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNombre = itemView.findViewById(R.id.tvNombre);
            tvDescripcion = itemView.findViewById(R.id.tvDescripcion);
            tvPrecio = itemView.findViewById(R.id.tvPrecio);
            imgProducto = itemView.findViewById(R.id.imgProducto);
        }
    }
}
