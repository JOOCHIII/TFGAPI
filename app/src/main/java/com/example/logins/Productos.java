package com.example.logins;
import com.example.logins.Productos;
import com.example.logins.FotoProducto;


import java.io.Serializable;
import java.util.List;
public class Productos implements Serializable {
    private int id;
    private String nombre;
    private String descripcion;
    private double precio;
    private int stock;
    private String categoria;
    private boolean destacado;
    private List<FotoProducto> fotos;

    // Constructor vacío
    public Productos() {}

    // Getters y Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public double getPrecio() { return precio; }
    public void setPrecio(double precio) { this.precio = precio; }

    public int getStock() { return stock; }
    public void setStock(int stock) { this.stock = stock; }

    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }

    public boolean isDestacado() { return destacado; }
    public void setDestacado(boolean destacado) { this.destacado = destacado; }

    public List<FotoProducto> getFotos() { return fotos; }
    public void setFotos(List<FotoProducto> fotos) { this.fotos = fotos; }
}
