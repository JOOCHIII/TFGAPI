package com.example.logins;

import java.io.Serializable;

public class Carrito implements Serializable {
    private Productos producto;
    private int cantidad;

    public Carrito() {
        // Constructor vacío requerido para Retrofit
    }

    public Carrito(Productos producto, int cantidad) {
        this.producto = producto;
        this.cantidad = cantidad;
    }

    public Productos getProducto() {
        return producto;
    }

    public void setProducto(Productos producto) {
        this.producto = producto;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public double getSubtotal() {
        return producto.getPrecio() * cantidad;
    }
}
