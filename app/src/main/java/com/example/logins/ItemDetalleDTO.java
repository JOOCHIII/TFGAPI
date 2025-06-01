package com.example.logins;

public class ItemDetalleDTO {
    private String nombreProducto;
    private int cantidad;
    private String talla;
    private double precioUnitario;
    private String imagenUrl;

    // Getters y setters
    public String getNombreProducto() { return nombreProducto; }
    public void setNombreProducto(String nombreProducto) { this.nombreProducto = nombreProducto; }

    public int getCantidad() { return cantidad; }
    public void setCantidad(int cantidad) { this.cantidad = cantidad; }

    public String getTalla() { return talla; }
    public void setTalla(String talla) { this.talla = talla; }

    public double getPrecioUnitario() { return precioUnitario; }
    public void setPrecioUnitario(double precioUnitario) { this.precioUnitario = precioUnitario; }

    public String getImagenUrl() { return imagenUrl; }
    public void setImagenUrl(String imagenUrl) { this.imagenUrl = imagenUrl; }
}
