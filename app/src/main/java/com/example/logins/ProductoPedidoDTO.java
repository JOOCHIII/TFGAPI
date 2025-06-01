package com.example.logins;

public class ProductoPedidoDTO {
    private String nombre;
    private int cantidad;
    private double precio;
    private String imagenUrl;
    private String talla;

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public int getCantidad() { return cantidad; }
    public void setCantidad(int cantidad) { this.cantidad = cantidad; }

    public double getPrecio() { return precio; }
    public void setPrecio(double precio) { this.precio = precio; }

    public String getImagenUrl() { return imagenUrl; }
    public void setImagenUrl(String imagenUrl) { this.imagenUrl = imagenUrl; }
    public String getTalla() { return talla; }
    public void setTalla(String talla) { this.talla = talla; }
}
