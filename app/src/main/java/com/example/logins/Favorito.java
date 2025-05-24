package com.example.logins;

public class Favorito {
    private FavoritoId id;
    private Productos producto;  // Debe coincidir con la respuesta JSON

    public FavoritoId getId() { return id; }
    public void setId(FavoritoId id) { this.id = id; }

    public Productos getProducto() { return producto; }
    public void setProducto(Productos producto) { this.producto = producto; }

    public static class FavoritoId {
        private long idUsuario;
        private long idProducto;

        public long getIdUsuario() { return idUsuario; }
        public long getIdProducto() { return idProducto; }
    }
}
