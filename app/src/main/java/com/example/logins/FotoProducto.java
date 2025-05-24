package com.example.logins; // o el paquete que uses
import com.example.logins.Productos;
import com.example.logins.FotoProducto;
public class FotoProducto {
    private int id;
    private String urlFoto;

    // Constructor vacío (opcional)
    public FotoProducto() {}

    // Getters y Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getUrlFoto() { return urlFoto; }
    public void setUrlFoto(String urlFoto) { this.urlFoto = urlFoto; }
}
