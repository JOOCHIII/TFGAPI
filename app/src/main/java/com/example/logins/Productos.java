package com.example.logins;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Productos implements Serializable {
    private long id;
    private String nombre;
    private String descripcion;
    private double precio;
    private List<FotoProducto> fotos;

    // Constructor vacío
    public Productos() {}

    public Productos(long id, String nombre, String descripcion, double precio, List<FotoProducto> fotos) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
        this.fotos = fotos;
    }

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public double getPrecio() { return precio; }
    public void setPrecio(double precio) { this.precio = precio; }

    public List<FotoProducto> getFotos() { return fotos; }
    public void setFotos(List<FotoProducto> fotos) { this.fotos = fotos; }

    // Método para obtener las URLs de fotos a partir de la lista de objetos FotoProducto
    public List<String> getUrlsFotos() {
        if (fotos == null) return new ArrayList<>();
        List<String> urls = new ArrayList<>();
        for (FotoProducto foto : fotos) {
            if (foto.getUrlFoto() != null) {
                urls.add(foto.getUrlFoto());
            }
        }
        return urls;
    }
}
