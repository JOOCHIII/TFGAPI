package com.example.logins;

import java.util.List;

public class DetallePedidoResponse {
    private Long id;
    private String fecha;
    private String estado;
    private double total;
    private List<ItemDetalleDTO> detalles;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getFecha() { return fecha; }
    public void setFecha(String fecha) { this.fecha = fecha; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public double getTotal() { return total; }
    public void setTotal(double total) { this.total = total; }

    public List<ItemDetalleDTO> getDetalles() { return detalles; }
    public void setDetalles(List<ItemDetalleDTO> detalles) { this.detalles = detalles; }
}
