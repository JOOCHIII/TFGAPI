package com.example.logins;

import com.google.gson.annotations.SerializedName;

public class CarritoDTO {

    @SerializedName("idProducto")
    private long idProducto;

    @SerializedName("nombreProducto")
    private String nombre;

    @SerializedName("descripcionProducto")
    private String descripcion;

    @SerializedName("precioProducto")
    private double precio;

    @SerializedName("cantidad")
    private int cantidad;

    @SerializedName("talla")
    private String talla;

    @SerializedName("imgurl")
    private String imagenUrl;

    public CarritoDTO(long idProducto, String nombre, String descripcion, double precio,
                      int cantidad, String talla, String imagenUrl) {
        this.idProducto = idProducto;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
        this.cantidad = cantidad;
        this.talla = talla;
        this.imagenUrl = imagenUrl;
    }

    // Getters y setters
    public long getIdProducto() { return idProducto; }
    public void setIdProducto(long idProducto) { this.idProducto = idProducto; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public double getPrecio() { return precio; }
    public void setPrecio(double precio) { this.precio = precio; }

    public int getCantidad() { return cantidad; }
    public void setCantidad(int cantidad) { this.cantidad = cantidad; }

    public String getTalla() { return talla; }
    public void setTalla(String talla) { this.talla = talla; }

    public String getImagenUrl() { return imagenUrl; }
    public void setImagenUrl(String imagenUrl) { this.imagenUrl = imagenUrl; }

    public double getSubtotal() {
        return this.cantidad * this.precio;
    }
}
