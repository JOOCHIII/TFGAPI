package com.example.logins;

public class PedidoAdmin {
    private long id;
    private long id_usuario;
    private String fecha;
    private String estado;
    private double total;
private String nombreUsuario;

    // getters y setters
    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public long getId_usuario() { return id_usuario; }
    public void setId_usuario(long id_usuario) { this.id_usuario = id_usuario; }

    public String getFecha() { return fecha; }
    public void setFecha(String fecha) { this.fecha = fecha; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public double getTotal() { return total; }
    public void setTotal(double total) { this.total = total; }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }
}
