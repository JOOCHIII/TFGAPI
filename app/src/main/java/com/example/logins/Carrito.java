package com.example.logins;

import java.io.Serializable;

public class Carrito implements Serializable {
    private long idProducto;
    private String nombre;
    private String descripcion;
    private double precio;
    private String talla;
    private int cantidad;

    public Carrito() {
        // Constructor vacío requerido por Retrofit
    }

    public Carrito(long idProducto, String nombre, String descripcion, double precio, String talla, int cantidad) {
        this.idProducto = idProducto;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
        this.talla = talla;
        this.cantidad = cantidad;
    }

    public long getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(long idProducto) {
        this.idProducto = idProducto;
    }

    public String getNombre() {
        return nombre != null ? nombre : "";
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion != null ? descripcion : "";
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public String getTalla() {
        return talla != null ? talla : "";
    }

    public void setTalla(String talla) {
        this.talla = talla;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public double getSubtotal() {
        return (precio >= 0 ? precio : 0.0) * cantidad;
    }
}
