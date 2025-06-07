package com.example.logins;

import java.io.Serializable;
import java.util.List;

public class PedidoDetalleDTO implements Serializable {
    private Long id;
    private String fecha;
    private String estado;
    private double total;
    private List<ItemDetalleDTO> items;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public List<ItemDetalleDTO> getItems() {
        return items;
    }

    public void setItems(List<ItemDetalleDTO> items) {
        this.items = items;
    }

    // Constructor, getters y setters
}
