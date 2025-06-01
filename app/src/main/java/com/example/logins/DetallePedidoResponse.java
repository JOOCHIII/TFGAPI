package com.example.logins;

import java.util.Collection;
import java.util.List;

public class DetallePedidoResponse {
    private String fecha;
    private String estado;
    private double total;
    private List<ProductoPedidoDTO> productos;

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public Collection<? extends ProductoPedidoDTO> getProductos() {
        return productos;
    }

    public void setProductos(List<ProductoPedidoDTO> productos) {
        this.productos = productos;
    }
// Getters y Setters
}
